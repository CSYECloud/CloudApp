package com.hari.cloud.app.filter;

import com.hari.cloud.app.dao.User;
import com.hari.cloud.app.service.UserService;
import com.hari.cloud.app.util.Utility;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import java.util.Base64;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.io.IOException;

public class AuthenticationFilter extends OncePerRequestFilter {
    UserService userService;

    public AuthenticationFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("authorization");
        if(authHeader==null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid auth token");
            return;
        }
        authHeader = authHeader.substring("Basic".length()).trim();
        byte[] decodedBytes = Base64.getDecoder().decode(authHeader);
        String decodedAuthHeader = new String(decodedBytes);

        String[] email_pass = decodedAuthHeader.split(":");
        if(email_pass.length < 2 || email_pass[0].isBlank() || email_pass[1].isBlank()) {
            filterChain.doFilter(request, response);
            System.out.println("Filtering harry");
            return;
        }
        String email = email_pass[0];
        String password = email_pass[1];

        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userService.getUserBy(email);
            // Perform user password validation
            Boolean isAuthenticated = isUserAuthenticated(password, user);

            if(isAuthenticated) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    private Boolean isUserAuthenticated(String password, User user) {
        System.out.println(Utility.passwordEncoder.matches(password, user.getPassword())+" Encoded pwd");
        return Utility.passwordEncoder.matches(password, user.getPassword());
    }
}