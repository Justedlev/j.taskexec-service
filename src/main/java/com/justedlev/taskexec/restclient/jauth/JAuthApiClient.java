package com.justedlev.taskexec.restclient.jauth;

import com.justedlev.taskexec.restclient.jauth.configuration.JAuthApiClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "JAuthApiClient", url = "${rest-client.jauth}", configuration = JAuthApiClientConfiguration.class)
public interface JAuthApiClient {
}
