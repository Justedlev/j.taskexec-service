package com.justedlev.taskexec.configuration;

import com.justedlev.taskexec.properties.JTaskExecProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        JTaskExecProperties.class
})
public class PropertiesConfiguration {
}
