package com.wisdom.interaction.service.impl;

import com.baidu.aip.contentcensor.AipContentCensor;
import com.baidu.aip.contentcensor.EImgType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wisdom.base.util.*;
import com.wisdom.interaction.mapper.WenDaMapper;
import com.wisdom.interaction.service.WenDaService;
import com.wisdom.interlocution.bean.dto.InterlocutionDTO;
import com.wisdom.interlocution.bean.dto.ReplyDTO;
import com.wisdom.interlocution.bean.vo.*;
import com.wisdom.user.bean.dto.AuditImgDTO;
import com.wisdom.user.bean.dto.AuditTextDTO;
import com.wisdom.user.bean.vo.StudentDetailedVO;
import com.wisdom.user.bean.vo.TeacherDetailedVO;
import com.wisdom.user.feign.RemoteUserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;
import top.banner.lib.lock.RedisLock;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class WenDaServiceImpl implements WenDaService {

    @Resource
    private WenDaMapper wenDaMapper;
    @Autowired
    private RemoteUserService remoteUserService;
    @Value("${APP_ID}")
    public String APP_ID;
    @Value("${API_KEY}")
    public String API_KEY;
    @Value("${SECRET_KEY}")
    public String SECRET_KEY;
    @Value("${adoptType}")
    public int adoptType;
    @Value("${score}")
    public int score;

    @Value("${filesonpath}")
    private String fileSonPath;

    private String filePath;

    public WenDaServiceImpl() throws IOException {
        filePath = new File("").getCanonicalPath();// 获取项目根目录
    }


    @Override
    public R add(InterlocutionDTO interlocutionDTO) {
        long queId = new SnowflakeIdWorker(0, 0).nextId();

        String info1 = "";

        String info2 = "";

        int deleted = 0;

        //多张图片逗号隔开
        String img = "";

        interlocutionDTO.setQueId(queId);

        String userName = remoteUserService.getStuByStuId(interlocutionDTO.getUserId()).getStuName();

        SampleAip sampleAip = new SampleAip(APP_ID, API_KEY, SECRET_KEY);

        //图像
        int conclusionTypeI = 1;

        //文本内容
        int conclusionTypeTM = Sample.sampleText(sampleAip.getAip(), interlocutionDTO.getContent());

        //标题
        int conclusionTypeTT = Sample.sampleText(sampleAip.getAip(), interlocutionDTO.getTitle());

        for (int i=0;i<interlocutionDTO.getImgs().size();i++) {
            img = img + "," + interlocutionDTO.getImgs().get(i);
            conclusionTypeI = Sample.sampleImage(sampleAip.getAip(), interlocutionDTO.getImgs().get(i));
            //图像信息
            AuditImgDTO auditImgDTO = new AuditImgDTO();
            //audit_source：1 为问题
            auditImgDTO.setAuditSource(1);
            auditImgDTO.setRole(1);
            auditImgDTO.setUserId(interlocutionDTO.getUserId());
            auditImgDTO.setAuditId(queId);
            auditImgDTO.setUserName(userName);
            auditImgDTO.setAuditImg(interlocutionDTO.getImgs().get(i));

            if (interlocutionDTO.getImgs().get(i)!= null && !interlocutionDTO.getImgs().get(i).equals("")) {
                if (conclusionTypeI != 1) {
                    deleted = 1;
                    auditImgDTO.setAuditResult(0);
                    remoteUserService.addAuditImg(auditImgDTO);
                    info1 = "图像存在违规";
                }else {
                    auditImgDTO.setAuditResult(2);
                    remoteUserService.addAuditImg(auditImgDTO);
                }
            }
        }

        //文本信息
        AuditTextDTO auditTextDTO = new AuditTextDTO();
        auditTextDTO.setAuditSource(1);
        auditTextDTO.setRole(1);
        auditTextDTO.setUserId(interlocutionDTO.getUserId());
        auditTextDTO.setAuditId(queId);
        auditTextDTO.setUserName(userName);

        if (interlocutionDTO.getContent()!= null && !interlocutionDTO.getContent().equals("")) {
            if (conclusionTypeTM != 1) {
                deleted = 1;
                auditTextDTO.setAuditResult(0);
                auditTextDTO.setAuditText(interlocutionDTO.getContent());
                remoteUserService.addAuditText(auditTextDTO);
                info2 = "内容存在违规";
            }else {
                auditTextDTO.setAuditResult(2);
                auditTextDTO.setAuditText(interlocutionDTO.getContent());
                remoteUserService.addAuditText(auditTextDTO);
            }
        }

        if (interlocutionDTO.getTitle()!= null && !interlocutionDTO.getTitle().equals("")) {
            if (conclusionTypeTT != 1) {
                deleted = 1;
                auditTextDTO.setAuditResult(0);
                auditTextDTO.setAuditText(interlocutionDTO.getTitle());
                remoteUserService.addAuditText(auditTextDTO);
                info2 = "内容存在违规";
            }else {
                auditTextDTO.setAuditResult(2);
                auditTextDTO.setAuditText(interlocutionDTO.getTitle());
                remoteUserService.addAuditText(auditTextDTO);
            }
        }

        if (img != null && !img.equals("")) {
            interlocutionDTO.setImg(img.substring(1));
        }else {
            interlocutionDTO.setImg("");
        }

        interlocutionDTO.setDeleted(deleted);

        wenDaMapper.add(interlocutionDTO);

        if (conclusionTypeI == 1 && conclusionTypeTM == 1 && conclusionTypeTT == 1) {
            return R.ok("发布成功");
        }else {
            remoteUserService.addNotice(interlocutionDTO.getUserId(), interlocutionDTO.getQueId());
            if (info1 != ""&&info2 != "") {
                return R.error("内容，图像存在违规！");
            }else if (info1 != ""){
                return R.error(info1);
            }else{
                return R.error(info2);
            }
        }
    }

    @Override
    public List<MaxTypeVO> getAllType() {
        List<MaxTypeVO> allType = wenDaMapper.getAllType();

        for (int i=0;i<allType.size();i++) {
            allType.get(i).setValue(i);
            List<MinTypeVO> minTypeVOList = allType.get(i).getChildren();
            for (int j=0;j<minTypeVOList.size();j++) {
                minTypeVOList.get(j).setValue(j);
            }
        }

        return allType;
    }

    @Override
    public R reply(ReplyDTO replyDTO) {

        SampleAip sampleAip = new SampleAip(APP_ID, API_KEY, SECRET_KEY);

        //文本内容
        int conclusionType = Sample.sampleText(sampleAip.getAip(), replyDTO.getContent());

        if (conclusionType == 1) {
            replyDTO.setQueId(new SnowflakeIdWorker(0, 0).nextId());
            wenDaMapper.reply(replyDTO);
            return R.ok("发表成功");
        }else {
            return R.error("发表失败");
        }

    }

    @Override
    public RespPageBean getAllWenDa(IPage<IssueVO> page) {

        RespPageBean respPageBean = new RespPageBean();

        IPage<IssueVO> allIssue = wenDaMapper.getAllWenDa(page);

        List<IssueVO> records = allIssue.getRecords();

        for (int i=0;i<records.size();i++) {

            if (records.get(i).getImg() != null && !records.get(i).getImg().equals("")) {

                String[] split = records.get(i).getImg().split(",");

                List<String> imgs = Arrays.stream(split).collect(Collectors.toList());

                records.get(i).setImgs(imgs);
            }

            records.get(i).setMaxName(wenDaMapper.getTypeName(records.get(i).getMaxTypeId()));
            records.get(i).setMinName(wenDaMapper.getTypeName(records.get(i).getMinTypeId()));
            records.get(i).setCount(wenDaMapper.getCount(records.get(i).getQueId()));
        }

        respPageBean.setData(records);

        respPageBean.setPages(allIssue.getPages());

        respPageBean.setTotal(allIssue.getTotal());

        return respPageBean;
    }

    @Override
    public Long getAllTotal() {
        return wenDaMapper.getAllTotal();
    }

    @Override
    public RespPageBean teaGetAllWenDa(IPage<IssueVO> page, Long teaId) {
        RespPageBean respPageBean = new RespPageBean();

        List<Long> stuIds = wenDaMapper.getClaAllStuByTeaId(teaId);

        IPage<IssueVO> allIssue = wenDaMapper.getAllWenDa(page);

        List<IssueVO> records = allIssue.getRecords();

        for (int i=0;i<records.size();i++) {
            //学生Id
            Long userId = records.get(i).getUserId();
            if (stuIds.contains(userId)) {
                records.get(i).setIsMyStu(1);
            }else {
                records.get(i).setIsMyStu(0);
            }

            if (records.get(i).getImg() != null && !records.get(i).getImg().equals("")) {

                String[] split = records.get(i).getImg().split(",");

                List<String> imgs = Arrays.stream(split).collect(Collectors.toList());

                records.get(i).setImgs(imgs);
            }

            records.get(i).setMaxName(wenDaMapper.getTypeName(records.get(i).getMaxTypeId()));
            records.get(i).setMinName(wenDaMapper.getTypeName(records.get(i).getMinTypeId()));
            records.get(i).setCount(wenDaMapper.getCount(records.get(i).getQueId()));
        }

        respPageBean.setData(records);

        respPageBean.setPages(allIssue.getPages());

        respPageBean.setTotal(allIssue.getTotal());

        return respPageBean;
    }

    /**
     *查看问题详情，传问题Id，用户Id，分页
     */
    @Override
    public IssueVO issueVODetail(Long queId, Long userId) {

        IssueVO issueVO = wenDaMapper.issueVODetail(queId);

        if (issueVO.getUserId().equals(userId)) {
            issueVO.setIsPublish(1);
        }else {
            issueVO.setIsPublish(0);
        }

        if (issueVO.getImg() != null && !issueVO.getImg().equals("")) {

            String[] split = issueVO.getImg().split(",");

            List<String> imgs = Arrays.stream(split).collect(Collectors.toList());

            issueVO.setImgs(imgs);
        }

        issueVO.setImage(issueVO.getImage());

        issueVO.setMaxName(wenDaMapper.getTypeName(issueVO.getMaxTypeId()));

        issueVO.setMinName(wenDaMapper.getTypeName(issueVO.getMinTypeId()));

        issueVO.setCount(wenDaMapper.getCount(queId));

        issueVO.setIsClick(wenDaMapper.isClick(queId, userId));

        return issueVO;
    }

    @Override
    public IssueVO replyVOSDetail(Long queId, IPage<ReplyVO> page) {

        IssueVO issueVO = new IssueVO();

        IPage<ReplyVO> replyVOIPage = wenDaMapper.getReplyByInfoId(page, queId);

        List<ReplyVO> records = replyVOIPage.getRecords();

        for (int i=0;i<records.size();i++) {
            String userName = "";
            String image = null;
            TeacherDetailedVO teaByTeaId = remoteUserService.getTeaByTeaId(records.get(i).getUserId());
            if (teaByTeaId != null) { //教师
                userName = teaByTeaId.getTeaName();
                image = teaByTeaId.getImage(); //头像
            }else { //学生
                StudentDetailedVO stuByTeaId = remoteUserService.getStuByStuId(records.get(i).getUserId());
                userName = stuByTeaId.getStuName();
                image = stuByTeaId.getImage();
            }

            records.get(i).setUserName(userName);

            records.get(i).setImage(image);
        }

        issueVO.setReplyVOs(records);

        return issueVO;
    }

    @Override
    public IssueVO teaIssueVODetail(Long queId, Long userId) {

        IssueVO issueVO = wenDaMapper.issueVODetail(queId);

        if (issueVO.getUserId().equals(userId)) {
            issueVO.setIsPublish(1);
        }else {
            issueVO.setIsPublish(0);
        }

        if (issueVO.getImg() != null && !issueVO.getImg().equals("")) {

            String[] split = issueVO.getImg().split(",");

            List<String> imgs = Arrays.stream(split).collect(Collectors.toList());

            issueVO.setImgs(imgs);
        }

        issueVO.setImage(issueVO.getImage());

        issueVO.setMaxName(wenDaMapper.getTypeName(issueVO.getMaxTypeId()));

        issueVO.setMinName(wenDaMapper.getTypeName(issueVO.getMinTypeId()));

        List<ReplyVO> records = wenDaMapper.teaGetReplyByInfoId(queId);

        for (int i=0;i<records.size();i++) {
            String userName = "";
            String image = null;
            TeacherDetailedVO teaByTeaId = remoteUserService.getTeaByTeaId(records.get(i).getUserId());
            if (teaByTeaId != null) { //教师
                userName = teaByTeaId.getTeaName();
                image = teaByTeaId.getImage(); //头像
            }else { //学生
                StudentDetailedVO stuByTeaId = remoteUserService.getStuByStuId(records.get(i).getUserId());
                userName = stuByTeaId.getStuName();
                image = stuByTeaId.getImage();
            }

            records.get(i).setUserName(userName);

            records.get(i).setImage(image);
        }

        issueVO.setIsClick(wenDaMapper.isClick(queId, userId));

        issueVO.setReplyVOs(records);

        return issueVO;
    }

    @Override
    public IssueVO issueVODetailList(Long queId, Long userId) {

        IssueVO issueVO = wenDaMapper.issueVODetail(queId);

        if (issueVO.getUserId() == userId) {
            issueVO.setIsPublish(1);
        }else {
            issueVO.setIsPublish(0);
        }
//        String img = issueVO.getImg();
//        InputStream in = null;
//        byte[] data = null;
//        try {
//            in = new FileInputStream(filePath+"\\"+img);
//            data = new byte[in.available()];
//            in.read(data);
//            in.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        //进行Base64编码
//        BASE64Encoder encoder = new BASE64Encoder();
//        issueVO.setImg("data:image/jpeg;base64,"+encoder.encode(data));

        issueVO.setCount(wenDaMapper.getCount(queId));

        issueVO.setMaxName(wenDaMapper.getTypeName(issueVO.getMaxTypeId()));

        issueVO.setMinName(wenDaMapper.getTypeName(issueVO.getMinTypeId()));

        List<ReplyVO> replyByInfoIdList = wenDaMapper.getReplyByInfoIdList(queId);

        issueVO.setIsClick(wenDaMapper.isClick(queId, userId));

//        issueVO.setReplyVOs(replyByInfoIdList);

        return issueVO;
    }

    @RedisLock(name = "wenDaClick", key = "#p0 + ':' + #p1")
    @Override
    public ClickVO click(Long queId, Long userId) {

        ClickVO clickVO = new ClickVO();

        Long clicks = wenDaMapper.getClicks(queId);

        wenDaMapper.addClick(queId, userId); //修改
        wenDaMapper.updateIssueClicks(queId, clicks + 1);

        clickVO.setClicks(clicks + 1);
        clickVO.setIsClick(1);

        return clickVO;
    }

    @RedisLock(name = "wenDaClick", key = "#p0 + ':' + #p1")
    @Override
    public ClickVO cancelClick(Long queId, Long userId) {

        ClickVO clickVO = new ClickVO();

        Long clicks = wenDaMapper.getClicks(queId);

        wenDaMapper.deletedClick(queId, userId);
        wenDaMapper.updateIssueClicks(queId, clicks - 1);

        clickVO.setClicks(clicks - 1);
        clickVO.setIsClick(0);

        return clickVO;
    }

    @Override
    public void adopt(Long replyId, Long userId, Long queId) {

        wenDaMapper.updateAllComAdopt(queId);
        wenDaMapper.deletedAllAdopt(queId);
        wenDaMapper.addAdopt(replyId, userId);
        wenDaMapper.updateComAdopt(replyId, 1);

        Long userIdByReplyId = wenDaMapper.getUserIdByReplyId(replyId);

        wenDaMapper.addScore(userIdByReplyId, adoptType, score);
        int stuScoreByStuId = remoteUserService.getStuScoreByStuId(userIdByReplyId);
        remoteUserService.updateScore(userIdByReplyId, stuScoreByStuId+score);


    }

    @Override
    public void cancelAdopt(Long replyId, Long userId) {
        wenDaMapper.updateComAdopt(replyId, 0);
        wenDaMapper.deletedAdopt(replyId, userId);
    }

    @Override
    public RespPageBean search(String val, IPage<IssueVO> page) {

        RespPageBean respPageBean = new RespPageBean();

        IPage<IssueVO> issueVOIPage = wenDaMapper.search(val, page);

        List<IssueVO> search = issueVOIPage.getRecords();

        for (int i=0;i<search.size();i++) {

            search.get(i).setMaxName(wenDaMapper.getTypeName(search.get(i).getMaxTypeId()));

            search.get(i).setMinName(wenDaMapper.getTypeName(search.get(i).getMinTypeId()));

            search.get(i).setCount(wenDaMapper.getCount(search.get(i).getQueId()));

            if (search.get(i).getImg() != null && !search.get(i).getImg().equals("")) {
                String[] split = search.get(i).getImg().split(",");

                List<String> imgs = Arrays.stream(split).collect(Collectors.toList());

                search.get(i).setImgs(imgs);
            }

        }

        respPageBean.setData(search);

        respPageBean.setTotal(issueVOIPage.getSize());

        return respPageBean;
    }

    @SneakyThrows
    @Override
    public String wenDaFile(MultipartFile multipartFile){
        if(Objects.isNull(multipartFile)){
            return "请选择要上传的文件";
        }
        if(multipartFile.getSize() > 1024*1024*1024){
            return "文件大小不能超过10M！";
        }
        //获取文件后缀
        assert multipartFile.getContentType() != null;
        if(multipartFile.getContentType().contains("image/png, image/jpeg, image/gif")){
            return "请选择jpg,png格式的图片";
        }
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0,0);
        String filename = idWorker.nextId()+multipartFile.getOriginalFilename();
        File savePathFile = new File(filePath+fileSonPath + filename);
        if(!savePathFile.exists()){
            //若不存在该目录，则创建目录
            savePathFile.mkdirs();
        }
        multipartFile.transferTo(savePathFile);
        return filename;
    }

    @Override
    public void deletedWenDa(List<Long> qId) {
        wenDaMapper.deletedWenDa(qId);
        wenDaMapper.deletedReply(qId);
    }

    @Override
    public void remoteUpdateWenDa(InterlocutionDTO interlocutionDTO) {
        wenDaMapper.remoteUpdateWenDa(interlocutionDTO);
    }

    @Override
    public List<IssueVO> stuNoticeWenDa(Long stuId) {
        return wenDaMapper.stuNoticeWenDa(stuId);
    }

    //--------------------------------------------------------------------------------------------------

    @Override
    public List<InterlocutionVO> getMyAllReplys(Long stuId) {

        //先找出问题
        List<InterlocutionVO> myAllIssue = wenDaMapper.getIssue(stuId);

        if (myAllIssue == null) {
            return null;
        }

        for(int i=0;myAllIssue.size()>i;i++) {

            myAllIssue.get(i).setImg(myAllIssue.get(i).getImg());

            myAllIssue.get(i).setImage(myAllIssue.get(i).getImage());

            List<ReplyVO> reply = wenDaMapper.getReply(myAllIssue.get(i).getQueId(), stuId);
            for (int j=0;j<reply.size();j++) {
                String userName = "";
                String image = null;
                TeacherDetailedVO teaByTeaId = remoteUserService.getTeaByTeaId(reply.get(j).getUserId());
                if (teaByTeaId != null) { //教师
                    userName = teaByTeaId.getTeaName();
                    image = teaByTeaId.getImage(); //头像
                }else { //学生
                    StudentDetailedVO stuByTeaId = remoteUserService.getStuByStuId(reply.get(j).getUserId());
                    userName = stuByTeaId.getStuName();
                    image = stuByTeaId.getImage();
                }

                reply.get(j).setUserName(userName);

                reply.get(j).setImage(image);
            }
            myAllIssue.get(i).setReplyVOList(reply);
        }

        return myAllIssue;
    }

    @Override
    public List<InterlocutionVO> getMyIssues(Long stuId) {
        List<InterlocutionVO> myIssues = wenDaMapper.getMyIssues(stuId);
        for (int i=0;i<myIssues.size();i++) {

            myIssues.get(i).setMaxName(wenDaMapper.getTypeName(myIssues.get(i).getMaxTypeId()));

            myIssues.get(i).setMinName(wenDaMapper.getTypeName(myIssues.get(i).getMinTypeId()));

            myIssues.get(i).setCount(wenDaMapper.getCount(myIssues.get(i).getQueId()));

            if (myIssues.get(i).getImg() != null && !myIssues.get(i).getImg().equals("")) {
                String[] split = myIssues.get(i).getImg().split(",");

                List<String> imgs = Arrays.stream(split).collect(Collectors.toList());

                myIssues.get(i).setImgs(imgs);
            }

            myIssues.get(i).setCount(wenDaMapper.getCount(myIssues.get(i).getQueId()));

        }
        return myIssues;
    }

    @Override
    public InterlocutionVO getReplyMyIssue(Long queId) {
        InterlocutionVO replyMyIssue = wenDaMapper.getReplyMyIssue(queId);
        replyMyIssue.setImg(replyMyIssue.getImg());
        List<ReplyVO> replyVOList = replyMyIssue.getReplyVOList();
        for (int i=0;i<replyVOList.size();i++) {
            String userName = "";
            String image = null;
            TeacherDetailedVO teaByTeaId = remoteUserService.getTeaByTeaId(replyVOList.get(i).getUserId());
            if (teaByTeaId != null) { //教师
                userName = teaByTeaId.getTeaName();
                image = teaByTeaId.getImage(); //头像
            }else { //学生
                StudentDetailedVO stuByTeaId = remoteUserService.getStuByStuId(replyVOList.get(i).getUserId());
                userName = stuByTeaId.getStuName();
                image = stuByTeaId.getImage();
            }

            replyVOList.get(i).setUserName(userName);

            replyVOList.get(i).setImage(image);
        }
        return replyMyIssue;
    }

    @Override
    public InterlocutionVO getMyReplyByqueId(Long queId, Long stuId) {
        //先找出问题
        InterlocutionVO myIssue = wenDaMapper.getMyReplyByqueId(queId);
        myIssue.setImg(myIssue.getImg());
        myIssue.setImage(myIssue.getImage());
        List<ReplyVO> reply = wenDaMapper.getReply(queId, stuId);
        for (int i=0;i<reply.size();i++) {
            reply.get(i).setImage(reply.get(i).getImage());
        }
        myIssue.setReplyVOList(reply);

        return myIssue;
    }

    /**
     * 解析图片名称
     */
//    public String img(String img) {
//        if(img != null && !img.equals("")) {
//            System.out.println(img);
//            InputStream in = null;
//            byte[] data = null;
//            try {
//                in = new FileInputStream(filePath+fileSonPath +"\\"+img);
//                data = new byte[in.available()];
//                in.read(data);
//                in.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            //进行Base64编码
//            BASE64Encoder encoder = new BASE64Encoder();
//            return "data:image/jpeg;base64,"+encoder.encode(data);
//        }else {
//            return "";
//        }
//    }

}
