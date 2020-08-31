package com.fabrick;

import com.fabrick.client.FabrickClient;
import com.fabrick.configurations.FabrickClientConfig;
import com.fabrick.controllers.requests.FabrickPaymentCreditor;
import com.fabrick.controllers.requests.FabrickPaymentCreditorAccount;
import com.fabrick.controllers.requests.FabrickPaymentRequest;
import com.fabrick.controllers.responses.FabrickBalanceResponse;
import com.fabrick.controllers.responses.FabrickPaymentResponse;
import com.fabrick.controllers.responses.FabrickTransactionsResponse;
import java.util.Date;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
public class FabrickClientTest extends GenericTest {

    @Autowired
    FabrickClient fabrickClient;

    @Autowired
    FabrickClientConfig fabrickClientConfig;

    @Test
    public void getBalanceFromFabrickApiOk() {
        Optional<FabrickBalanceResponse> fabrickBalanceResponse = fabrickClient.getBalance(fabrickClientConfig, API_KEY_OK, ACCOUNT_ID_OK);
        assertThat(fabrickBalanceResponse.get().getStatus()).isEqualTo("OK");
    }

    @Test
    public void getBalanceFromFabrickApiWithPayloadOk() {
        Optional<FabrickBalanceResponse> fabrickBalanceResponse = fabrickClient.getBalance(fabrickClientConfig, API_KEY_OK, ACCOUNT_ID_OK);
        assertThat(fabrickBalanceResponse.get().getStatus()).isEqualTo("OK");

        assertThat(fabrickBalanceResponse.get().getPayload()).isNotNull();
        assertThat(fabrickBalanceResponse.get().getPayload().getDate()).isNotNull();
        assertThat(fabrickBalanceResponse.get().getPayload().getBalance()).isNotNull();
        assertThat(fabrickBalanceResponse.get().getPayload().getAvailableBalance()).isNotNull();
        assertThat(fabrickBalanceResponse.get().getPayload().getCurrency()).isNotNull();
    }

    @Test
    public void getBalanceFromFabrickApiKoWithAccountIdNotValid() {
        Optional<FabrickBalanceResponse> fabrickBalanceResponse = fabrickClient.getBalance(fabrickClientConfig, API_KEY_OK, ACCOUNT_ID_KO);
        assertThat(fabrickBalanceResponse.get().getStatus()).isEqualTo("KO");
    }

    @Test
    public void getBalanceFromFabrickApiKoWithApiKeyNotValid() {
        Optional<FabrickBalanceResponse> fabrickBalanceResponse = fabrickClient.getBalance(fabrickClientConfig, API_KEY_KO, ACCOUNT_ID_OK);
        assertThat(fabrickBalanceResponse.get().getStatus()).isEqualTo("KO");
    }

    @Test
    public void getTransactionsFromFabrickApiOk() {
        Date dtFrom = StringToDate("2019-01-01");
        Date dtTo = StringToDate("2019-12-01");

        Optional<FabrickTransactionsResponse> fabrickTransactionsResponse = fabrickClient.getTransactions(fabrickClientConfig, API_KEY_OK, ACCOUNT_ID_OK, dtFrom, dtTo);
        assertThat(fabrickTransactionsResponse.get().getStatus()).isEqualTo("OK");
    }

    @Test
    public void getTransactionsFromFabrickApiKo() {
        Optional<FabrickTransactionsResponse> fabrickTransactionsResponse = fabrickClient.getTransactions(fabrickClientConfig, API_KEY_OK, ACCOUNT_ID_OK, null, null);
        assertThat(fabrickTransactionsResponse.isPresent()).isEqualTo(false);
    }

    @Test
    public void postPaymentFromFabrickApiKo() {
        FabrickPaymentRequest fabrickPaymentRequest = new FabrickPaymentRequest();
        fabrickPaymentRequest.setExecutionDate(new Date());
        fabrickPaymentRequest.setDescription("Descr Test");
        fabrickPaymentRequest.setAmount(12.33);
        fabrickPaymentRequest.setCurrency("EUR");
        fabrickPaymentRequest.setCreditor(new FabrickPaymentCreditor());
        fabrickPaymentRequest.getCreditor().setName("LUCA TERRIBILE");
        fabrickPaymentRequest.getCreditor().setAccount(new FabrickPaymentCreditorAccount());
        fabrickPaymentRequest.getCreditor().getAccount().setAccountCode("IT40L0326822311052923800661");

        Optional<FabrickPaymentResponse> fabrickPaymentResponse = fabrickClient.postPayment(fabrickClientConfig, API_KEY_OK, ACCOUNT_ID_OK, fabrickPaymentRequest);
        assertThat(fabrickPaymentResponse.get().getStatus()).isEqualTo("KO");
    }
}
