package com.wisdom.interlocution.feign.factory;

import com.wisdom.interlocution.bean.dto.InterlocutionDTO;
import com.wisdom.interlocution.bean.vo.IssueVO;
import com.wisdom.interlocution.feign.RemoteInterlocutionService;
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
public class RemoteInterlocutionFallbackFactory implements FallbackFactory<RemoteInterlocutionService> {
    @Override
    public RemoteInterlocutionService create(Throwable throwable) {
        return new RemoteInterlocutionService() {

            @Override
            public void remoteUpdateWenDa(InterlocutionDTO inforDTO) {

            }

            @Override
            public List<IssueVO> stuNoticeWenDa(Long stuId) {
                return null;
            }
        };
    }
}
