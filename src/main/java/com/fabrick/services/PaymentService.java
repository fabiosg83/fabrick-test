package com.fabrick.services;

import com.fabrick.controllers.requests.FabrickPaymentRequest;
import com.fabrick.controllers.responses.FabrickPaymentResponse;
import com.fabrick.controllers.responses.RestResponse;
import com.fabrick.dao.PaymentImpl;
import com.fabrick.entity.Payments;
import com.fabrick.utility.enumeration.PaymentStatus;
import java.util.Date;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author fabio.sgroi
 */
@Service
@Slf4j
public class PaymentService extends GenericService {

    @Autowired
    PaymentImpl paymentImpl;

    public RestResponse managePayment(String apiKey, Long accountId, FabrickPaymentRequest fabrickPaymentRequest) {
        log.info("PAYMENT_SERVICE [{}] - START", accountId);
        RestResponse cResp = new RestResponse();

        Optional<String> opApiKey = cleanToken(apiKey);
        if (!opApiKey.isPresent()) {
            return errorResponse(cResp, 401, "Token not valid");
        }

        Payments payment = new Payments();
        payment.setAccountId(accountId);
        payment.setCreditorName(fabrickPaymentRequest.getCreditor().getName());
        payment.setCreditorAccountCode(fabrickPaymentRequest.getCreditor().getAccount().getAccountCode());
        payment.setDescription(fabrickPaymentRequest.getDescription());
        payment.setAmount(fabrickPaymentRequest.getAmount());
        payment.setCurrency(fabrickPaymentRequest.getCurrency());
        payment.setStatus(PaymentStatus.TO_PROCESS.getCode());
        payment.setDtExecution((fabrickPaymentRequest.getExecutionDate() != null) ? fabrickPaymentRequest.getExecutionDate() : new Date());

        log.info("Save Payment for AccountId [{}]", accountId);
        if (paymentImpl.saveOrUpdateObject(payment) < 0) {
            return errorResponse(cResp, HttpStatus.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
        }

        log.info("Call Fabrick API [{}] - PostPayment", accountId);
        Optional<FabrickPaymentResponse> fabrickPaymentResponse = fabrickClient.postPayment(fabrickClientConfig, opApiKey.get(), accountId, fabrickPaymentRequest);
        if (fabrickPaymentResponse.isPresent() && fabrickPaymentResponse.get().getStatus().equals("OK")) {
            log.info("Response Fabrick API [{}] - PostPayment [OK]", accountId);
            cResp.setStatusMessage("OK");
            cResp.setData(fabrickPaymentResponse.get().getPayload());
            payment.setStatus(PaymentStatus.PAYMENT_OK.getCode());
        } else if (fabrickPaymentResponse.isPresent() && fabrickPaymentResponse.get().getStatus().equals("KO")) {
            log.error("Response Fabrick API [{}] - PostPayment [{}]", accountId, fabrickPaymentResponse.get().getErrors().get(0).getDescription());
            cResp.setStatusCode(400);
            cResp.setStatusMessage("KO");
            cResp.setStatusMessage(fabrickPaymentResponse.get().getErrors().get(0).getCode());
            cResp.setData(fabrickPaymentResponse.get().getErrors().get(0).getDescription());
            payment.setStatus(PaymentStatus.PAYMENT_KO.getCode());
        } else {
            cResp.setStatusMessage("KO");
            cResp.setStatusCode(400);
            cResp.setData("Bad request");
        }

        if (paymentImpl.saveOrUpdateObject(payment) < 0) {
            return errorResponse(cResp, HttpStatus.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
        }

        log.info("PAYMENT_SERVICE [{}] - FINISH", accountId);
        return cResp;
    }
}
