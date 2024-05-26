package com.AutoMeet.global.auth.jwt;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class JwtProperties {

    private String secret = "AutoMeet";

    private Integer expirationTime = 1200000;

    private Integer refreshTokenTime = 604800000;

    private String tokenPrefix = "Bearer ";

    private String headerString = "Access-token";
}