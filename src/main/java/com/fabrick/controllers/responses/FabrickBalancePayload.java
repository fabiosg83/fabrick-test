package com.fabrick.controllers.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class FabrickBalancePayload {

    @JsonFormat(pattern = "yyyy-MM-dd")
    protected Date date;
    protected Double balance;
    protected Double availableBalance;
    protected String currency;
}
