package com.wisdom.information.feign;

import com.wisdom.base.util.R;
import com.wisdom.information.bean.dto.InforDTO;
import com.wisdom.information.bean.vo.InforVO;
import com.wisdom.information.feign.factory.RemoteInformationFallbackFactory;
import io.swagger.annotations.ApiOperation;
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
@FeignClient(value = "interaction", name = "interaction",fallbackFactory = RemoteInformationFallbackFactory.class)
public interface RemoteInformationService {

    @PostMapping("/information/remoteUpdateInfo")
    void remoteUpdateInfo(@RequestBody InforDTO inforDTO);

    @GetMapping("/information/teaNoticeInfo")
    List<InforVO> teaNoticeInfo(@RequestParam("teaId") Long teaId);

}
