package com.jecfalo.palermus_api.core.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitFilter extends OncePerRequestFilter {
    private static final int MAX_REQUEST = 10;
    private static final long WINDOW_MS = 60_000;

    private final Map<String, RequestCounter> requestConterMap = new ConcurrentHashMap<>();
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        String method = request.getMethod();

        //aplicar limiting para puntos publicos
        boolean isRateLimitedEndpoint =
                (path.equals("/api/users") && method.equals("POST")) ||
                        (path.equals("/api/login") && method.equals("POST"));
        if(isRateLimitedEndpoint){
            filterChain.doFilter(request, response);
            return;
        }
        String clientIp = getClientIp(request);
        cleanExpiredEntries();

        RequestCounter counter = requestConterMap.computeIfAbsent(clientIp, k -> new RequestCounter());
        if(counter.isExpired()){
            counter.reset();
        }
        if(counter.incrementAndGet() > MAX_REQUEST){
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(
                    "{\"timestamp\":\"" + java.time.LocalDateTime.now() + "\"," +
                            "\"status\":429," +
                            "\"error\":\"Demasiadas Solicitudes\"," +
                            "\"message\":\"Has excedido el límite de peticiones. Intenta de nuevo en unos segundos.\"}"
            );
            return;
        }
        filterChain.doFilter(request, response);
    }
    private String getClientIp(HttpServletRequest request){
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if(xForwardedFor != null && !xForwardedFor.isEmpty()){
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteUser();
    }
    private void cleanExpiredEntries(){
        requestConterMap.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }
    private static class RequestCounter {
        private final AtomicInteger count = new AtomicInteger(0);
        private volatile long windowStart = System.currentTimeMillis();
        public boolean isExpired(){
            return System.currentTimeMillis() - windowStart > WINDOW_MS;
        }
        public void reset(){
            count.set(0);
            windowStart = System.currentTimeMillis();
        }
        public int incrementAndGet(){
            return count.incrementAndGet();
        }
    }
}
