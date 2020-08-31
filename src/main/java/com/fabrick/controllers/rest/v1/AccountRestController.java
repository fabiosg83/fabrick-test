package com.fabrick.controllers.rest.v1;

import com.fabrick.controllers.requests.FabrickPaymentRequest;
import com.fabrick.controllers.responses.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.fabrick.interceptors.RestrictAccess;
import com.fabrick.services.AccountService;
import com.fabrick.services.PaymentService;
import com.fabrick.services.TransactionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import java.util.Date;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

/**
 *
 * @author fabio.sgroi
 */
@RestController
@RequestMapping("/v1/accounts")
@CrossOrigin(origins = "${app.cross.origin.accounts}")
@Api(value = "Accounts Controller", tags = {"Accounts"})
@RestrictAccess
@Slf4j
public class AccountRestController {

    @Autowired
    AccountService accountService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    TransactionService transactionService;

    @ApiOperation(value = "Get Account Balance", response = RestResponse.class, authorizations = {
        @Authorization(value = "apiKey")})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully")
        ,@ApiResponse(code = 401, message = "You are not authorized to view the resource")
        ,@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
        ,@ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @RequestMapping(name = "balance", value = "/{accountId}/balance", method = RequestMethod.GET, produces = "application/json")
    public RestResponse getAccount(HttpServletRequest request, HttpServletResponse response,
            @ApiIgnore
            @RequestHeader(name = "Authorization", required = false) String authorization,
            @PathVariable(value = "accountId", required = true) Long accountId
    ) throws Exception {
        log.info("CALL ROUTE [" + request.getRequestURI() + "]");

        RestResponse cResp = accountService.manageAccountBalance(authorization, accountId);
        response.setStatus(cResp.getStatusCode());

        return cResp;
    }

    @ApiOperation(value = "Post Payment", response = RestResponse.class, authorizations = {
        @Authorization(value = "apiKey")})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully")
        ,@ApiResponse(code = 401, message = "You are not authorized to view the resource")
        ,@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
        ,@ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @RequestMapping(name = "payments/money-transfers", value = "/{accountId}/payments/money-transfers", method = RequestMethod.POST, produces = "application/json")
    public RestResponse payment(HttpServletRequest request, HttpServletResponse response,
            @ApiIgnore
            @RequestHeader(name = "Authorization", required = false) String authorization,
            @PathVariable(value = "accountId", required = true) Long accountId,
            @Valid
            @RequestBody FabrickPaymentRequest fabrickPaymentRequest
    ) throws Exception {
        log.info("CALL ROUTE [" + request.getRequestURI() + "]");

        RestResponse cResp = paymentService.managePayment(authorization, accountId, fabrickPaymentRequest);
        response.setStatus(cResp.getStatusCode());

        return cResp;
    }

    @ApiOperation(value = "Get Transactions", response = RestResponse.class, authorizations = {
        @Authorization(value = "apiKey")})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully")
        ,@ApiResponse(code = 401, message = "You are not authorized to view the resource")
        ,@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
        ,@ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @RequestMapping(name = "transactions", value = "/{accountId}/transactions", method = RequestMethod.GET, produces = "application/json")
    public RestResponse getTransactions(HttpServletRequest request, HttpServletResponse response,
            @ApiIgnore
            @RequestHeader(name = "Authorization", required = false) String authorization,
            @PathVariable(value = "accountId", required = true) Long accountId,
            @RequestParam(name = "fromAccountingDate")
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam(name = "toAccountingDate")
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate
    ) throws Exception {
        log.info("CALL ROUTE [" + request.getRequestURI() + "]");

        RestResponse cResp = transactionService.manageTransactions(authorization, accountId, fromDate, toDate);
        response.setStatus(cResp.getStatusCode());

        return cResp;
    }

}
