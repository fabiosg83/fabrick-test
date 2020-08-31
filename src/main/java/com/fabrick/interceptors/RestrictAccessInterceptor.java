package com.fabrick.interceptors;

import com.fabrick.controllers.responses.RestResponse;
import com.fabrick.dao.OperationImpl;
import com.fabrick.entity.Operations;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 *
 * @author fabio.sgroi
 */
@Slf4j
public class RestrictAccessInterceptor extends HandlerInterceptorAdapter {

    private final ObjectMapper mapper = new ObjectMapper();
    private final List<String> ips;
    private final OperationImpl operationImpl;

    public RestrictAccessInterceptor(List<String> ips, OperationImpl operationImpl) {
        this.ips = ips;
        this.operationImpl = operationImpl;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            Method methodClass = method.getMethod();
            if (method.getMethodAnnotation(RestrictAccess.class) != null || methodClass.getDeclaringClass().isAnnotationPresent(RestrictAccess.class)) {
                log.info("CALL ROUTE [" + request.getRequestURI() + "]");

                if (!ips.contains(request.getRemoteAddr())) {
                    errorResponse(response, "ip_not_authorized");
                    return false;
                }

                Optional<String> paramsToVerify = getParamsFromHeader(request, response);
                if (!paramsToVerify.isPresent()) {
                    return false;
                }

                Operations operation = new Operations();
                operation.setApikey(paramsToVerify.get());
                operation.setRoute(request.getRequestURI());
                operation.setDtCall(new Date());

                operationImpl.saveOrUpdateObject(operation);
            }
        }

        return true;
    }

    private Optional<String> getParamsFromHeader(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String tokenToVerify = request.getHeader("Authorization");
        if (StringUtils.isBlank(tokenToVerify)) {
            errorResponse(response, "token_not_valid");
            return Optional.empty();
        }

        if (tokenToVerify.regionMatches(true, 0, "Bearer", 0, "Bearer".length())) {
            tokenToVerify = tokenToVerify.replace("Bearer", "").replace("bearer", "").trim();
        }

        return Optional.of(tokenToVerify);
    }

    private void errorResponse(HttpServletResponse response, String errorMessage) throws Exception {
        log.error("Error Interceptor [{}]", errorMessage);
        RestResponse resp = new RestResponse(HttpServletResponse.SC_UNAUTHORIZED, "KO", errorMessage);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(mapper.writeValueAsString(resp));
    }

}
