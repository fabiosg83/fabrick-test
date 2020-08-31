package com.fabrick.configurations;

import com.fabrick.beans.HttpConnectionPoolBean;
import com.fabrick.client.FabrickClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 *
 * @author fabio.sgroi
 */
@Configuration
public class ConfigurationBase {

    private final static Logger jLogger = LoggerFactory.getLogger("main");

    @Autowired
    ConfigurableEnvironment env;

    @Autowired
    HttpConnectionPoolConfig httpConnectionPoolConfig;

    @Autowired
    HttpConnectionPoolBean httpConnectionPoolManager;

    @Autowired
    FabrickClient fabrickClient;

    private String swaggerHost = null;
    private List<String> protocols = null;
    private List<String> whitelistIp = null;

    @PostConstruct
    public void run() {
        jLogger.info("LOAD CONFIGURATION - START");

        String localeLang = env.getProperty("app.locale.lang").toLowerCase();
        String localeCountry = env.getProperty("app.locale.country").toUpperCase();
        Locale.setDefault(new Locale(localeLang, localeCountry));

        swaggerHost = env.getProperty("swagger.host", "localhost:8888");

        loadProtocols();

        loadWhitelistIp();

        if (httpConnectionPoolConfig.getEnable()) {
            httpConnectionPoolManager.setupConnectionPool(httpConnectionPoolConfig);
            fabrickClient.setClientPool(httpConnectionPoolManager.getHttpClient("pool"));
        }

        jLogger.info("LOAD CONFIGURATION - FINISH");
    }

    //<editor-fold defaultstate="collapsed" desc="LOAD_PROTOCOLS">
    private void loadProtocols() {

        protocols = new ArrayList<>();
        String strProtocols = env.getProperty("swagger.protocols");
        if (StringUtils.isNotBlank(strProtocols)) {
            String[] arrProtocols = strProtocols.split(",");
            for (String arrProtocol : arrProtocols) {
                protocols.add(arrProtocol);
            }
        } else {
            protocols.add("http");
        }
    }

    public List<String> getProtocols() {
        return protocols;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="LOAD_WHITELIST_IP">
    private void loadWhitelistIp() {

        whitelistIp = new ArrayList<>();
        String strWhitelistIp = env.getProperty("app.ip.whitelist");
        if (StringUtils.isNotBlank(strWhitelistIp)) {
            String[] arrWhitelistIp = strWhitelistIp.split(",");
            for (String arrIp : arrWhitelistIp) {
                whitelistIp.add(arrIp);
            }
        }
    }

    public List<String> getWhitelistIp() {
        return whitelistIp;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="SWAGGER">
    public String getSwaggerHost() {
        return swaggerHost;
    }
    //</editor-fold>

}
