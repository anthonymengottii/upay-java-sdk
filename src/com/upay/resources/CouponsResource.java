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

public class CouponsResource {

    private final HttpClientWrapper http;
    private final ObjectMapper mapper = new ObjectMapper();

    public CouponsResource(HttpClientWrapper http) {
        this.http = http;
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
