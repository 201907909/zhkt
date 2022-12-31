package com.wisdom.interlocution.feign;

import com.wisdom.interlocution.bean.dto.InterlocutionDTO;
import com.wisdom.interlocution.bean.vo.IssueVO;
import com.wisdom.interlocution.feign.factory.RemoteInterlocutionFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author WisdomCR
 * @since 2022-10-17
 */
@FeignClient(value = "interaction", name = "interaction",fallbackFactory = RemoteInterlocutionFallbackFactory.class)
public interface RemoteInterlocutionService {


    @PostMapping("/wenda/remoteUpdateWenDa")
    void remoteUpdateWenDa(@RequestBody InterlocutionDTO interlocutionDTO);

    @GetMapping("/wenda/stuNoticeWenDa")
    List<IssueVO> stuNoticeWenDa(@RequestParam("stuId") Long stuId);
}
