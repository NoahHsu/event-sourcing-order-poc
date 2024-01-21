package org.example.event.sourcing.order.poc.modules.idempotency.filter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.lang.String.join;
import static org.springframework.http.HttpStatus.TOO_EARLY;

@Slf4j
@RequiredArgsConstructor
public class IdempotenceFilter extends OncePerRequestFilter {

    private static final String REQUEST_ID_KEY = "rid";
    private static final String SERVICE_ID_KEY = "sid";
    public static final String DELIMITER = "_";
    private final RedisTemplate<String, IdempotencyValue> redisTemplate;
    private long defaultTtl = 2880;

    private final ObjectMapper OBJECT_MAPPER = initObjectMapper();

    private ObjectMapper initObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.debug("start IdempotenceFilter");

        String method = request.getMethod();
        String requestId = request.getHeader(REQUEST_ID_KEY);
        String serviceId = request.getHeader(SERVICE_ID_KEY);
        String cacheKey = join(DELIMITER,
                method, request.getRequestURI(), serviceId, requestId);

        if (isNotTargetMethod(method)) {
            log.info("Request method {} didn't match the target idempotency https method.", method);
            filterChain.doFilter(request, response);
        } else if (StringUtils.isBlank(requestId)
                || StringUtils.isBlank(serviceId)) {
            log.warn("Request should bring a RequestId and ServiceId in header, but no. get cacheKey as {}.", cacheKey);
            filterChain.doFilter(request, response);
        } else {
            log.info("requestId and serviceId not empty, rid = {}, sid = {}", requestId, serviceId);
            BoundValueOperations<String, IdempotencyValue> keyOperation = redisTemplate.boundValueOps(cacheKey);
            boolean isAbsent = keyOperation.setIfAbsent(IdempotencyValue.init(), defaultTtl, TimeUnit.MINUTES);
            if (isAbsent) {
                log.info("cache {} not exist ", cacheKey);
                ContentCachingResponseWrapper responseCopier = new ContentCachingResponseWrapper(response);

                filterChain.doFilter(request, responseCopier);

                updateResultInCache(request, responseCopier, keyOperation);
                responseCopier.copyBodyToResponse();
            } else {
                log.info("cache {} already exist ", cacheKey);
                handleWhenCacheExist(request, response, keyOperation);
            }

        }

    }

    private boolean isNotTargetMethod(String method) {
        return !HttpMethod.POST.matches(method);
    }

    private void handleWhenCacheExist(HttpServletRequest request, HttpServletResponse response,
                                      BoundValueOperations<String, IdempotencyValue> keyOperation)
            throws IOException {
        IdempotencyValue cachedResponse = keyOperation.get();
        log.info("cached content = {} ", cachedResponse);
        String responseBody;
        Integer status;

        if (cachedResponse.isDone) {
            log.info("cache {} exist, and is done.");
            status = cachedResponse.status;
            responseBody = cachedResponse.cacheValue;
        } else {
            log.info("cache exist, and is still in processing, please retry later");
            status = TOO_EARLY.value();
            ProblemDetail pd = ProblemDetail.forStatus(TOO_EARLY);
            pd.setType(URI.create(request.getRequestURI()));
            pd.setDetail("request is now processing, please try again later");
            responseBody = OBJECT_MAPPER.writeValueAsString(pd);
        }
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        PrintWriter responseWriter = response.getWriter();
        responseWriter.write(responseBody);

        response.flushBuffer();

    }

    private void updateResultInCache(HttpServletRequest request, ContentCachingResponseWrapper responseCopier,
                                     BoundValueOperations<String, IdempotencyValue> keyOperation)
            throws UnsupportedEncodingException {
        if (needCache(responseCopier)) {
            log.info("process result need to be cached");
            String responseBody = new String(responseCopier.getContentAsByteArray(), request.getCharacterEncoding());
            IdempotencyValue result = IdempotencyValue.done(Collections.emptyMap(), responseCopier.getStatus(), responseBody);

            log.info("save {} to redis", result);
            keyOperation.set(result, defaultTtl, TimeUnit.MINUTES);
        } else {
            log.info("process result don't need to be cached");
            redisTemplate.delete(keyOperation.getKey());
        }
    }

    private boolean needCache(ContentCachingResponseWrapper responseCopier) {
        int statusCode = responseCopier.getStatus();
        return statusCode >= 200
                && statusCode <= 300;
    }

    public record IdempotencyValue(Map<String, Object> header, int status, String cacheValue, boolean isDone) {

        protected static IdempotencyValue init() {
            return new IdempotencyValue(Collections.emptyMap(), 0, "", false);
        }

        protected static IdempotencyValue done(Map<String, Object> header, Integer status, String cacheValue) {
            return new IdempotencyValue(header, status, cacheValue, true);
        }

    }

}
