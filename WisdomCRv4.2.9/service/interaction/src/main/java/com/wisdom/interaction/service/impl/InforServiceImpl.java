package com.wisdom.interaction.service.impl;

import com.baidu.aip.contentcensor.AipContentCensor;
import com.baidu.aip.contentcensor.EImgType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wisdom.base.util.*;
import com.wisdom.information.bean.dto.InforDTO;
import com.wisdom.information.bean.vo.ClickVO;
import com.wisdom.information.bean.vo.CommentVO;
import com.wisdom.information.bean.vo.InforVO;
import com.wisdom.interaction.mapper.InforMapper;
import com.wisdom.interaction.service.InforService;
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
import java.util.List;
import java.util.Objects;

@Service
public class InforServiceImpl implements InforService {

    @Resource
    private InforMapper inforMapper;

    @Autowired
    private RemoteUserService remoteUserService;

    @Value("${APP_ID}")
    public String APP_ID;
    @Value("${API_KEY}")
    public String API_KEY;
    @Value("${SECRET_KEY}")
    public String SECRET_KEY;

    @Value("${filesonpath}")
    private String fileSonPath;

    private String filePath;

    public InforServiceImpl() throws IOException {
        filePath = new File("").getCanonicalPath();// 获取项目根目录
    }

    @Override
    public RespPageBean getAllInfor(IPage<InforVO> page, Long userId) {

        RespPageBean respPageBean = new RespPageBean();

        IPage<InforVO> allInfor = inforMapper.getAllInfor(page, userId);

        List<InforVO> records = allInfor.getRecords();

        Long pageNum = allInfor.getPages();

        //当前页未读的资讯条数
        int noRead = 10-records.size();

        //所有未读读的资讯
        Long allNoRead = allInfor.getTotal();

        //所有已读的资讯
        Long allRead = 0l;

        if (noRead > 0) {

            long current1 = page.getCurrent();

            //从第几条来开始对已读进行分页
            Long s1 =  (current1-1)*10;
            int start = (int)(s1 - allNoRead);

            //
            if (start < 0) {
                start = 0;
            }

            List<InforVO> records1 = inforMapper.getAllInfor1(start, noRead, userId);

            allRead = inforMapper.getTotal(userId);

            if (records != null && records.size() != 0) {
                records.addAll(records1);
            }else {
                records = records1;
            }

        }

        for (int i=0;i<records.size();i++) {
            records.get(i).setCount(inforMapper.getCount(records.get(i).getInfoId()));

            records.get(i).setInfoImage(records.get(i).getInfoImage());

            //判断是否已读，0为为未读，1为已读
            if(userId != null && !userId.equals("")) {
                records.get(i).setIsRead(inforMapper.isRead(records.get(i).getInfoId(), userId));
            }else {
                records.get(i).setIsRead(-1);
            }
        }

        if(userId != null && !userId.equals("")) {
            //消息未读的数量
            respPageBean.setUnReadNum((int) allInfor.getTotal());
        }else {
            respPageBean.setUnReadNum(0);
        }


        respPageBean.setData(records);

        respPageBean.setPages(allInfor.getPages());

        respPageBean.setTotal(allRead+allNoRead);

        return respPageBean;
    }

    @Override
    public Long getAllTotal() {
        return inforMapper.getAllTotal();
    }


    @Override
    public RespPageBean getAllInforByTeaId(IPage<InforVO> page, Long teaId) {

        RespPageBean respPageBean = new RespPageBean();

        IPage<InforVO> allInfor = inforMapper.getAllInforByTeaId(page, teaId);

        List<InforVO> records = allInfor.getRecords();

        for (int i=0;i<records.size();i++) {

            records.get(i).setCount(inforMapper.getCount(records.get(i).getInfoId()));

            records.get(i).setInfoImage(records.get(i).getInfoImage());
        }

        respPageBean.setData(records);

        respPageBean.setTotal(allInfor.getTotal());

        return respPageBean;

    }

    @Override
    public R add(InforDTO inforDTO) {

        long infoId = new SnowflakeIdWorker(0, 0).nextId();

        int deleted = 0;

        inforDTO.setInfoId(infoId);

//        String userName = remoteUserService.getTeaNameByTeaId(inforDTO.getTeaId());

        String userName = inforDTO.getInfoAuthor();

        SampleAip sampleAip = new SampleAip(APP_ID, API_KEY, SECRET_KEY);
        //文本内容
        int conclusionTypeTM = Sample.sampleText(sampleAip.getAip(), inforDTO.getInfoMain());

        //标题
        int conclusionTypeTT = Sample.sampleText(sampleAip.getAip(), inforDTO.getInfoTitle());

        //文本信息
        AuditTextDTO auditTextDTO = new AuditTextDTO();
        auditTextDTO.setAuditSource(0);
        auditTextDTO.setRole(0);
        auditTextDTO.setUserId(inforDTO.getTeaId());
        auditTextDTO.setAuditId(infoId);
        auditTextDTO.setUserName(userName);

        if (inforDTO.getInfoMain() != null && !inforDTO.getInfoMain().equals("")) {
            if (conclusionTypeTM != 1) {
                deleted = 1;
                auditTextDTO.setAuditResult(0);
                auditTextDTO.setAuditText(inforDTO.getInfoMain());
                remoteUserService.addAuditText(auditTextDTO);
            }else {
                auditTextDTO.setAuditResult(2);
                auditTextDTO.setAuditText(inforDTO.getInfoMain());
                remoteUserService.addAuditText(auditTextDTO);
            }
        }

        if (inforDTO.getInfoTitle()!= null && !inforDTO.getInfoTitle().equals("")) {
            if (conclusionTypeTT != 1) {
                deleted = 1;
                auditTextDTO.setAuditResult(0);
                auditTextDTO.setAuditText(inforDTO.getInfoTitle());
                remoteUserService.addAuditText(auditTextDTO);
            }else {
                auditTextDTO.setAuditResult(2);
                auditTextDTO.setAuditText(inforDTO.getInfoTitle());
                remoteUserService.addAuditText(auditTextDTO);
            }
        }

        inforDTO.setDeleted(deleted);

        inforMapper.add(inforDTO);

        if (conclusionTypeTM == 1 && conclusionTypeTT == 1) {
            return R.ok("发布成功");
        }else {
            remoteUserService.addNotice(inforDTO.getTeaId(), inforDTO.getInfoId());
            return R.error("内容存在违规");
        }
    }


    @Override
    public InforVO infoDetailByInfoId(Long infoId, Long userId) {

        InforVO inforVO = inforMapper.inforDetail(infoId);

        if (inforVO == null) {
            return null;
        }

        inforVO.setInfoImage(inforVO.getInfoImage());

        inforVO.setImage(inforVO.getImage());

        //评论的数量
        inforVO.setCount((long) inforMapper.getCommenyTotal(infoId));

        //用户是否已经点赞
        if(userId != null && !userId.equals("")){
            inforVO.setRecommend(inforMapper.isRecommend(infoId, userId));

            //未读
            if(inforMapper.isRead(infoId, userId) == 0) {
                //记为已读
                inforMapper.read(infoId, userId);
            }
        }

        return inforVO;
    }

    @Override
    public InforVO commentByInfoId(IPage<CommentVO> page, Long infoId) {

        InforVO inforVO = new InforVO();

        IPage<CommentVO> commentVOs = inforMapper.getCommentByInfoId(page, infoId);

        List<CommentVO> records = commentVOs.getRecords();

        for (int i=0;i<records.size();i++) {

            Integer role = records.get(i).getRole();

            String userName = "";

            String img = null;

            if (role == 0) { //教师

                TeacherDetailedVO teaByTeaId = remoteUserService.getTeaByTeaId(records.get(i).getUserId());
                userName = teaByTeaId.getTeaName();
                img = teaByTeaId.getImage(); //头像

            }else if(role == 1) { //学生

                StudentDetailedVO stuByTeaId = remoteUserService.getStuByStuId(records.get(i).getUserId());
                userName = stuByTeaId.getStuName();
                img = stuByTeaId.getImage();

            }

            records.get(i).setUserName(userName);

            records.get(i).setImage(img);

        }

        inforVO.setCommentVOs(records);

        return inforVO;
    }

    @Override
    public InforVO commentListByInfoId(Long infoId) {
        InforVO inforVO = new InforVO();

        List<CommentVO> records = inforMapper.commentListByInfoId(infoId);

        for (int i=0;i<records.size();i++) {

            Integer role = records.get(i).getRole();

            String userName = "";

            String img = null;

            if (role == 0) { //教师

                TeacherDetailedVO teaByTeaId = remoteUserService.getTeaByTeaId(records.get(i).getUserId());
                userName = teaByTeaId.getTeaName();
                img = teaByTeaId.getImage(); //头像

            }else if(role == 1) { //学生

                StudentDetailedVO stuByTeaId = remoteUserService.getStuByStuId(records.get(i).getUserId());
                userName = stuByTeaId.getStuName();
                img = stuByTeaId.getImage();

            }

            records.get(i).setUserName(userName);

            records.get(i).setImage(img);

        }

        inforVO.setCommentVOs(records);

        return inforVO;
    }

    @Override
    public RespPageBean search(IPage<InforVO> page, String val) {

        RespPageBean respPageBean = new RespPageBean();
        //资讯分页
        IPage<InforVO> allInfor = inforMapper.search(page, val);

        List<InforVO> records = allInfor.getRecords();

        for (int i=0;i<records.size();i++) {
            records.get(i).setCount(inforMapper.getCount(records.get(i).getInfoId()));

            records.get(i).setInfoImage(records.get(i).getInfoImage());

        }
        respPageBean.setData(records);

        respPageBean.setTotal(allInfor.getTotal());

        return respPageBean;
    }

    @Override
    public RespPageBean teaSearch(IPage<InforVO> page, String val, Long teaId) {
        RespPageBean respPageBean = new RespPageBean();
        //资讯分页
        IPage<InforVO> allInfor = inforMapper.teaSearch(page, val, teaId);

        List<InforVO> records = allInfor.getRecords();

        for (int i=0;i<records.size();i++) {
            records.get(i).setCount(inforMapper.getCount(records.get(i).getInfoId()));

            records.get(i).setInfoImage(records.get(i).getInfoImage());

        }

        respPageBean.setData(records);

        respPageBean.setTotal(allInfor.getTotal());

        return respPageBean;
    }

    @RedisLock(name = "approval", key = "#p0 + ':' + #p1")
    @Override
    public ClickVO approval(Long infoId, Long userId) {

        ClickVO clickVO = new ClickVO();

        Long clicks = inforMapper.getClicks(infoId);

        inforMapper.updateInforRecom(infoId, clicks+1);

        inforMapper.addClicks(infoId, userId);

        clickVO.setClicks(clicks+1);

        clickVO.setRecommend(1);

        return clickVO;
    }

    @RedisLock(name = "approval", key = "#p0 + ':' + #p1")
    @Override
    public ClickVO cancelApproval(Long infoId, Long userId) {

        ClickVO clickVO = new ClickVO();

        Long clicks = inforMapper.getClicks(infoId);

        inforMapper.updateInforRecom(infoId, clicks-1);

        inforMapper.deletedClicks(infoId, userId);

        clickVO.setClicks(clicks-1);

        clickVO.setRecommend(0);

        return clickVO;

    }



    @Override
    public void deleted(List<Long> infoId) {
        for (int i=0;i<infoId.size();i++) {
            inforMapper.deletedInfo(infoId.get(i));
            inforMapper.deletedCom(infoId.get(i));
        }
    }

    @Override
    public void update(InforDTO inforDTO) {
        inforMapper.updateInfor(inforDTO);
    }

    @Override
    public void remoteUpdateInfo(InforDTO inforDTO) {
        inforMapper.remoteUpdateInfo(inforDTO);
    }

    @Override
    public List<InforVO> teaNoticeInfo(Long teaId) {
        return inforMapper.teaNoticeInfo(teaId);
    }

    @SneakyThrows
    @Override
    public String inforFile(MultipartFile multipartFile){
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

    /**
     * 解析图片名称
     */
//    public String img(String img) {
//        if(img != null && !img.equals("")) {
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
