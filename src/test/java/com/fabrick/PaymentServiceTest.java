package com.fabrick;

import com.fabrick.controllers.requests.FabrickPaymentCreditor;
import com.fabrick.controllers.requests.FabrickPaymentCreditorAccount;
import com.fabrick.controllers.requests.FabrickPaymentRequest;
import com.fabrick.controllers.responses.RestResponse;
import com.fabrick.services.PaymentService;
import java.util.Date;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 *
 * @author fabio.sgroi
 */
@SpringBootTest
@ActiveProfiles("dev")
public class PaymentServiceTest extends GenericTest {

    @Autowired
    PaymentService paymentService;

    @Test
    public void paymentServiceIsNotNull() {
        assertThat(paymentService).isNotNull();
    }

    @Test
    public void postPaymentFromPaymentServiceKO() {
        String token = String.format("Bearer %s", API_KEY_OK);

        FabrickPaymentRequest fabrickPaymentRequest = new FabrickPaymentRequest();
        fabrickPaymentRequest.setExecutionDate(new Date());
        fabrickPaymentRequest.setDescription("Descr Test");
        fabrickPaymentRequest.setAmount(12.33);
        fabrickPaymentRequest.setCurrency("EUR");
        fabrickPaymentRequest.setCreditor(new FabrickPaymentCreditor());
        fabrickPaymentRequest.getCreditor().setName("LUCA TERRIBILE");
        fabrickPaymentRequest.getCreditor().setAccount(new FabrickPaymentCreditorAccount());
        fabrickPaymentRequest.getCreditor().getAccount().setAccountCode("IT40L0326822311052923800661");

        RestResponse cResp = paymentService.managePayment(token, ACCOUNT_ID_OK, fabrickPaymentRequest);

        assertThat(cResp.getStatusCode()).isEqualTo(400);
        assertThat(cResp.getStatusMessage()).isEqualTo("API000");
        assertThat(cResp.getData()).isNotNull();
    }

    @Test
    public void postPaymentFromPaymentServiceWithTokenNotValid() {
        String token = String.format("Bearer %s", API_KEY_KO);

        FabrickPaymentRequest fabrickPaymentRequest = new FabrickPaymentRequest();
        fabrickPaymentRequest.setExecutionDate(new Date());
        fabrickPaymentRequest.setDescription("Descr Test");
        fabrickPaymentRequest.setAmount(12.33);
        fabrickPaymentRequest.setCurrency("EUR");
        fabrickPaymentRequest.setCreditor(new FabrickPaymentCreditor());
        fabrickPaymentRequest.getCreditor().setName("LUCA TERRIBILE");
        fabrickPaymentRequest.getCreditor().setAccount(new FabrickPaymentCreditorAccount());
        fabrickPaymentRequest.getCreditor().getAccount().setAccountCode("IT40L0326822311052923800661");

        RestResponse cResp = paymentService.managePayment(token, ACCOUNT_ID_OK, fabrickPaymentRequest);

        assertThat(cResp.getStatusCode()).isEqualTo(400);
        assertThat(cResp.getStatusMessage()).isEqualTo("GTW003");
        assertThat(cResp.getData()).isNotNull();
    }

    @Test
    public void postPaymentFromPaymentServiceWithAccountIdNotValid() {
        String token = String.format("Bearer %s", API_KEY_OK);

        FabrickPaymentRequest fabrickPaymentRequest = new FabrickPaymentRequest();
        fabrickPaymentRequest.setExecutionDate(new Date());
        fabrickPaymentRequest.setDescription("Descr Test");
        fabrickPaymentRequest.setAmount(12.33);
        fabrickPaymentRequest.setCurrency("EUR");
        fabrickPaymentRequest.setCreditor(new FabrickPaymentCreditor());
        fabrickPaymentRequest.getCreditor().setName("LUCA TERRIBILE");
        fabrickPaymentRequest.getCreditor().setAccount(new FabrickPaymentCreditorAccount());
        fabrickPaymentRequest.getCreditor().getAccount().setAccountCode("IT40L0326822311052923800661");

        RestResponse cResp = paymentService.managePayment(token, ACCOUNT_ID_KO, fabrickPaymentRequest);

        assertThat(cResp.getStatusCode()).isEqualTo(400);
        assertThat(cResp.getStatusMessage()).isEqualTo("REQ004");
        assertThat(cResp.getData()).isNotNull();
    }
}
