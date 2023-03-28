package com.codeqiu.project.filter;

import com.codeqiu.qapicommon.model.entity.InterfaceInfo;
import com.codeqiu.qapicommon.model.entity.User;
import com.codeqiu.qapicommon.service.InnerInterfaceInfoService;
import com.codeqiu.qapicommon.service.InnerUserInterfaceInfoService;
import com.codeqiu.qapicommon.service.InnerUserService;
import com.codeqiu.apiclientsdk.utils.SignUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {
//    private static final List<String> QUERY_URI = Arrays.asList("127.0.0.1");

    private static final String INVOKE_PATH = "http://39.107.240.179:7523"; // 192.168.101.128

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    @DubboReference
    private InnerUserService innerUserService;

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;


    /**
     * 全局过滤
     *
     * @param exchange
     * @param chain
     * @return
     */
    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 请求日志
        ServerHttpRequest request = exchange.getRequest();
        String path = INVOKE_PATH + request.getPath().toString();
        String method = request.getMethodValue();
        log.info("请求唯一标识" + request.getId());
        log.info("请求路径" + path);
        log.info("请求参数" + request.getQueryParams());
        log.info("请求方法" + method);
        String hostString = request.getRemoteAddress().getHostString();
        log.info("请求来源路径" + request.getRemoteAddress());
        log.info("请求uri" + request.getURI());
        log.info("custom global filter");

        // 访问控制 --- 黑白名单
        ServerHttpResponse response = exchange.getResponse();
//        if (!QUERY_URI.contains(hostString)) {
//            return handleNoAuth(response);
//        }
        HttpHeaders headers = request.getHeaders();
        // 用户鉴权
        String accessKey = headers.getFirst("accessKey");
        String body = URLDecoder.decode(headers.getFirst("body"), "utf-8");
        String sign = headers.getFirst("sign");
        String timestamp = headers.getFirst("timestamp");
        String nonce = headers.getFirst("nonce");
        // todo 实际情况要从数据库中查询
        User invokeUser = null;
        try {
            invokeUser = innerUserService.getInvokeUser(accessKey);
        } catch (Exception e) {
            log.error("getInvokeUser error");
        }
        if (invokeUser == null) {
            return handleNoAuth(response);
        }

//        if (!"codeqiu".equals(accessKey)) {
//            return handleNoAuth(response);
//        }

        if (Long.parseLong(nonce) > 10000) {
            return handleNoAuth(response);
        }
        // 与当前时间相差不到五分钟
        long timeMillis = System.currentTimeMillis();
        long FIVE_MILLIS = 60 * 5L;
        if ((timeMillis - Long.parseLong(timestamp)) <= FIVE_MILLIS) {
            return handleNoAuth(response);
        }

        // todo 需要从数据库中取出 secretKey
        String secretKey = invokeUser.getSecretKey();
        String serverSign = SignUtils.genSign(body, secretKey);
        if (!Objects.equals(sign, serverSign)) {
            return handleNoAuth(response);
        }
        // 请求的模拟接口是否存在
        // todo 从数据库中检查接口是否存在
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(path, method);
        } catch (Exception e) {
            log.error("getInterfaceInfo error");
        }
        if (interfaceInfo == null) {
            return handleNoAuth(response);
        }
        // 判断用户是否第一次调用，如果第一次调用则往数据库中添加一条数据，设置调用次数为 10
        innerUserInterfaceInfoService.checkUserInterfaceinfo(interfaceInfo.getId(), invokeUser.getId());

        // todo 判断用户是否还有该接口的调用次数
        boolean isLeftNum = false;
        try {
            isLeftNum = innerUserInterfaceInfoService.getUserLeftNum(interfaceInfo.getId(), invokeUser.getId());
        } catch (Exception e) {
            log.error("getUserLeftNum error");
        }
        if (!isLeftNum) {
            return handleNoAuth(response);
        }
        // 请求转发，调用模拟接口 响应日志
//        Mono<Void> filter = chain.filter(exchange);
        return handleResponse(exchange, chain,interfaceInfo.getId(), invokeUser.getId());

    }

    /**
     * 处理响应
     *
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain,long interfaceInfoId, long userId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 缓存数据的工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 拿到响应码
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                // 装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 等调用完转发的接口后才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 往返回值里写数据
                            // 拼接字符串
                            return super.writeWith(
                                    fluxBody.map(dataBuffer -> {
                                        // 7. 调用成功，接口调用次数 + 1 invokeCount
                                        try {
                                            innerUserInterfaceInfoService.invokeCount(interfaceInfoId, userId);
                                        } catch (Exception e) {
                                            log.error("invokeCount error", e);
                                        }
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        DataBufferUtils.release(dataBuffer);//释放掉内存
                                        // 构建日志
                                        StringBuilder sb2 = new StringBuilder(200);
                                        List<Object> rspArgs = new ArrayList<>();
                                        rspArgs.add(originalResponse.getStatusCode());
                                        String data = new String(content, StandardCharsets.UTF_8); //data
                                        sb2.append(data);
                                        // 打印日志
                                        log.info("响应结果：" + data);
                                        return bufferFactory.wrap(content);
                                    }));
                        } else {
                            // 8. 调用失败，返回一个规范的错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 设置 response 对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange); // 降级处理返回数据
        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }


    private static Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    private static Mono<Void> handleInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }


    @Override
    public int getOrder() {
        return -1;
    }
}
