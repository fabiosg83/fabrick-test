package com.fabrick.controllers.responses;

import java.util.Date;
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
public class FabrickTransaction {

    protected String transactionId;
    protected String operationId;
    protected Date accountingDate;
    protected Date valueDate;
    protected Double amount;
    protected String currency;
    protected String description;
    protected FabrickTransactionType type;
}
