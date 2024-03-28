package dev.eduardylopes.gestao_vagas.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import dev.eduardylopes.gestao_vagas.providers.JWTCompanyProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityCompanyFilter extends OncePerRequestFilter {

  @Autowired
  JWTCompanyProvider jwtCompanyProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    if (request.getRequestURI().startsWith("/companies")) {
      SecurityContextHolder.getContext().setAuthentication(null);
      String token = request.getHeader("Authorization");

      if (token != null) {
        var decodedJwt = this.jwtCompanyProvider.validateToken(token);

        if (decodedJwt == null) {
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          return;
        }

        request.setAttribute("company_id", decodedJwt.getSubject());

        var roles = decodedJwt.getClaim("roles")
            .asList(String.class)
            .stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
            .toList();

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(token,
            null,
            roles);

        SecurityContextHolder.getContext().setAuthentication(auth);
      }
    }

    filterChain.doFilter(request, response);
  }

}
