package com.justedlev.taskexec.restclient.auth;

import com.justedlev.taskexec.restclient.auth.configuration.AuthApiClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "auth-api-client", url = "${rest-client.auth}", configuration = AuthApiClientConfiguration.class)
public interface AuthApiClient {
    @PostMapping(value = "/v1/account/sleep")
    Object sleep();

    @PostMapping(value = "/v1/account/offline")
    Object offline();
}
