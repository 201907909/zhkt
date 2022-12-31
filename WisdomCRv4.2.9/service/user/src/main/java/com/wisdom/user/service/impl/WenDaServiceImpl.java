package com.wisdom.user.service.impl;

import com.wisdom.interlocution.bean.vo.InterlocutionVO;
import com.wisdom.user.mapper.WenDaMapper;
import com.wisdom.user.service.WenDaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WenDaServiceImpl implements WenDaService {

    @Autowired
    private WenDaMapper wenDaMapper;

    @Override
    public List<InterlocutionVO> getMyAllReplys(Long stuId) {

        //先找出问题
        List<InterlocutionVO> myAllIssue = wenDaMapper.getIssue(stuId);
        if (myAllIssue == null) {
            return null;
        }

        for(int i=0;myAllIssue.size()>i;i++) {
            Map map = new HashMap();
            map.put("queId", myAllIssue.get(i).getQueId());
            map.put("userId", stuId);
            myAllIssue.get(i).setReplyVOList(wenDaMapper.getReply(myAllIssue.get(i).getQueId(), stuId));
        }

        return myAllIssue;
    }

    @Override
    public List<InterlocutionVO> getMyIssues(Long stuId) {
        return wenDaMapper.getMyIssues(stuId);
    }

    @Override
    public InterlocutionVO getReplyMyIssue(Long queId) {
        return wenDaMapper.getReplyMyIssue(queId);
    }

    @Override
    public InterlocutionVO getMyReplyByqueId(Long queId, Long stuId) {
        //先找出问题
        InterlocutionVO myIssue = wenDaMapper.getMyReplyByqueId(queId);

        myIssue.setReplyVOList(wenDaMapper.getReply(queId, stuId));

        return myIssue;
    }
}
