package com.wisdom.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisdom.gateway.config.IgnoreUrlsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;

/**
 * token校验
 * @author WisdomCR
 * @since  2022/10/26
 */
@Component
public class TokenCheckFilter implements Ordered, GlobalFilter {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        InetSocketAddress localAddress = request.getRemoteAddress();
        String ip = localAddress.getHostString();

        //白名单请求直接放行
        PathMatcher pathMatcher = new AntPathMatcher();

        for (String path : ignoreUrlsConfig.getUrls()) {
            if (pathMatcher.match(path, request.getURI().getPath())) {
                return chain.filter(exchange);
            }
        }

//        //判断是否是白名单
//        if(ignoreUrlsConfig.getUrls().contains(path)) {
//            return chain.filter(exchange);
//        }

        HttpHeaders headers = request.getHeaders();

        List<String> authorization = headers.get("Authorization");

        System.out.println(authorization);

        if(!CollectionUtils.isEmpty(authorization)) {
            String token = authorization.get(0);
            if(StringUtils.hasText(token)) {
                //约定好的有前缀，将"Bearer "替换成""
                String realToken = token.replaceFirst("Bearer ", "");
                System.out.println(localAddress.getHostName());
                System.out.println(realToken);
                String s = stringRedisTemplate.opsForValue().get(realToken);
                System.out.println(s);
                if (StringUtils.hasText(realToken) && stringRedisTemplate.hasKey(realToken)) {
                    if (s.equals(ip)) {
                        return chain.filter(exchange);
                    }
                }
            }
        }

        //拦截
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().set("content-type", "application/json;charset=utf-8");
        HashMap<String, Object> map = new HashMap<>(4);
        map.put("code", HttpStatus.UNAUTHORIZED.value());
        map.put("msg", "未授权");
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] bytes = new byte[0];
        try {
            bytes = objectMapper.writeValueAsBytes(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        DataBuffer wrap = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(wrap));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
