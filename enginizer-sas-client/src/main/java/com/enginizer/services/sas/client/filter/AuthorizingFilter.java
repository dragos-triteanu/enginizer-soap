//package com.enginizer.services.sas.client.filter;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * Filter class that performs an authorization in the host spring context.
// */
//public class AuthorizingFilter implements Filter {
//    private static final Logger LOG = LoggerFactory.getLogger(AuthorizingFilter.class);
//
//    private PermissionEvaluationService permissionEvaluationService;
//
//    public AuthorizingFilter(final PermissionEvaluationService permissionEvaluationService) {
//        this.permissionEvaluationService = permissionEvaluationService;
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest req = (HttpServletRequest) servletRequest;
//        HttpServletResponse res = (HttpServletResponse) servletResponse;
//        String path = req.getPathInfo();
//
//        AuthorizedPath authorizedPath = AuthorizedPath.fromString(path);
//
//        if (null != authorizedPath) {
//            switch (authorizedPath) {
//                case DOMESTIC_PAYMENT:
//                    AuthorizationDTO authorized = permissionEvaluationService.isAuthorized("DomesticPaymentResource", "domesticPayment", "amount");
//                    res.setContentType("application/json");
//                    if (null == authorized) {
//                        LOG.info("Authorization information not found on cookie");
//                        res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization info not found");
//                        return;
//                    }
//
//                    LOG.info("SAS authorization responded with returnCode=",authorized.getReturnCode());
//
//                    if (authorized.getReturnCode() != 0) {
//                        res.sendError(HttpServletResponse.SC_UNAUTHORIZED, authorized.getErrorMessage());
//                        return;
//                    }
//                    break;
//                default:
//                    break;
//            }
//        }
//
//        filterChain.doFilter(servletRequest, servletResponse);
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//
//    public void setPermissionEvaluationService(PermissionEvaluationService permissionEvaluationService) {
//        this.permissionEvaluationService = permissionEvaluationService;
//    }
//}
