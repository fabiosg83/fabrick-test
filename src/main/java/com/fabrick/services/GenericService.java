package com.fabrick.services;

import com.fabrick.client.FabrickClient;
import com.fabrick.configurations.FabrickClientConfig;
import com.fabrick.controllers.responses.RestResponse;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author fabio.sgroi
 */
@Service
@Slf4j
public class GenericService {

    @Autowired
    FabrickClientConfig fabrickClientConfig;

    @Autowired
    FabrickClient fabrickClient;

    //<editor-fold defaultstate="collapsed" desc="ERROR_RESPONSE">
    protected RestResponse errorResponse(RestResponse cResp, int httpStatus, String logMessage) {
        log.error("Error: " + logMessage);
        cResp.setStatusCode(httpStatus);
        cResp.setStatusMessage("KO");
        cResp.setData(logMessage.toLowerCase().replaceAll(" ", "_"));
        return cResp;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CLEAN_TOKEN">
    protected Optional<String> cleanToken(String tokenFromHeader) {
        if (StringUtils.isNotBlank(tokenFromHeader)) {
            return Optional.of(tokenFromHeader.replace("Bearer", "").trim());
        } else {
            return Optional.empty();
        }
    }
    //</editor-fold>

}
