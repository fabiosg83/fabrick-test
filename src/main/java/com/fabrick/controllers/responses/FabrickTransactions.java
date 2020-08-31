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
public class FabrickTransactions {
 
    protected List<FabrickTransaction> list;
}
