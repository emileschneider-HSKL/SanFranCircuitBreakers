package com.hskl.sanfrancircuitbreakers.filter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter implements Filter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private static final long BUCKET_CAPACITY = 90000000; //90000
    private static final long REFILL_TOKENS = 10000;
    private static final Duration REFILL_DURATION = Duration.ofSeconds(30);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String ip = httpRequest.getRemoteAddr();
        Bucket bucket = buckets.computeIfAbsent(ip, this::newBucket);

        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
        } else {
            System.out.println("⚠️ Ratelimiter aktiv für: " + ip);
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(200); // oder 429, wie du willst
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("{\"message\": \"Rate Limit überschritten. Bitte warte kurz.\"}");
            httpResponse.getWriter().flush();
            httpResponse.getWriter().close();
        }
    }

    private Bucket newBucket(String ip) {
        return Bucket.builder()
                .addLimit(Bandwidth.classic(BUCKET_CAPACITY, Refill.greedy(REFILL_TOKENS, REFILL_DURATION)))
                .build();
    }
}
