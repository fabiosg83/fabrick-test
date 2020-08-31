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
@ConfigurationProperties(prefix = "fabrick.client")
@Getter
@Setter
@ToString
public class FabrickClientConfig {

    protected String baseUrl;
    protected String uriBalance;
    protected String uriPayments;
    protected String uriTransactions;
    protected Integer timeout;
}
