package com.wisdom.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wisdom.base.util.RespPageBean;
import com.wisdom.information.bean.dto.InforDTO;
import com.wisdom.information.feign.RemoteInformationService;
import com.wisdom.interlocution.bean.dto.InterlocutionDTO;
import com.wisdom.interlocution.feign.RemoteInterlocutionService;
import com.wisdom.user.bean.dto.AdminLoginDTO;
import com.wisdom.user.bean.dto.AuditImgDTO;
import com.wisdom.user.bean.dto.AuditNoPassDTO;
import com.wisdom.user.bean.dto.AuditTextDTO;
import com.wisdom.user.bean.vo.AdminLoginVO;
import com.wisdom.user.bean.vo.AuditImgVO;
import com.wisdom.user.bean.vo.AuditTextVO;
import com.wisdom.user.bean.vo.NoticeVO;
import com.wisdom.user.mapper.AdminMapper;
import com.wisdom.user.mapper.NoticeMapper;
import com.wisdom.user.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Value("${filesonpath}")
    private String fileSonPath;

    private String filePath;

    @Autowired
    private RemoteInformationService remoteInformationService;

    @Autowired
    private RemoteInterlocutionService remoteInterlocutionService;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private NoticeMapper noticeMapper;

    public AdminServiceImpl() throws IOException {
        filePath = new File("").getCanonicalPath();// 获取项目根目录
    }


    @Override
    public AdminLoginVO login(AdminLoginDTO adminLoginDTO) {
        AdminLoginVO admin = adminMapper.login(adminLoginDTO);
        if (admin == null) {
            return null;
        }else {
            admin.setRole(2);
            return admin;
        }
    }

    @Override
    public RespPageBean reviewText(IPage<AuditTextVO> page) {

        RespPageBean respPageBean = new RespPageBean();

        IPage<AuditTextVO> page1 = adminMapper.reviewText(page);

        List<AuditTextVO> records = page1.getRecords();

        respPageBean.setData(records);

        respPageBean.setTotal(page1.getTotal());

        respPageBean.setPages(page1.getPages());

        return respPageBean;
    }

    @Override
    public RespPageBean textSecurity(IPage<AuditTextVO> page, String val) {
        RespPageBean respPageBean = new RespPageBean();

        IPage<AuditTextVO> page1 = adminMapper.textSecurity(page, val);

        List<AuditTextVO> records = page1.getRecords();

        respPageBean.setData(records);

        respPageBean.setTotal(page1.getTotal());

        respPageBean.setPages(page1.getPages());

        return respPageBean;
    }

    @Override
    public RespPageBean imgSecurity(IPage<AuditImgVO> page) {
        RespPageBean respPageBean = new RespPageBean();

        IPage<AuditImgVO> page1 = adminMapper.imgSecurity(page);

        List<AuditImgVO> records = page1.getRecords();

        for(int i=0;i<records.size();i++) {
            records.get(i).setAuditImg(records.get(i).getAuditImg());
        }

        respPageBean.setData(records);

        respPageBean.setTotal(page1.getTotal());

        respPageBean.setPages(page1.getPages());

        return respPageBean;
    }

    @Override
    public RespPageBean historyText(IPage<AuditTextVO> page) {

        RespPageBean respPageBean = new RespPageBean();

        IPage<AuditTextVO> page1 = adminMapper.historyText(page);

        List<AuditTextVO> records = page1.getRecords();

        respPageBean.setData(records);

        respPageBean.setTotal(page1.getTotal());

        respPageBean.setPages(page1.getPages());

        return respPageBean;
    }

    @Override
    public RespPageBean reviewImg(IPage<AuditImgVO> page) {

        RespPageBean respPageBean = new RespPageBean();

        IPage<AuditImgVO> page1 = adminMapper.reviewImg(page);

        List<AuditImgVO> records = page1.getRecords();

        for(int i=0;i<records.size();i++) {
            records.get(i).setAuditImg(records.get(i).getAuditImg());
        }

        respPageBean.setData(records);

        respPageBean.setTotal(page1.getTotal());

        respPageBean.setPages(page1.getPages());

        return respPageBean;
    }

    @Override
    public RespPageBean historyImg(IPage<AuditImgVO> page) {

        RespPageBean respPageBean = new RespPageBean();

        IPage<AuditImgVO> page1 = adminMapper.historyImg(page);

        List<AuditImgVO> records = page1.getRecords();

        for (int i=0;i<records.size();i++) {
            records.get(i).setAuditImg(records.get(i).getAuditImg());
        }

        respPageBean.setData(records);

        respPageBean.setTotal(page1.getTotal());

        respPageBean.setPages(page1.getPages());

        return respPageBean;
    }

    @Override
    public void addAuditText(AuditTextDTO auditTextDTO) {
        adminMapper.addAuditText(auditTextDTO);
    }

    @Override
    public void overruled(Long id, Long auditId, int auditType) {
        if (auditType == 0) { //文本审核
            adminMapper.updateAuditText(id, 0, 1);
        }else if(auditType == 1){ //图像审核
            adminMapper.updateAuditImg(id, 0, 1);
        }

        noticeMapper.updateNotice(auditId, 3);
    }

    @Override
    public void auditPass(List<String> id, List<String> auditId, int auditType) {

        for (int i=0;i<auditId.size();i++) {
            pass(Long.valueOf(id.get(i)), Long.valueOf(auditId.get(i)), auditType);

        }

    }

    @Override
    public void eliminate(AuditNoPassDTO auditNoPassDTO) {
        for (int i=0;i<auditNoPassDTO.getId().size();i++) {
            noPass(Long.valueOf(auditNoPassDTO.getId().get(i)),
                    Long.valueOf(auditNoPassDTO.getAuditId().get(i)),
                    Long.valueOf(auditNoPassDTO.getUserId().get(i)),
                    auditNoPassDTO.getAuditType(),
                    auditNoPassDTO.getAuditSource().get(i));

        }
    }

    public void noPass(Long id, Long auditId, Long userId, int auditType, int auditSource) {
        if (auditType == 0) { //文本审核
            adminMapper.updateAuditText(id, 0, 1);
        }else if(auditType == 1){ //图像审核
            adminMapper.updateAuditImg(id, 0, 1);
        }
        if (auditSource == 0) {
            InforDTO inforDTO = new InforDTO();
            inforDTO.setDeleted(1);
            inforDTO.setInfoId(auditId);
            //修改资讯
            remoteInformationService.remoteUpdateInfo(inforDTO);
        }else if (auditSource == 1) {
            InterlocutionDTO interlocutionDTO = new InterlocutionDTO();
            interlocutionDTO.setDeleted(2);
            interlocutionDTO.setQueId(auditId);
            //修改问答
            remoteInterlocutionService.remoteUpdateWenDa(interlocutionDTO);
        }

        NoticeVO exist = noticeMapper.isExist(auditId);

        if (exist == null) {
            noticeMapper.addNotice(userId, auditId);
        }else {
            noticeMapper.updateNoticeState(auditId, 0, 0);
        }

    }


    public void pass(Long id, Long auditId, int auditType) {

        if (auditType == 0) { //文本审核
            adminMapper.updateAuditText(id, 2, 1);
        }else if(auditType == 1){ //图像审核
            adminMapper.updateAuditImg(id, 2, 1);
        }

        List<AuditTextVO> auditText = adminMapper.getAuditText(auditId);
        int auditSource = auditText.get(0).getAuditSource();
        for (int i=0;i<auditText.size();i++) {
            if (auditText.get(i).getAuditResult() != 2) {
                return;
            }
        }
        List<AuditImgVO> auditImg = adminMapper.getAuditImg(auditId);
        for (int i=0;i<auditImg.size();i++) {
            if (auditImg.get(i).getAuditResult() != 2) {
                return;
            }
        }


        if (auditSource == 0) {
            InforDTO inforDTO = new InforDTO();
            inforDTO.setDeleted(0);
            inforDTO.setInfoId(auditId);
            //修改资讯
            remoteInformationService.remoteUpdateInfo(inforDTO);
        }else if (auditSource == 1) {
            InterlocutionDTO interlocutionDTO = new InterlocutionDTO();
            interlocutionDTO.setDeleted(0);
            interlocutionDTO.setQueId(auditId);
            //修改问答
            remoteInterlocutionService.remoteUpdateWenDa(interlocutionDTO);
        }

        adminMapper.updateNoticeStatue(1, auditId);

        noticeMapper.updateNotice(auditId, 2);
    }

    @Override
    public void addAuditImg(AuditImgDTO auditImgDTO) {
        adminMapper.addAuditImg(auditImgDTO);
    }

    /**
     * 解析图片名称
     */
    public String img(String img) {
        if(img != null && !img.equals("")) {
            InputStream in = null;
            byte[] data = null;
            try {
                in = new FileInputStream(filePath+fileSonPath +"\\"+img);
                data = new byte[in.available()];
                in.read(data);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //进行Base64编码
            BASE64Encoder encoder = new BASE64Encoder();
            return "data:image/jpeg;base64,"+encoder.encode(data);
        }else {
            return "";
        }
    }
}
