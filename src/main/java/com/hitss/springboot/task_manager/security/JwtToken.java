package com.hitss.springboot.task_manager.security;


import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


@Component
public class JwtToken {
    
    private static final String SECRET_STRING = "MiClaveSecretaMuySegura123456789012345678901234567890";
    public static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes());
    public static final String PREFIX_TOKEN = "Bearer ";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String CONTENT_TYPE = "application/json";
}
