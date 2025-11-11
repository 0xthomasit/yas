package com.yas.product.service;

import com.yas.commonlibrary.config.ServiceUrlConfig;
import com.yas.product.viewmodel.NoFileMediaVm;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import java.net.URI;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

/* Circuit Breaker Pattern (Resilience) */
@Service
@RequiredArgsConstructor
public class MediaService extends AbstractCircuitBreakFallbackHandler {

    private final RestClient restClient;
    private final ServiceUrlConfig serviceUrlConfig;

    // 🔄 RETRY: Try up to 3 times if request fails
    @Retry(name = "restApi")
    // 🛡️ CIRCUIT BREAKER: Stop making requests if service is down
    // - If 50% of requests fail, circuit opens
    // - After opening, immediately return fallback without calling service
    // - Periodically test if service is back up
    @CircuitBreaker(name = "restCircuitBreaker", fallbackMethod = "handleMediaFallback")
    public NoFileMediaVm getMedia(Long id) {
        // 🎯 CALL MEDIA MICROSERVICE
        // Problem: What if Media service is down or slow?
        // Solution: Circuit breaker prevents cascading failures
        if (id == null) {
            //TODO return default no image url
            return new NoFileMediaVm(null, "", "", "", "");
        }
        final URI url = UriComponentsBuilder
                .fromHttpUrl(serviceUrlConfig.media())
                .path("/medias/{id}")
                .buildAndExpand(id)
                .toUri();

        return restClient.get()
                .uri(url)
                .retrieve()
                .body(NoFileMediaVm.class);
    }

    @Retry(name = "restApi")
    @CircuitBreaker(name = "restCircuitBreaker", fallbackMethod = "handleMediaFallback")
    public NoFileMediaVm saveFile(MultipartFile multipartFile, String caption, String fileNameOverride) {
        final URI url = UriComponentsBuilder.fromHttpUrl(serviceUrlConfig.media()).path("/medias").build().toUri();
        final String jwt = ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getTokenValue();

        final MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("multipartFile", multipartFile.getResource());
        builder.part("caption", caption);
        builder.part("fileNameOverride", fileNameOverride);

        return restClient.post()
                .uri(url)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .headers(h -> h.setBearerAuth(jwt))
                .body(builder.build())
                .retrieve()
                .body(NoFileMediaVm.class);
    }

    @Retry(name = "restApi")
    @CircuitBreaker(name = "restCircuitBreaker", fallbackMethod = "handleBodilessFallback")
    public void removeMedia(Long id) {
        final URI url = UriComponentsBuilder.fromHttpUrl(serviceUrlConfig.media()).path("/medias/{id}")
                .buildAndExpand(id).toUri();
        final String jwt = ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getTokenValue();
        restClient.delete()
                .uri(url)
                .headers(h -> h.setBearerAuth(jwt))
                .retrieve()
                .body(Void.class);
    }

    // ⚠️ FALLBACK: Called when circuit is open or request fails
    private NoFileMediaVm handleMediaFallback(Throwable throwable) throws Throwable {
        // Log error and rethrow
        // In production, you might return a default image instead
        return handleTypedFallback(throwable);
    }
}
