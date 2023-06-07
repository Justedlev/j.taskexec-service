package com.justedlev.taskexec.api.configuration;

import feign.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@EnableConfigurationProperties(JAccountFeignClientProperties.class)
public class JAccountFeignClientConfiguration {
    private final JAccountFeignClientProperties properties;

    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(
                properties.getConnectTimeout().toMillis(),
                TimeUnit.MILLISECONDS,
                properties.getReadTimeout().toMillis(),
                TimeUnit.MILLISECONDS,
                Boolean.FALSE
        );
    }
}
