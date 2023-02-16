package com.team05.codebotiics.mopi_webapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles authentication success.
 */
@Configuration
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    /**
     * Redirects the client to the "/home" mapping. The controller handles authorisation from there.
     * @param httpServletRequest
     * @param httpServletResponse
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.sendRedirect("/home");
    }
}
