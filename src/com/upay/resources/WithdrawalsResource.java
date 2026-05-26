package com.upay.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.upay.http.HttpClientWrapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WithdrawalsResource {

    private final HttpClientWrapper http;

    public WithdrawalsResource(HttpClientWrapper http) {
        this.http = Objects.requireNonNull(http, "HttpClientWrapper cannot be null");
    }

    /** Lista saques */
    public JsonNode list(Integer page, Integer limit, String status) throws IOException, InterruptedException {
        Map<String, Object> params = new HashMap<>();
        if (page != null) params.put("page", page);
        if (limit != null) params.put("limit", limit);
        if (status != null) params.put("status", status);
        return http.get("/withdraws", params);
    }

    /** Obtém um saque por ID */
    public JsonNode get(String id) throws IOException, InterruptedException {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID é obrigatório");
        return http.get("/withdraws/" + id, null);
    }

    /** Retorna o saldo disponível para saque */
    public JsonNode getBalance() throws IOException, InterruptedException {
        return http.get("/withdraws/balance", null);
    }

    /** Cria uma solicitação de saque */
    public JsonNode create(Map<String, Object> data) throws IOException, InterruptedException {
        if (data == null) throw new IllegalArgumentException("data é obrigatório");
        Object amountObj = data.get("amountCents");
        if (amountObj == null || ((Number) amountObj).intValue() < 100) {
            throw new IllegalArgumentException("Valor mínimo é R$ 1,00 (100 centavos)");
        }
        if (!data.containsKey("pixKey") && !data.containsKey("bankAccount")) {
            throw new IllegalArgumentException("pixKey ou bankAccount é obrigatório");
        }
        return http.post("/withdraws", data);
    }

    /** Cancela um saque pendente */
    public JsonNode cancel(String id) throws IOException, InterruptedException {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID é obrigatório");
        return http.post("/withdraws/" + id + "/cancel", null);
    }
}
