package com.fabrick;

import com.fabrick.controllers.responses.FabrickBalancePayload;
import com.fabrick.controllers.responses.RestResponse;
import com.fabrick.services.AccountService;
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
public class AccountServiceTest extends GenericTest {

    @Autowired
    AccountService accountService;

    @Test
    public void accountServiceIsNotNull() {
        assertThat(accountService).isNotNull();
    }

    @Test
    public void getBalanceFromAccountServiceOK() {
        String token = String.format("Bearer %s", API_KEY_OK);

        RestResponse cResp = accountService.manageAccountBalance(token, ACCOUNT_ID_OK);

        assertThat(cResp.getStatusCode()).isEqualTo(200);
        assertThat(cResp.getStatusMessage()).isEqualTo("OK");
        assertThat(cResp.getData()).isNotNull();
        assertThat(((FabrickBalancePayload) cResp.getData()).getBalance()).isNotNull();
        assertThat(((FabrickBalancePayload) cResp.getData()).getAvailableBalance()).isNotNull();
        assertThat(((FabrickBalancePayload) cResp.getData()).getCurrency()).isNotNull();
    }

    @Test
    public void getBalanceFromAccountServiceWithTokenNotValid() {
        String token = String.format("Bearer %s", API_KEY_KO);

        RestResponse cResp = accountService.manageAccountBalance(token, ACCOUNT_ID_OK);

        assertThat(cResp.getStatusCode()).isEqualTo(403);
        assertThat(cResp.getStatusMessage()).isEqualTo("GTW003");
        assertThat(cResp.getData()).isNotNull();
    }

    @Test
    public void getBalanceFromAccountServiceWithAccountIdNotValid() {
        String token = String.format("Bearer %s", API_KEY_OK);

        RestResponse cResp = accountService.manageAccountBalance(token, ACCOUNT_ID_KO);

        assertThat(cResp.getStatusCode()).isEqualTo(403);
        assertThat(cResp.getStatusMessage()).isEqualTo("REQ004");
        assertThat(cResp.getData()).isNotNull();
    }

}
