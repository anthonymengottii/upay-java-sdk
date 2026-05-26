package com.upay.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.upay.http.HttpClientWrapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CheckoutsResource {

    private final HttpClientWrapper http;

    public CheckoutsResource(HttpClientWrapper http) {
        this.http = Objects.requireNonNull(http, "HttpClientWrapper cannot be null");
    }

    /** Lista checkouts */
    public JsonNode list(Integer page, Integer limit) throws IOException, InterruptedException {
        Map<String, Object> params = new HashMap<>();
        if (page != null) params.put("page", page);
        if (limit != null) params.put("limit", limit);
        return http.get("/checkouts", params);
    }

    /** Cria um checkout */
    public JsonNode create(Map<String, Object> data) throws IOException, InterruptedException {
        if (data == null || data.get("name") == null || data.get("name").toString().isBlank()) {
            throw new IllegalArgumentException("name é obrigatório");
        }
        return http.post("/checkouts", data);
    }

    /** Obtém um checkout por ID */
    public JsonNode get(String id) throws IOException, InterruptedException {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID é obrigatório");
        return http.get("/checkouts/" + id, null);
    }

    /** Atualiza um checkout */
    public JsonNode update(String id, Map<String, Object> data) throws IOException, InterruptedException {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID é obrigatório");
        return http.put("/checkouts/" + id, data);
    }

    /** Deleta um checkout */
    public JsonNode delete(String id) throws IOException, InterruptedException {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID é obrigatório");
        return http.delete("/checkouts/" + id);
    }
}
