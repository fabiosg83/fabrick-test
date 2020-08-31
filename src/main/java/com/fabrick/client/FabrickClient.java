package com.fabrick.client;

import com.fabrick.beans.HttpConnectionPoolBean;
import com.fabrick.configurations.FabrickClientConfig;
import com.fabrick.configurations.HttpConnectionPoolConfig;
import com.fabrick.controllers.requests.FabrickPaymentRequest;
import com.fabrick.controllers.responses.FabrickBalanceResponse;
import com.fabrick.controllers.responses.FabrickPaymentResponse;
import com.fabrick.controllers.responses.FabrickTransactionsResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author fabio.sgroi
 */
@Service
@Slf4j
public class FabrickClient {

    @Autowired
    HttpConnectionPoolConfig httpConnectionPoolConfig;

    @Autowired
    HttpConnectionPoolBean httpConnectionPoolManager;

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    CloseableHttpClient clientPool = null;

    static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    /**
     *
     * @param clientPool
     */
    public void setClientPool(CloseableHttpClient clientPool) {
        this.clientPool = clientPool;
    }

    /**
     *
     * @param fabrickClientConfig
     * @param apiKey
     * @param accountId
     * @return Optional<FabrickBalanceResponse>
     */
    public Optional<FabrickBalanceResponse> getBalance(FabrickClientConfig fabrickClientConfig, String apiKey, Long accountId) {

        Optional<FabrickBalanceResponse> fabrickBalanceResponse = Optional.empty();
        String url2call = fabrickClientConfig.getBaseUrl() + fabrickClientConfig.getUriBalance().replace("{accountId}", accountId.toString());
        HttpGet request = new HttpGet(url2call);

        RequestConfig.Builder builder = RequestConfig.custom().setConnectTimeout(fabrickClientConfig.getTimeout()).setConnectionRequestTimeout(fabrickClientConfig.getTimeout()).setSocketTimeout(fabrickClientConfig.getTimeout());

        RequestConfig config = builder.build();
        request.setConfig(config);

        request.addHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"));
        request.addHeader(new BasicHeader("Auth-Schema", "S2S"));
        request.addHeader(new BasicHeader("apiKey", apiKey));

        try (CloseableHttpClient httpclient = HttpClients.createDefault();
                CloseableHttpResponse response = httpclient.execute(request);) {

            String jsonResponse = EntityUtils.toString(response.getEntity());
            log.info("[GET] jsonResponse [{}]", jsonResponse);
            fabrickBalanceResponse = Optional.of(gson.fromJson(jsonResponse, FabrickBalanceResponse.class));
        } catch (Exception e) {
            log.error("Error: ", e);
        }

        return fabrickBalanceResponse;
    }

    /**
     *
     * @param fabrickClientConfig
     * @param apiKey
     * @param accountId
     * @param fabrickPaymentRequest
     * @return Optional<FabrickPaymentResponse>
     */
    public Optional<FabrickPaymentResponse> postPayment(FabrickClientConfig fabrickClientConfig, String apiKey, Long accountId, FabrickPaymentRequest fabrickPaymentRequest) {

        Optional<FabrickPaymentResponse> fabrickPaymentResponse = Optional.empty();
        String url2call = fabrickClientConfig.getBaseUrl() + fabrickClientConfig.getUriPayments().replace("{accountId}", accountId.toString());

        HttpPost request = new HttpPost(url2call);

        RequestConfig.Builder builder = RequestConfig.custom().setConnectTimeout(fabrickClientConfig.getTimeout()).setConnectionRequestTimeout(fabrickClientConfig.getTimeout()).setSocketTimeout(fabrickClientConfig.getTimeout());

        RequestConfig config = builder.build();
        request.setConfig(config);

        request.addHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"));
        request.addHeader(new BasicHeader("Auth-Schema", "S2S"));
        request.addHeader(new BasicHeader("apiKey", apiKey));

        StringEntity params = new StringEntity(gson.toJson(fabrickPaymentRequest), "UTF-8");
        request.setEntity(params);

        try (CloseableHttpClient defaultClient = (clientPool == null) ? HttpClients.createDefault() : null;
                CloseableHttpResponse response = (defaultClient == null) ? clientPool.execute(request) : defaultClient.execute(request)) {

            String jsonResponse = EntityUtils.toString(response.getEntity());
            log.info("[POST] jsonResponse [{}]", jsonResponse);

            fabrickPaymentResponse = Optional.of(gson.fromJson(jsonResponse, FabrickPaymentResponse.class));
        } catch (Exception e) {
            log.error("Error: ", e);
        }
        return fabrickPaymentResponse;
    }

    /**
     *
     * @param fabrickClientConfig
     * @param apiKey
     * @param accountId
     * @param fromDate
     * @param toDate
     * @return Optional<FabrickTransactionsResponse>
     */
    public Optional<FabrickTransactionsResponse> getTransactions(FabrickClientConfig fabrickClientConfig, String apiKey, Long accountId, Date fromDate, Date toDate) {

        Optional<FabrickTransactionsResponse> fabrickTransactionsResponse = Optional.empty();

        if (fromDate == null || toDate == null) {
            return fabrickTransactionsResponse;
        }

        String url2call = fabrickClientConfig.getBaseUrl() + fabrickClientConfig.getUriTransactions().replace("{accountId}", accountId.toString());
        url2call += String.format("?fromAccountingDate=%s&toAccountingDate=%s", formatter.format(fromDate), formatter.format(toDate));

        HttpGet request = new HttpGet(url2call);

        RequestConfig.Builder builder = RequestConfig.custom().setConnectTimeout(fabrickClientConfig.getTimeout()).setConnectionRequestTimeout(fabrickClientConfig.getTimeout()).setSocketTimeout(fabrickClientConfig.getTimeout());

        RequestConfig config = builder.build();
        request.setConfig(config);

        request.addHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"));
        request.addHeader(new BasicHeader("Auth-Schema", "S2S"));
        request.addHeader(new BasicHeader("apiKey", apiKey));

        try (CloseableHttpClient httpclient = HttpClients.createDefault();
                CloseableHttpResponse response = httpclient.execute(request);) {

            String jsonResponse = EntityUtils.toString(response.getEntity());
            log.info("[GET] jsonResponse [{}]", jsonResponse);
            fabrickTransactionsResponse = Optional.of(gson.fromJson(jsonResponse, FabrickTransactionsResponse.class));
        } catch (Exception e) {
            log.error("Error: ", e);
        }

        return fabrickTransactionsResponse;
    }

}
