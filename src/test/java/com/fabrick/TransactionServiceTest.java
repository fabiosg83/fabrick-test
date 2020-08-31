package com.fabrick;

import com.fabrick.controllers.responses.FabrickTransaction;
import com.fabrick.controllers.responses.RestResponse;
import com.fabrick.services.TransactionService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class TransactionServiceTest extends GenericTest {

    @Autowired
    TransactionService transactionService;

    @Test
    public void transactionServiceIsNotNull() {
        assertThat(transactionService).isNotNull();
    }

    @Test
    public void getTransactionFromTransactionServiceOK() {
        String token = String.format("Bearer %s", API_KEY_OK);

        Date dtFrom = StringToDate("2019-01-01");
        Date dtTo = StringToDate("2019-12-01");

        RestResponse cResp = transactionService.manageTransactions(token, ACCOUNT_ID_OK, dtFrom, dtTo);

        assertThat(cResp.getStatusCode()).isEqualTo(200);
        assertThat(cResp.getStatusMessage()).isEqualTo("OK");
        assertThat(cResp.getData()).isNotNull();
        assertThat((cResp.getData())).isInstanceOf(ArrayList.class);
    }

    @Test
    public void getTransactionFromTransactionServiceKO() {
        String token = String.format("Bearer %s", API_KEY_OK);

        RestResponse cResp = transactionService.manageTransactions(token, ACCOUNT_ID_OK, null, null);

        assertThat(cResp.getStatusCode()).isEqualTo(400);
        assertThat(cResp.getStatusMessage()).isEqualTo("KO");
        assertThat(cResp.getData()).isNotNull();
    }

    @Test
    public void getTransactionFromTransactionServiceWithTokenNotValid() {
        String token = String.format("Bearer %s", API_KEY_KO);

        Date dtFrom = StringToDate("2019-01-01");
        Date dtTo = StringToDate("2019-12-01");

        RestResponse cResp = transactionService.manageTransactions(token, ACCOUNT_ID_OK, dtFrom, dtTo);

        assertThat(cResp.getStatusCode()).isEqualTo(403);
        assertThat(cResp.getStatusMessage()).isEqualTo("GTW003");
        assertThat(cResp.getData()).isNotNull();
    }

    @Test
    public void getTransactionFromTransactionServiceWithAccountIdNotValid() {
        String token = String.format("Bearer %s", API_KEY_OK);

        Date dtFrom = StringToDate("2019-01-01");
        Date dtTo = StringToDate("2019-12-01");

        RestResponse cResp = transactionService.manageTransactions(token, ACCOUNT_ID_KO, dtFrom, dtTo);

        assertThat(cResp.getStatusCode()).isEqualTo(403);
        assertThat(cResp.getStatusMessage()).isEqualTo("REQ004");
        assertThat(cResp.getData()).isNotNull();
    }
}
