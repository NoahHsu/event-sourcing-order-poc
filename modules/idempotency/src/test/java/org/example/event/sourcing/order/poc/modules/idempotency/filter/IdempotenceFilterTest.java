package org.example.event.sourcing.order.poc.modules.idempotency.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class IdempotenceFilterTest {

    public static final String REQUEST_HEADER_RID = "rid";
    public static final String REQUEST_HEADER_CID = "cid";
    private static final String GIVEN_CHARSET = "UTF-8";

    private static final String GIVEN_URI = "/given/uri/path";
    private static final String GIVEN_RID = "givenRid";
    private static final String GIVEN_CID = "givenCid";
    private RedisTemplate<String, IdempotenceFilter.IdempotencyValue> redisTemplate = mock(RedisTemplate.class);

    private IdempotenceFilter sut = new IdempotenceFilter(redisTemplate, 60);

    private MockHttpServletRequest mockRequest;
    private MockHttpServletResponse mockResponse;
    private FilterChain mockFilterChain;

    @BeforeEach
    void setupFilterChain() {
        this.mockFilterChain = mock(FilterChain.class);
    }

    @Test
    void givenPost_ValidHeader_NoCacheExist_Response200_whenDoFilterInternal_thenShouldCacheResponse() throws ServletException, IOException {
        String givenMethod = "POST";
        int givenStatus = 200;
        String givenResponseBody = "{'response-key': 'response-value'}";
        String expectedCacheKey = "POST_/given/uri/path_givenCid_givenRid";
        Integer expectedStatus = 200;

        setupRequest(givenMethod, GIVEN_URI, GIVEN_RID, GIVEN_CID);
        setupResponse(givenStatus, givenResponseBody);

        BoundValueOperations<String, IdempotenceFilter.IdempotencyValue> mockBoundValueOps =
                mock(BoundValueOperations.class);
        given(redisTemplate.boundValueOps(any())).willReturn(mockBoundValueOps);
        given(mockBoundValueOps.setIfAbsent(any(), anyLong(), any())).willReturn(Boolean.TRUE);

        sut.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        then(mockFilterChain).should().doFilter(eq(mockRequest), any());
        then(redisTemplate).should().boundValueOps(expectedCacheKey);
        then(mockBoundValueOps).should().setIfAbsent(assertArg(value -> {
            assertFalse(value.isDone());
        }), anyLong(), any());
        then(mockBoundValueOps).should().set(assertArg(value -> {
            assertEquals(expectedStatus, value.status());
            assertTrue(value.isDone());
        }), anyLong(), any());
    }

    private void setupResponse(int status, String responseBody) throws UnsupportedEncodingException {
        this.mockResponse = new MockHttpServletResponse();
        mockResponse.setStatus(status);
        mockResponse.getWriter().write(responseBody);
    }

    private void setupRequest(String givenMethod, String givenUri, String givenRid, String givenCid) {
        this.mockRequest = new MockHttpServletRequest(givenMethod, givenUri);
        this.mockRequest.setContentType(APPLICATION_JSON_VALUE);
        this.mockRequest.setCharacterEncoding(GIVEN_CHARSET);
        if (givenRid != null)
            this.mockRequest.addHeader(REQUEST_HEADER_RID, givenRid);
        if (givenCid != null)
            this.mockRequest.addHeader(REQUEST_HEADER_CID, givenCid);
    }

    @Test
    void givenPost_ValidHeader_NoCacheExist_Response404_whenDoFilterInternal_thenShouldDeleteCache() throws ServletException, IOException {
        String givenMethod = "POST";
        int givenStatus = 404;
        String givenResponseBody = "{'response-key': 'response-value'}";
        String expectedCacheKey = "POST_/given/uri/path_givenCid_givenRid";

        setupRequest(givenMethod, GIVEN_URI, GIVEN_RID, GIVEN_CID);
        setupResponse(givenStatus, givenResponseBody);

        BoundValueOperations<String, IdempotenceFilter.IdempotencyValue> mockBoundValueOps =
                mock(BoundValueOperations.class);
        given(redisTemplate.boundValueOps(any())).willReturn(mockBoundValueOps);
        given(mockBoundValueOps.setIfAbsent(any(), anyLong(), any())).willReturn(Boolean.TRUE);
        given(mockBoundValueOps.getKey()).willReturn(expectedCacheKey);

        sut.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        then(mockFilterChain).should().doFilter(eq(mockRequest), any());
        then(redisTemplate).should().boundValueOps(expectedCacheKey);
        then(mockBoundValueOps).should().setIfAbsent(assertArg(value -> {
            assertFalse(value.isDone());
        }), anyLong(), any());
        then(redisTemplate).should().delete(expectedCacheKey);
    }

    @Test
    void givenPost_ValidHeader_CacheExistIsProcessing_whenDoFilterInternal_thenShouldReturn425Error() throws ServletException, IOException {
        String givenMethod = "POST";
        setupRequest(givenMethod, GIVEN_URI, GIVEN_RID, GIVEN_CID);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        String expectedCacheKey = "POST_/given/uri/path_givenCid_givenRid";
        Integer expectedStatus = 425;

        BoundValueOperations<String, IdempotenceFilter.IdempotencyValue> mockBoundValueOps =
                mock(BoundValueOperations.class);
        given(redisTemplate.boundValueOps(any())).willReturn(mockBoundValueOps);
        given(mockResponse.getWriter()).willReturn(mock(PrintWriter.class));
        given(mockBoundValueOps.setIfAbsent(any(), anyLong(), any())).willReturn(Boolean.FALSE);
        given(mockBoundValueOps.get()).willReturn(mockInprogressIdempotencyValue());

        sut.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        then(mockFilterChain).should(never()).doFilter(any(), any());
        then(redisTemplate).should().boundValueOps(expectedCacheKey);
        then(mockBoundValueOps).should(never()).set(any(), anyLong(), any());
        then(mockResponse).should().setStatus(expectedStatus);
        then(mockResponse).should().flushBuffer();
    }

    private IdempotenceFilter.IdempotencyValue mockInprogressIdempotencyValue() {
        return IdempotenceFilter.IdempotencyValue.init();
    }

    @Test
    void givenPost_ValidHeader_CacheExistIsDone_whenDoFilterInternal_thenShouldReturnCachedResponse() throws ServletException, IOException {
        String givenMethod = "POST";
        String givenCacheValue = "{'response-key': 'response-value'}";
        String expectedCacheKey = "POST_/given/uri/path_givenCid_givenRid";
        Integer expectedStatus = 200;

        setupRequest(givenMethod, GIVEN_URI, GIVEN_RID, GIVEN_CID);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        BoundValueOperations<String, IdempotenceFilter.IdempotencyValue> mockBoundValueOps =
                mock(BoundValueOperations.class);
        PrintWriter mockPrintWriter = mock(PrintWriter.class);
        given(redisTemplate.boundValueOps(any())).willReturn(mockBoundValueOps);
        given(mockResponse.getWriter()).willReturn(mockPrintWriter);
        given(mockBoundValueOps.setIfAbsent(any(), anyLong(), any())).willReturn(Boolean.FALSE);
        given(mockBoundValueOps.get()).willReturn(mockDoneIdempotencyValue(givenCacheValue));

        sut.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        then(mockFilterChain).should(never()).doFilter(any(), any());
        then(redisTemplate).should().boundValueOps(expectedCacheKey);
        then(mockBoundValueOps).should(never()).set(any(), anyLong(), any());
        then(mockResponse).should().setStatus(expectedStatus);
        then(mockPrintWriter).should().write(givenCacheValue);
        then(mockResponse).should().flushBuffer();
    }

    private IdempotenceFilter.IdempotencyValue mockDoneIdempotencyValue(String cacheValue) {
        return IdempotenceFilter.IdempotencyValue.done(Collections.emptyMap(), 200, cacheValue);
    }

    @Test
    void givenGetMethod_whenDoFilterInternal_thenShouldDoFilterDirectly() throws ServletException, IOException {
        String givenMethod = "GET";
        int givenStatus = 200;
        String givenResponseBody = "{'response-key': 'response-value'}";

        setupRequest(givenMethod, GIVEN_URI, GIVEN_RID, GIVEN_CID);
        setupResponse(givenStatus, givenResponseBody);

        sut.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        then(mockFilterChain).should().doFilter(mockRequest, mockResponse);
        then(redisTemplate).shouldHaveNoInteractions();
    }

    private static Stream<Arguments> ridCidArguments() {
        return Stream.of(
                // Arguments.of(rid, cid)
                Arguments.of(null, null), // both null
                Arguments.of(" ", " "), // both blank
                Arguments.of(" ", "cid"), // rid is blank
                Arguments.of("rid", "") // cid is blank

        );
    }

    @ParameterizedTest
    @MethodSource("ridCidArguments")
    void givenPost_WithoutRidOrSid_whenDoFilterInternal_thenShouldDoFilterDirectly(String givenRid, String givenCid) throws ServletException, IOException {
        String givenMethod = "POST";
        setupRequest(givenMethod, GIVEN_URI, givenRid, givenCid);
        setupResponse(200, "{'response-key': 'response-value'}");

        sut.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        then(mockFilterChain).should().doFilter(mockRequest, mockResponse);
        then(redisTemplate).shouldHaveNoInteractions();
    }

}
