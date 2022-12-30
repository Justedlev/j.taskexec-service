package com.justedlev.taskexec.executor.handlers;

import com.justedlev.account.client.JAccountFeignClient;
import com.justedlev.account.model.request.UpdateAccountModeRequest;
import com.justedlev.taskexec.executor.manager.AbstractTaskExecutorHandler;
import com.justedlev.taskexec.executor.model.TaskContext;
import com.justedlev.taskexec.model.response.TaskResultResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountOfflineModeHandler extends AbstractTaskExecutorHandler {
    private final JAccountFeignClient accountFeignClient;
    private final ModelMapper defaultMapper;

    @Override
    public TaskResultResponse handle(TaskContext context) {
        var request = defaultMapper.map(context.getPayload(), UpdateAccountModeRequest.class);
        var res = accountFeignClient.updateMode(request);

        return TaskResultResponse.builder()
                .taskName(this.getTaskName())
                .message(String.format("Updated %s accounts to mode %s", res.size(), request.getToMode()))
                .build();
    }

    @Override
    public String getTaskName() {
        return "auth-offline-mode";
    }
}
