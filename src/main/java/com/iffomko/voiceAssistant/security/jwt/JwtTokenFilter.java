package com.iffomko.voiceAssistant.security.jwt;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Фильтр, который спрашивает аутентификацию у всех запросов.
 * Если её нет, то дальше он запрос не пропускает
 */
@Component
public class JwtTokenFilter extends GenericFilter {
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Инициализирует поля нужными значениями
     * @param jwtTokenProvider объект по работе с JWT токеном
     */
    @Autowired
    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Этот методы вызывает при обработке цепочки фильтров
     * @param servletRequest запрос, который проходит обработку фильтров
     * @param servletResponse ответ на запрос, который проходит обработку фильтров
     * @param filterChain цепочка фильтров
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException
    {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);

        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (JwtAuthenticationException e) {
            SecurityContextHolder.clearContext();
            throw new JwtAuthenticationException(e.getMessage(), e.getStatus());
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
