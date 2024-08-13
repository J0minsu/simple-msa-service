package com.sparta.msa_exam.auth.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.logging.Logger;

@Component
public class BaseFilter extends OncePerRequestFilter implements Ordered {

    private static final Logger logger = Logger.getLogger(BaseFilter.class.getName());

    @Value("${server.port}")
    private String PORT;

    /*@Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse();
            logger.info("Post Filter: Response status code is " + response.getStatusCode());
            response.getHeaders().set("Server-Port", PORT);
            // Add any custom logic here
        }));
    }*/

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("Server-Port", PORT);
        filterChain.doFilter(request, response);
    }
}