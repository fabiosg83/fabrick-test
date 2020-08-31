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
public class FabrickErrorResponse {

    protected String code;
    protected String description;
    protected String params;
}
