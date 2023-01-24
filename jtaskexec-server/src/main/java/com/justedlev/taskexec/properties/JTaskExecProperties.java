package com.justedlev.taskexec.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@Data
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "jtask-exec")
public class JTaskExecProperties {
    private Boolean registerTasks;
    private Boolean restoreTasks;
}
