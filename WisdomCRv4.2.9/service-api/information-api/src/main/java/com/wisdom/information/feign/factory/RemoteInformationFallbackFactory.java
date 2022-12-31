package com.wisdom.information.feign.factory;

import com.wisdom.base.util.R;
import com.wisdom.information.bean.dto.InforDTO;
import com.wisdom.information.bean.vo.InforVO;
import com.wisdom.information.feign.RemoteInformationService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author WisdomCR
 * @since 2022-10-17
 */
@Slf4j
@Component
public class RemoteInformationFallbackFactory implements FallbackFactory<RemoteInformationService> {
    @Override
    public RemoteInformationService create(Throwable throwable) {
        return new RemoteInformationService() {

            @Override
            public void remoteUpdateInfo(InforDTO inforDTO) {

            }

            @Override
            public List<InforVO> teaNoticeInfo(Long teaId) {
                return null;
            }
        };
    }
}
