package com.justedlev.taskexec.executor.handlers;

import com.justedlev.auth.client.AuthFeignClient;
import com.justedlev.auth.enumeration.ModeType;
import com.justedlev.auth.model.request.UpdateAccountModeRequest;
import com.justedlev.taskexec.executor.manager.AbstractTaskExecutorHandler;
import com.justedlev.taskexec.executor.model.TaskContext;
import com.justedlev.taskexec.executor.model.TaskResultResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthSleepModeHandler extends AbstractTaskExecutorHandler {
    private final AuthFeignClient authFeignClient;
    private final ModelMapper defaultMapper;

    @Override
    public TaskResultResponse handle(TaskContext context) {
        var request = defaultMapper.map(context.getPayload(), UpdateAccountModeRequest.class);
        var res = authFeignClient.updateMode(request);

        return TaskResultResponse.builder()
                .taskName(this.getTaskName())
                .message(String.format("Updated %s accounts to mode %s", res.size(), ModeType.SLEEP))
                .build();
    }

    @Override
    public String getTaskName() {
        return "auth-sleep-mode";
    }
}
