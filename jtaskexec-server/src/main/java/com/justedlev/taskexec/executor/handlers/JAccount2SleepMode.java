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
public class JAccount2SleepMode extends AbstractTaskHandler<UpdateAccountModeRequest> {
    private final JAccountFeignClient accountFeignClient;
    private final UpdateAccountModeRequest payload = payload();

    @Override
    public TaskResultResponse handle() {
        var res = accountFeignClient.updateMode(payload);

        return TaskResultResponse.builder()
                .taskName(this.taskName())
                .message(String.format("Updated %s accounts to mode '%s'", res.size(), payload.getToMode()))
                .build();
    }

    @Override
    public UpdateAccountModeRequest payload() {
        return UpdateAccountModeRequest.builder()
                .toMode(ModeType.SLEEP)
                .fromModes(List.of(ModeType.ONLINE, ModeType.HIDDEN))
                .build();
    }
}
