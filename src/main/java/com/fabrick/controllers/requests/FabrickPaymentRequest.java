package com.fabrick.controllers.requests;

import java.util.Date;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author fabio.sgroi
 */
@Getter
@Setter
@ToString
public class FabrickPaymentRequest {

    protected FabrickPaymentCreditor creditor;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    protected Date executionDate;
    @NotBlank(message = "Description is a required field")
    protected String description;
    @NotNull(message = "Amount is a required field")
    @DecimalMin("0.01")
    protected Double amount;
    @NotBlank(message = "Currency is a required field")
    protected String currency;

}
/*
{
  "creditor": {
    "name": "LUCA TERRIBILE",
    "account": {
      "accountCode": "IT40L0326822311052923800661"
    }
  },
  "executionDate": "2019-04-01",

  "description": "Payment test",
  "amount": 1,
  "currency": "EUR"
}
 */
