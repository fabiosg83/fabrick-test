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
public class FabrickPaymentCreditor {

    @NotBlank(message="Name is a required field")
    private String name;
    protected FabrickPaymentCreditorAccount account;

}
