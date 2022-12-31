package com.wisdom.interaction.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wisdom.base.util.R;
import com.wisdom.base.util.RespPageBean;
import com.wisdom.information.bean.dto.InforDTO;
import com.wisdom.information.bean.vo.ClickVO;
import com.wisdom.information.bean.vo.CommentVO;
import com.wisdom.information.bean.vo.InforVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface InforService {
    RespPageBean getAllInfor(IPage<InforVO> page, Long userId);
    Long getAllTotal();
    RespPageBean getAllInforByTeaId(IPage<InforVO> page, Long teaId);
    R add(InforDTO inforDTO);
    InforVO infoDetailByInfoId(Long infoId, Long userId);
    InforVO commentByInfoId(IPage<CommentVO> page, Long infoId);
    InforVO commentListByInfoId(Long infoId);
    RespPageBean search(IPage<InforVO> page, String val);
    RespPageBean teaSearch(IPage<InforVO> page, String val, Long teaId);
    ClickVO approval(Long infoId, Long userId);
    ClickVO cancelApproval(Long infoId, Long userId);
    void deleted(List<Long> infoId);
    void update(InforDTO inforDTO);
    void remoteUpdateInfo(InforDTO inforDTO);
    List<InforVO> teaNoticeInfo(Long teaId);
    String inforFile(MultipartFile multipartFile);
}
