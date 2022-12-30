package com.justedlev.taskexec;

import com.justedlev.account.client.JAccountFeignClient;
import com.justedlev.taskexec.properties.JTaskExecProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableDiscoveryClient
@EnableFeignClients(clients = JAccountFeignClient.class)
@EnableConfigurationProperties({
        JTaskExecProperties.class
})
public class JTaskExecutorApplication {

    public static void main(String[] args) {
        SpringApplication.run(JTaskExecutorApplication.class, args);
    }

}
