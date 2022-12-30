package com.justedlev.taskexec.executor.handlers;

import com.justedlev.account.client.JAccountFeignClient;
import com.justedlev.account.enumeration.ModeType;
import com.justedlev.account.model.request.UpdateAccountModeRequest;
import com.justedlev.taskexec.executor.manager.AbstractTaskHandler;
import com.justedlev.taskexec.model.response.TaskResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AccountOfflineModeHandler extends AbstractTaskHandler<UpdateAccountModeRequest> {
    private final JAccountFeignClient accountFeignClient;

    @Override
    public TaskResultResponse handle() {
        var res = accountFeignClient.updateMode(payload());

        return TaskResultResponse.builder()
                .taskName(this.taskName())
                .message(String.format("Updated %s accounts to mode %s", res.size(), payload().getToMode()))
                .build();
    }

    @Override
    public String taskName() {
        return "auth-offline-mode";
    }

    @Override
    public UpdateAccountModeRequest payload() {
        return UpdateAccountModeRequest.builder()
                .toMode(ModeType.OFFLINE)
                .fromModes(List.of(ModeType.ONLINE, ModeType.HIDDEN, ModeType.SLEEP))
                .build();
    }
}
