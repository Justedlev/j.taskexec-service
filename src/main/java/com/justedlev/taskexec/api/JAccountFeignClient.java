package com.justedlev.taskexec.api;

import com.justedlev.taskexec.api.configuration.JAccountFeignClientConfiguration;
import com.justedlevhub.api.model.request.AccountFilterRequest;
import com.justedlevhub.api.model.request.CreateAccountRequest;
import com.justedlevhub.api.model.request.UpdateAccountModeRequest;
import com.justedlevhub.api.model.request.UpdateAccountRequest;
import com.justedlevhub.api.model.response.AccountResponse;
import com.justedlevhub.api.model.response.PageResponse;
import com.justedlevhub.api.model.response.ReportResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(
        name = "jaccount-api-client",
        url = "${jaccount.client.url}",
        configuration = JAccountFeignClientConfiguration.class
)
public interface JAccountFeignClient {
    @PostMapping(value = AccountV1Endpoints.V1_ACCOUNT_PAGE)
    PageResponse<AccountResponse> findPage(@RequestBody AccountFilterRequest request);

    @PostMapping(value = AccountV1Endpoints.V1_ACCOUNT_CREATE)
    @ResponseStatus(HttpStatus.CREATED)
    AccountResponse create(@RequestBody CreateAccountRequest request);

    @GetMapping(value = AccountV1Endpoints.V1_ACCOUNT_NICKNAME)
    AccountResponse getByNickname(@PathVariable String nickname);

    @GetMapping(value = AccountV1Endpoints.V1_ACCOUNT_CONFIRM_CODE)
    ReportResponse confirm(@PathVariable String code);

    @PutMapping(value = AccountV1Endpoints.V1_ACCOUNT_NICKNAME_UPDATE)
    AccountResponse update(@RequestBody UpdateAccountRequest request);

    @PostMapping(
            value = AccountV1Endpoints.V1_ACCOUNT_NICKNAME_UPDATE_AVATAR,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    AccountResponse updateAvatar(@PathVariable String nickname, @RequestPart MultipartFile file);

    @PostMapping(value = AccountV1Endpoints.V1_ACCOUNT_UPDATE_MODE)
    List<AccountResponse> updateMode(@RequestBody UpdateAccountModeRequest request);
}
