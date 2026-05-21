package com.example;

import io.micronaut.context.annotation.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties("gateway")
public class GatewayProperties {

    private Map<String, String> routes = new HashMap<>();

    public Map<String, String> getRoutes() {
        return routes;
    }

    public void setRoutes(Map<String, String> routes) {
        this.routes = routes;
    }
}