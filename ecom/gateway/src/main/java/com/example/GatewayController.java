package com.example;


import io.micronaut.http.*;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;

import java.net.URI;

@Controller("/api")
public class GatewayController {

    private final HttpClient client;
    private final GatewayProperties properties;

    public GatewayController(@Client("*") HttpClient client,
                             GatewayProperties properties) {
        this.client = client;
        this.properties = properties;
    }

    @Get("/{service}/{path:.*}")
    public HttpResponse<?> proxyGet(HttpRequest<?> request,
                                    @PathVariable String service,
                                    @PathVariable String path) {
        return forward(request, service, path);
    }

    @Post("/{service}/{path:.*}")
    public HttpResponse<?> proxyPost(HttpRequest<?> request,
                                     @PathVariable String service,
                                     @PathVariable String path) {
        return forward(request, service, path);
    }

    @Put("/{service}/{path:.*}")
    public HttpResponse<?> proxyPut(HttpRequest<?> request,
                                    @PathVariable String service,
                                    @PathVariable String path) {
        return forward(request, service, path);
    }

    @Delete("/{service}/{path:.*}")
    public HttpResponse<?> proxyDelete(HttpRequest<?> request,
                                       @PathVariable String service,
                                       @PathVariable String path) {
        return forward(request, service, path);
    }

    private HttpResponse<?> forward(HttpRequest<?> request,
                                    String service,
                                    String path) {

        String baseUrl = properties.getRoutes().get(service);

        if (baseUrl == null) {
            return HttpResponse.notFound("Unknown service: " + service);
        }

        String targetUrl = baseUrl + "/" + service + "/" + path;

        HttpRequest<?> forward = HttpRequest
                .create(request.getMethod(), URI.create(targetUrl))
                .body(request.getBody().orElse(null));

        request.getHeaders().forEach((k, v) ->
                forward.getHeaders().add(k, v));

        return client.toBlocking().exchange(forward, String.class);
    }
}