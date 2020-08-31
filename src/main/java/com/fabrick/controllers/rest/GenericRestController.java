package com.fabrick.controllers.rest;

import com.fabrick.controllers.responses.RestResponse;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author fabio.sgroi
 */
@RestController
@RequestMapping(value = "**")
public class GenericRestController {

    @RequestMapping(value = {"/{path:^(?!swagger-ui).*}"})
    public RestResponse resourceNotFound(HttpServletResponse response) {

        RestResponse cResp = new RestResponse(404, "KO", "resource_not_found");

        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return cResp;
    }

    @RequestMapping(value = {"/", "/v*/**"})
    public RestResponse resourceNotFoundGeneric(HttpServletResponse response) {

        RestResponse cResp = new RestResponse(404, "KO", "resource_not_found_generic");

        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return cResp;
    }
}
