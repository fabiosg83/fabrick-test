package com.fabrick.services;

import com.fabrick.controllers.responses.FabrickTransactionsResponse;
import com.fabrick.controllers.responses.RestResponse;
import java.util.Date;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author fabio.sgroi
 */
@Service
@Slf4j
public class TransactionService extends GenericService {

    public RestResponse manageTransactions(String apiKey, Long accountId, Date fromDate, Date toDate) {
        log.info("TRANSACTION_SERVICE [{}] - START", accountId);
        RestResponse cResp = new RestResponse();

        Optional<String> opApiKey = cleanToken(apiKey);
        if (!opApiKey.isPresent()) {
            return errorResponse(cResp, 401, "Token not valid");
        }

        log.info("Call Fabrick API [{}] - GetTransactions", accountId);
        Optional<FabrickTransactionsResponse> fabrickTransactionsResponse = fabrickClient.getTransactions(fabrickClientConfig, opApiKey.get(), accountId, fromDate, toDate);
        if (fabrickTransactionsResponse.isPresent() && fabrickTransactionsResponse.get().getStatus().equals("OK")) {
            log.info("Response Fabrick API [{}] - GetTransactions [OK]", accountId);
            cResp.setStatusMessage("OK");
            cResp.setData(fabrickTransactionsResponse.get().getPayload().getList());
        } else if (fabrickTransactionsResponse.isPresent() && fabrickTransactionsResponse.get().getStatus().equals("KO")) {
            log.error("Response Fabrick API [{}] - GetTransactions [{}]", accountId, fabrickTransactionsResponse.get().getErrors().get(0).getDescription());
            cResp.setStatusCode(403);
            cResp.setStatusMessage(fabrickTransactionsResponse.get().getErrors().get(0).getCode());
            cResp.setData(fabrickTransactionsResponse.get().getErrors().get(0).getDescription());
        } else {
            cResp.setStatusMessage("KO");
            cResp.setStatusCode(400);
            cResp.setData("Bad request");
        }

        log.info("TRANSACTION_SERVICE [{}] - FINISH", accountId);
        return cResp;
    }
}
