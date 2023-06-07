package com.justedlev.taskexec.executor.handlers;

import com.justedlev.taskexec.api.JAccountFeignClient;
import com.justedlev.taskexec.executor.manager.AbstractTaskHandler;
import com.justedlevhub.api.model.request.UpdateAccountModeRequest;
import com.justedlevhub.api.model.response.TaskResultResponse;
import com.justedlevhub.api.type.ModeType;
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
