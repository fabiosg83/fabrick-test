package com.fabrick.controllers.responses;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author fabio.sgroi
 */
@Getter
@Setter
@ToString
public class RestResponse {

    protected Integer statusCode;
    protected String statusMessage;
    protected Object data;

    //<editor-fold defaultstate="collapsed" desc="CONSTRUCTORS">
    public RestResponse() {
        statusCode = 200;
        statusMessage = "";
        data = new Object();
    }

    public RestResponse(Integer statusCode, String statusMessage, Object data) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.data = data;
    }
    //</editor-fold>

}
