package com.wisdom.user.service.impl;

import com.wisdom.information.bean.vo.InforVO;
import com.wisdom.information.feign.RemoteInformationService;
import com.wisdom.interlocution.bean.vo.IssueVO;
import com.wisdom.interlocution.feign.RemoteInterlocutionService;
import com.wisdom.user.bean.vo.NoticeVO;
import com.wisdom.user.mapper.NoticeMapper;
import com.wisdom.user.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Value("${filesonpath}")
    private String fileSonPath;

    private String filePath;

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private RemoteInformationService remoteInformationService;

    @Autowired
    private RemoteInterlocutionService remoteInterlocutionService;

    public NoticeServiceImpl() throws IOException {
        filePath = new File("").getCanonicalPath();// 获取项目根目录
    }

    @Override
    public List<NoticeVO> stuNotice(Long stuId) {
        List<NoticeVO> noticeVOS = new ArrayList<>();
        List<IssueVO> issueVOS = remoteInterlocutionService.stuNoticeWenDa(stuId);
        for (int i=0;i<issueVOS.size();i++) {
            NoticeVO noticeVO = new NoticeVO();
            noticeVO.setIsPass(noticeMapper.isPass(issueVOS.get(i).getQueId()));
            noticeVO.setNoticeId(issueVOS.get(i).getQueId());
            noticeVO.setTitle(issueVOS.get(i).getTitle());
            noticeVO.setContent(issueVOS.get(i).getContent());
            noticeVO.setImg(issueVOS.get(i).getImg());
            noticeVO.setCreateTime(issueVOS.get(i).getCreateTime());
            noticeVOS.add(noticeVO);
        }
        return noticeVOS;
    }

    @Override
    public List<NoticeVO> teaNotice(Long teaId) {
        List<NoticeVO> noticeVOS = new ArrayList<>();
        List<InforVO> inforVOS = remoteInformationService.teaNoticeInfo(teaId);
        for (int i=0;i<inforVOS.size();i++) {
            NoticeVO noticeVO = new NoticeVO();
            noticeVO.setIsPass(noticeMapper.isPass(inforVOS.get(i).getInfoId()));
            noticeVO.setNoticeId(inforVOS.get(i).getInfoId());
            noticeVO.setTitle(inforVOS.get(i).getInfoTitle());
            noticeVO.setContent(inforVOS.get(i).getInfoMain());
            noticeVO.setImg(inforVOS.get(i).getInfoImage());
            noticeVOS.add(noticeVO);
        }
        return noticeVOS;
    }

    @Override
    public void deleted(List<Long> nId) {
        for (int i=0;i<nId.size();i++) {
            noticeMapper.deleted(nId.get(i));
        }
    }

    @Override
    public void application(Long noticeId) {
        noticeMapper.appliAuditText(noticeId);
        noticeMapper.appliAuditImg(noticeId);
        noticeMapper.updateNotice(noticeId, 1);
    }

    @Override
    public void addNotice(Long userId, Long noticeId) {
        noticeMapper.addNotice(userId, noticeId);
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
