package com.upay.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.upay.http.HttpClientWrapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SubscriptionsResource {

    private final HttpClientWrapper http;

    public SubscriptionsResource(HttpClientWrapper http) {
        this.http = Objects.requireNonNull(http, "HttpClientWrapper cannot be null");
    }

    /** Lista assinaturas */
    public JsonNode list(Integer page, Integer limit, String status) throws IOException, InterruptedException {
        Map<String, Object> params = new HashMap<>();
        if (page != null) params.put("page", page);
        if (limit != null) params.put("limit", limit);
        if (status != null) params.put("status", status);
        return http.get("/subscriptions", params);
    }

    /** Cria uma assinatura */
    public JsonNode create(Map<String, Object> data) throws IOException, InterruptedException {
        if (data == null || !data.containsKey("planId") || data.get("planId") == null) {
            throw new IllegalArgumentException("planId é obrigatório");
        }
        return http.post("/subscriptions", data);
    }

    /** Cancela uma assinatura */
    public JsonNode cancel(String id) throws IOException, InterruptedException {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID é obrigatório");
        return http.patch("/subscriptions/" + id + "/cancel", null);
    }

    /** Pausa uma assinatura */
    public JsonNode pause(String id) throws IOException, InterruptedException {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID é obrigatório");
        return http.patch("/subscriptions/" + id + "/pause", null);
    }

    /** Retoma uma assinatura pausada */
    public JsonNode resume(String id) throws IOException, InterruptedException {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID é obrigatório");
        return http.patch("/subscriptions/" + id + "/resume", null);
    }

    /** Tenta reprocessar o pagamento de uma assinatura em atraso */
    public JsonNode retry(String id) throws IOException, InterruptedException {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID é obrigatório");
        return http.post("/subscriptions/" + id + "/retry", null);
    }

    /** Retorna métricas de assinaturas (MRR, churn, totais) */
    public JsonNode getMetrics() throws IOException, InterruptedException {
        return http.get("/subscriptions/metrics", null);
    }
}
