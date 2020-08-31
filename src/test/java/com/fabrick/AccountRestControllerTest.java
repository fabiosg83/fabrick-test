package com.fabrick;

import com.fabrick.controllers.requests.FabrickPaymentCreditor;
import com.fabrick.controllers.requests.FabrickPaymentCreditorAccount;
import com.fabrick.controllers.requests.FabrickPaymentRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author fabio.sgroi
 */
@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureMockMvc
public class AccountRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    final String TOKEN_OK = "FXOVVXXHVCPVPBZXIJOBGUGSKHDNFRRQJP";
    final Long ACCOUNT_ID_OK = 14537780L;

    final String TOKEN_KO = "TOKEN-NOT-VALID";
    final Long ACCOUNT_ID_KO = 14537781L;

    final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Test
    public void getBalanceFromAccountRestControllerOK() throws Exception {

        String path = String.format("/v1/accounts/%d/balance", ACCOUNT_ID_OK);
        String token = String.format("Bearer %s", TOKEN_OK);

        this.mockMvc.perform(get(path).header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode", is(200)))
                .andExpect(jsonPath("$.statusMessage", is("OK")))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.balance").exists());
    }

    @Test
    public void getBalanceFromAccountRestControllerWithTokenNotValid() throws Exception {

        String path = String.format("/v1/accounts/%d/balance", ACCOUNT_ID_OK);
        String token = String.format("Bearer %s", TOKEN_KO);

        this.mockMvc.perform(get(path).header("Authorization", token))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.statusCode", is(403)))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data", is("Api key not found")));
    }

    @Test
    public void getBalanceFromAccountRestControllerWithAccountIdNotValid() throws Exception {

        String path = String.format("/v1/accounts/%d/balance", ACCOUNT_ID_KO);
        String token = String.format("Bearer %s", TOKEN_OK);

        this.mockMvc.perform(get(path).header("Authorization", token))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.statusCode", is(403)))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data", is("Invalid account identifier")));
    }

    @Test
    public void postPaymentFromAccountRestControllerKO() throws Exception {

        String path = String.format("/v1/accounts/%d/payments/money-transfers", ACCOUNT_ID_OK);
        String token = String.format("Bearer %s", TOKEN_OK);

        FabrickPaymentRequest fabrickPaymentRequest = new FabrickPaymentRequest();
        fabrickPaymentRequest.setExecutionDate(new Date());
        fabrickPaymentRequest.setDescription("Descr Test");
        fabrickPaymentRequest.setAmount(12.33);
        fabrickPaymentRequest.setCurrency("EUR");
        fabrickPaymentRequest.setCreditor(new FabrickPaymentCreditor());
        fabrickPaymentRequest.getCreditor().setName("LUCA TERRIBILE");
        fabrickPaymentRequest.getCreditor().setAccount(new FabrickPaymentCreditorAccount());
        fabrickPaymentRequest.getCreditor().getAccount().setAccountCode("IT40L0326822311052923800661");

        this.mockMvc.perform(post(path).content(gson.toJson(fabrickPaymentRequest)).header("Authorization", token).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode", is(400)));
    }

    @Test
    public void getTransactionsFromAccountRestControllerOK() throws Exception {

        String path = String.format("/v1/accounts/%d/transactions?fromAccountingDate=2019-01-01&toAccountingDate=2019-12-01", ACCOUNT_ID_OK);
        String token = String.format("Bearer %s", TOKEN_OK);

        this.mockMvc.perform(get(path).header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode", is(200)))
                .andExpect(jsonPath("$.statusMessage", is("OK")))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isNotEmpty());
    }
}
