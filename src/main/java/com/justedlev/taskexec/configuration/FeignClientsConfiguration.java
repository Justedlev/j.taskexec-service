package com.justedlev.taskexec.configuration;

import com.justedlev.taskexec.api.JAccountFeignClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = {
        JAccountFeignClient.class
})
public class FeignClientsConfiguration {
}
