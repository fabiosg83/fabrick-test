package com.fabrick.services;

import com.fabrick.controllers.responses.FabrickBalanceResponse;
import com.fabrick.controllers.responses.RestResponse;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author fabio.sgroi
 */
@Service
@Slf4j
public class AccountService extends GenericService {

    public RestResponse manageAccountBalance(String apiKey, Long accountId) {
        log.info("ACCOUNT_SERVICE [{}] - START", accountId);
        RestResponse cResp = new RestResponse();

        Optional<String> opApiKey = cleanToken(apiKey);
        if (!opApiKey.isPresent()) {
            return errorResponse(cResp, 401, "Token not valid");
        }

        log.info("Call Fabrick API [{}] - GetBalance", accountId);
        Optional<FabrickBalanceResponse> fabrickBalanceResponse = fabrickClient.getBalance(fabrickClientConfig, opApiKey.get(), accountId);
        if (fabrickBalanceResponse.isPresent() && fabrickBalanceResponse.get().getStatus().equals("OK")) {
            log.info("Response Fabrick API [{}] - GetBalance [OK]", accountId);
            cResp.setStatusMessage("OK");
            cResp.setData(fabrickBalanceResponse.get().getPayload());
        } else if (fabrickBalanceResponse.isPresent() && fabrickBalanceResponse.get().getStatus().equals("KO")) {
            log.error("Response Fabrick API [{}] - GetBalance [{}]", accountId, fabrickBalanceResponse.get().getErrors().get(0).getDescription());
            cResp.setStatusMessage("KO");
            cResp.setStatusCode(403);
            cResp.setStatusMessage(fabrickBalanceResponse.get().getErrors().get(0).getCode());
            cResp.setData(fabrickBalanceResponse.get().getErrors().get(0).getDescription());
        } else {
            cResp.setStatusMessage("KO");
            cResp.setStatusCode(400);
            cResp.setData("Bad request");
        }

        log.info("ACCOUNT_SERVICE [{}] - FINISH", accountId);
        return cResp;
    }
}
