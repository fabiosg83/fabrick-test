package com.fabrick.configurations;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author fabio.sgroi
 */
@Configuration
@ConfigurationProperties(prefix = "http.connection.pool")
@Getter
@Setter
@ToString
public class HttpConnectionPoolConfig {

    protected Boolean enable;
    protected Integer maxConnectionsPerRoute;
    protected Integer maxConnectionsInTotal;
    protected Integer connectionValidationTime;
    protected Integer idleConnectionsTimeout;

}
