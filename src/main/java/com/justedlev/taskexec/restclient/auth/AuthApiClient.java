package com.justedlev.taskexec.restclient.auth;

import com.justedlev.taskexec.restclient.auth.configuration.AuthApiClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "AuthApiClient", url = "${rest-client.auth}", configuration = AuthApiClientConfiguration.class)
public interface AuthApiClient {
}
