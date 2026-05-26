package com.upay.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.upay.http.HttpClientWrapper;
import com.upay.utils.UpayException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CouponsResource {

    private final HttpClientWrapper http;
    private final ObjectMapper mapper = new ObjectMapper();

    public CouponsResource(HttpClientWrapper http) {
        this.http = Objects.requireNonNull(http, "HttpClientWrapper cannot be null");
    }

    /** Lista cupons */
    public JsonNode list(Integer page, Integer limit) throws IOException, InterruptedException {
        Map<String, Object> params = new HashMap<>();
        if (page != null) params.put("page", page);
        if (limit != null) params.put("limit", limit);
        return http.get("/coupons", params);
    }

    /** Cria um cupom */
    public JsonNode create(Map<String, Object> data) throws IOException, InterruptedException {
        if (data == null || data.get("code") == null || data.get("code").toString().isBlank()) {
            throw new IllegalArgumentException("Código do cupom é obrigatório");
        }
        return http.post("/coupons", data);
    }

    /** Obtém um cupom por ID */
    public JsonNode getCoupon(String id) throws IOException, InterruptedException {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID é obrigatório");
        return http.get("/coupons/" + id, null);
    }

    /** Atualiza um cupom */
    public JsonNode update(String id, Map<String, Object> data) throws IOException, InterruptedException {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID é obrigatório");
        return http.patch("/coupons/" + id, data);
    }

    /** Deleta um cupom */
    public JsonNode delete(String id) throws IOException, InterruptedException {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID é obrigatório");
        return http.delete("/coupons/" + id);
    }

    /**
     * Valida um cupom de desconto usando o endpoint público /api/coupons/validate.
     * O endpoint pode retornar 400 com { valid: false } para cupons inválidos — isso
     * é tratado como resposta válida, não como erro.
     */
    public JsonNode validate(String code, int amountCents) throws IOException, InterruptedException {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Código do cupom é obrigatório");
        }
        if (amountCents < 100) {
            throw new IllegalArgumentException("Valor mínimo é R$ 1,00 (100 centavos)");
        }

        Map<String, Object> body = new HashMap<>();
        body.put("code", code.trim());
        body.put("amount", amountCents);

        // Faz chamada direta pois 400 com {valid:false} é resposta válida
        String url = http.getBaseUrl() + "/api/coupons/validate";
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(30))
                .header("Content-Type", "application/json")
                .header("User-Agent", "Upay-Java-SDK/1.0.0")
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonNode json;
        try {
            json = response.body() != null && !response.body().isEmpty()
                    ? mapper.readTree(response.body())
                    : mapper.createObjectNode();
        } catch (Exception e) {
            json = mapper.createObjectNode();
        }

        // 400 com {valid} é resposta válida; outros erros 4xx/5xx lançam exceção
        if (response.statusCode() >= 400 && !json.has("valid")) {
            String message = json.path("message").asText("HTTP " + response.statusCode());
            throw new UpayException(message, null, response.statusCode());
        }

        return json;
    }
}
