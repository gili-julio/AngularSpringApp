package com.nut.springboot.security;

public class ApiEndpoints {

    // Endpoints permitidos sem autenticação
    public static final String[] PUBLIC_ENDPOINTS = {
            "/auth"
    };

    // Endpoints que requerem autenticação
    public static final String[] SECURED_ENDPOINTS = {
            "/teste"
    };
}
