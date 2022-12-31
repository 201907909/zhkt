package com.wisdom.user.config.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisdom.base.util.Result;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author WisdomCR
 * @since  2022/10/18
 */
@Component
public class MyUrlBlockHandler implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response, BlockException e) throws Exception {
        Result result = null;
        if (e instanceof FlowException) {
            result = Result.builder()
                    .code("flow ")
                    .message("限流了")
                    .build();
        } else if (e instanceof DegradeException) {
            result = Result.builder()
                    .code("degrade")
                    .message("降级了")
                    .build();
        } else if (e instanceof ParamFlowException) {
            result = Result.builder()
                    .code("param flow")
                    .message("热点参数限流")
                    .build();
        } else if (e instanceof SystemBlockException) {
            result = Result.builder()
                    .code("system block")
                    .message("系统规则（负载/...不满足要求）")
                    .build();
        } else if (e instanceof AuthorityException) {
            result = Result.builder()
                    .code("authority")
                    .message("授权规则不通过")
                    .build();
        }
        response.setStatus(400);
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Type", "application/json;charset=utf-8");
        response.setContentType("application/json;charset=utf-8");
        new ObjectMapper().writeValue(response.getWriter(), result);
    }
}
