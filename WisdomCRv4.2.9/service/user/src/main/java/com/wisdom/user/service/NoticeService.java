package com.wisdom.user.service;

import com.wisdom.user.bean.vo.NoticeVO;

import java.util.List;

public interface NoticeService {

    List<NoticeVO> stuNotice(Long stuId);

    List<NoticeVO> teaNotice(Long teaId);

    void deleted(List<Long> nId);

    void application(Long noticeId);

    void addNotice(Long userId, Long noticeId);

}
