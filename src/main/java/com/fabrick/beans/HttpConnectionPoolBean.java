package com.fabrick.beans;

import com.fabrick.configurations.HttpConnectionPoolConfig;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.stereotype.Component;

/**
 *
 * @author fabio.sgroi
 */
@Component
@Slf4j
public class HttpConnectionPoolBean {

    private CloseableHttpClient httpClient = null;
    private PoolingHttpClientConnectionManager connectionManager = null;
    private final Set<String> connectionPoolUsers = new HashSet<>();
    private HttpConnectionPoolConfig httpConnectionPoolLocal = null;
    private boolean shutdownConnectionManager = false;

    public CloseableHttpClient getHttpClient(String poolUser) {
        
        synchronized (this.connectionPoolUsers) {
            if (shutdownConnectionManager) {
                setupConnectionPoolLocal();
                this.shutdownConnectionManager = false;
            }
            if (this.connectionManager == null) {
                return null;
            } else if (this.connectionManager != null && this.httpClient == null) {
                this.httpClient = HttpClients.custom().setConnectionManager(this.connectionManager).build();
            }
            this.connectionPoolUsers.add(poolUser);
        }

        return this.httpClient;
    }

    public void setupConnectionPool(HttpConnectionPoolConfig httpConnectionPool) {
        this.httpConnectionPoolLocal = httpConnectionPool;
        if (httpConnectionPool != null && this.connectionManager == null) {
            this.connectionManager = new PoolingHttpClientConnectionManager();
            this.connectionManager.setDefaultMaxPerRoute(httpConnectionPool.getMaxConnectionsPerRoute());
            this.connectionManager.setMaxTotal(httpConnectionPool.getMaxConnectionsInTotal());
            if (httpConnectionPool.getConnectionValidationTime() != null) {
                this.connectionManager.setValidateAfterInactivity(httpConnectionPool.getConnectionValidationTime());
            }
        }
    }

    private void setupConnectionPoolLocal() {
        if (this.httpConnectionPoolLocal != null && this.connectionManager == null) {
            this.connectionManager = new PoolingHttpClientConnectionManager();
            this.connectionManager.setDefaultMaxPerRoute(this.httpConnectionPoolLocal.getMaxConnectionsPerRoute());
            this.connectionManager.setMaxTotal(this.httpConnectionPoolLocal.getMaxConnectionsInTotal());
            if (this.httpConnectionPoolLocal.getConnectionValidationTime() != null) {
                this.connectionManager.setValidateAfterInactivity(this.httpConnectionPoolLocal.getConnectionValidationTime());
            }
        }
    }

    public void close(String poolUser) {
        synchronized (this.connectionPoolUsers) {
            try {
                this.connectionPoolUsers.remove(poolUser);

                if (this.httpClient != null && this.connectionPoolUsers.isEmpty()) {
                    this.httpClient.close();
                    if (this.connectionManager != null) {
                        this.connectionManager.shutdown();
                        this.connectionManager = null;
                        this.shutdownConnectionManager = true;
                    }
                    this.httpClient = null;
                }
            } catch (IOException e) {
                log.error("Error: " + e.getMessage());
                log.error("Error:", e);
            }
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            if (this.httpClient != null) {
                this.httpClient.close();
            }
            if (this.connectionManager != null) {
                this.connectionManager.shutdown();
                this.shutdownConnectionManager = true;
            }
            this.httpClient = null;
            this.connectionManager = null;
            this.connectionPoolUsers.clear();
        } catch (IOException e) {
            log.error("Error: " + e.getMessage());
            log.error("Error:", e);
        }
    }

}
