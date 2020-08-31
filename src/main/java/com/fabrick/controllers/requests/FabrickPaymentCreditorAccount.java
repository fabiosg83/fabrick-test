package com.fabrick.controllers.requests;

import javax.validation.constraints.NotBlank;
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
public class FabrickPaymentCreditorAccount {

    @NotBlank(message="AccountCode is a required field")
    protected String accountCode;

}
