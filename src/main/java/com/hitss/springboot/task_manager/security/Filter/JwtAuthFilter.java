package com.hitss.springboot.task_manager.security.Filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.hitss.springboot.task_manager.security.JwtToken.CONTENT_TYPE;
import static com.hitss.springboot.task_manager.security.JwtToken.HEADER_AUTHORIZATION;
import static com.hitss.springboot.task_manager.security.JwtToken.PREFIX_TOKEN;
import static com.hitss.springboot.task_manager.security.JwtToken.SECRET_KEY;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hitss.springboot.task_manager.models.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class JwtAuthFilter extends UsernamePasswordAuthenticationFilter{
    private AuthenticationManager authenticationManager;

        

        public JwtAuthFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

        @Override
        public Authentication attemptAuthentication(
                                                    HttpServletRequest request, 
                                                    HttpServletResponse response
                                                    )throws AuthenticationException {
            User user = null;
            String username = null;
            String password = null;

            try {
                user = new ObjectMapper().readValue(request.getInputStream(), User.class);
                username = user.getUsername();
                password = user.getPassword();
            } catch (StreamReadException e) {
                throw new AuthenticationServiceException("Error al leer el JSON de la petición", e);
            } catch (DatabindException e) {
                throw new AuthenticationServiceException("Error al deserializar el objeto User", e);
            } catch (IOException e) {
                throw new AuthenticationServiceException("Error de I/O al leer la petición", e);
            }

            if (username == null || password == null) {
                throw new BadCredentialsException("Username y password son requeridos");
            }

            UsernamePasswordAuthenticationToken token = new 
            UsernamePasswordAuthenticationToken(username, password);
            return authenticationManager.authenticate(token);
        }

        @Override
        protected void successfulAuthentication(HttpServletRequest request, 
                                                HttpServletResponse response,
                FilterChain chain, Authentication authResult) throws IOException, ServletException {
            
            org.springframework.security.core.userdetails.User user = 
            (org.springframework.security.core.userdetails.User) authResult.getPrincipal();
            String username = user.getUsername();

            Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();


            Claims claims = Jwts.claims()
                            .add("authorities", new ObjectMapper().writeValueAsString(roles))
                            .add("username", username)
                            .build();

          
            long expirationTime = System.currentTimeMillis() + 3600000;
            Date expirationDate = new Date(expirationTime);
            Date issuedAt = new Date();
            
            String token = Jwts.builder()
                               .subject(username)
                               .claims(claims)
                               .expiration(expirationDate)
                               .issuedAt(issuedAt)
                               .signWith(SECRET_KEY)
                               .compact();

            response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);

            Map<String, String> body = new HashMap<>();
            body.put("token", token);
            body.put("username", username);
            body.put("message", String.format("Hola %s has iniciado sesión con exito!", username));

            response.setContentType(CONTENT_TYPE);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(new ObjectMapper().writeValueAsString(body));

        }

        @Override
        protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                AuthenticationException failed) throws IOException, ServletException {
            Map<String, String> body = new HashMap<>();

            body.put("message", "Error en la autenticación username o password incorrectos!");
            body.put("error", failed.getMessage());

            response.setContentType(CONTENT_TYPE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        }
}
