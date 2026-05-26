package com.upay.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.upay.http.HttpClientWrapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WalletResource {

    private final HttpClientWrapper http;

    public WalletResource(HttpClientWrapper http) {
        this.http = Objects.requireNonNull(http, "HttpClientWrapper cannot be null");
    }

    /** Retorna o resumo da carteira (saldo, recebido, sacado) */
    public JsonNode getSummary() throws IOException, InterruptedException {
        return http.get("/wallet/summary", null);
    }

    /** Retorna o extrato da carteira com paginação */
    public JsonNode getStatement(Integer page, Integer limit, String startDate, String endDate)
            throws IOException, InterruptedException {
        Map<String, Object> params = new HashMap<>();
        if (page != null) params.put("page", page);
        if (limit != null) params.put("limit", limit);
        if (startDate != null) params.put("startDate", startDate);
        if (endDate != null) params.put("endDate", endDate);
        return http.get("/wallet/statement", params);
    }
}
