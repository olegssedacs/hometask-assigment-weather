package com.paymentology.weather.infra.http;

import com.paymentology.weather.domain.client.ClientKeyRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private final ClientKeyRepository keyRepo;

    @Autowired
    public ApiKeyAuthFilter(ClientKeyRepository keyRepo) {
        this.keyRepo = keyRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (permit(request)) {
            filterChain.doFilter(request, response);
        } else {
            response.sendError(403);
        }
    }

    private boolean permit(HttpServletRequest request) {
        return Optional
                .ofNullable(request.getHeader("X-API-KEY"))
                .flatMap(keyRepo::find)
                .map(key -> !key.revoked())
                .orElse(false);
    }
}
