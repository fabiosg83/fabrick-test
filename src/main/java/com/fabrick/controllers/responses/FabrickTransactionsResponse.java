package com.fabrick.controllers.responses;

import java.util.List;
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
public class FabrickTransactionsResponse {

    protected String status;
    protected List<FabrickErrorResponse> error;
    protected List<FabrickErrorResponse> errors;
    protected FabrickTransactions payload;
}
