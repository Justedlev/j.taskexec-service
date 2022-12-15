package com.justedlev.taskexec;

import com.justedlev.auth.client.AuthFeignClient;
import com.justedlev.taskexec.properties.TaskExecProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients(clients = AuthFeignClient.class)
@EnableConfigurationProperties({
        TaskExecProperties.class
})
public class TaskExecutorApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskExecutorApplication.class, args);
    }

}
