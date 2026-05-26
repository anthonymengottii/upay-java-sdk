package com.upay.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.upay.http.HttpClientWrapper;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class OffersResource {

    private final HttpClientWrapper http;

    public OffersResource(HttpClientWrapper http) {
        this.http = Objects.requireNonNull(http, "HttpClientWrapper cannot be null");
    }

    // ── Order Bumps ──────────────────────────────────────────────────────────

    /** Lista order bumps de um checkout */
    public JsonNode listOrderBumps(String checkoutId) throws IOException, InterruptedException {
        if (checkoutId == null || checkoutId.isBlank()) throw new IllegalArgumentException("checkoutId é obrigatório");
        return http.get("/checkouts/" + checkoutId + "/order-bumps", null);
    }

    /** Cria um order bump em um checkout */
    public JsonNode createOrderBump(String checkoutId, Map<String, Object> data) throws IOException, InterruptedException {
        if (checkoutId == null || checkoutId.isBlank()) throw new IllegalArgumentException("checkoutId é obrigatório");
        if (data == null || !data.containsKey("productId")) throw new IllegalArgumentException("productId é obrigatório");
        return http.post("/checkouts/" + checkoutId + "/order-bumps", data);
    }

    /** Atualiza um order bump */
    public JsonNode updateOrderBump(String checkoutId, String bumpId, Map<String, Object> data)
            throws IOException, InterruptedException {
        if (checkoutId == null || checkoutId.isBlank()) throw new IllegalArgumentException("checkoutId é obrigatório");
        if (bumpId == null || bumpId.isBlank()) throw new IllegalArgumentException("bumpId é obrigatório");
        return http.put("/checkouts/" + checkoutId + "/order-bumps/" + bumpId, data);
    }

    /** Remove um order bump */
    public JsonNode deleteOrderBump(String checkoutId, String bumpId) throws IOException, InterruptedException {
        if (checkoutId == null || checkoutId.isBlank()) throw new IllegalArgumentException("checkoutId é obrigatório");
        if (bumpId == null || bumpId.isBlank()) throw new IllegalArgumentException("bumpId é obrigatório");
        return http.delete("/checkouts/" + checkoutId + "/order-bumps/" + bumpId);
    }

    // ── Upsell ───────────────────────────────────────────────────────────────

    /** Obtém o upsell de um checkout */
    public JsonNode getUpsell(String checkoutId) throws IOException, InterruptedException {
        if (checkoutId == null || checkoutId.isBlank()) throw new IllegalArgumentException("checkoutId é obrigatório");
        return http.get("/checkouts/" + checkoutId + "/upsell", null);
    }

    /** Cria ou atualiza o upsell de um checkout */
    public JsonNode upsertUpsell(String checkoutId, Map<String, Object> data) throws IOException, InterruptedException {
        if (checkoutId == null || checkoutId.isBlank()) throw new IllegalArgumentException("checkoutId é obrigatório");
        if (data == null || !data.containsKey("productId")) throw new IllegalArgumentException("productId é obrigatório");
        return http.post("/checkouts/" + checkoutId + "/upsell", data);
    }

    /** Remove o upsell de um checkout */
    public JsonNode deleteUpsell(String checkoutId) throws IOException, InterruptedException {
        if (checkoutId == null || checkoutId.isBlank()) throw new IllegalArgumentException("checkoutId é obrigatório");
        return http.delete("/checkouts/" + checkoutId + "/upsell");
    }

    // ── Downsell ─────────────────────────────────────────────────────────────

    /** Cria ou atualiza o downsell de um checkout */
    public JsonNode upsertDownsell(String checkoutId, Map<String, Object> data) throws IOException, InterruptedException {
        if (checkoutId == null || checkoutId.isBlank()) throw new IllegalArgumentException("checkoutId é obrigatório");
        if (data == null || !data.containsKey("productId")) throw new IllegalArgumentException("productId é obrigatório");
        return http.post("/checkouts/" + checkoutId + "/upsell/downsell", data);
    }

    /** Remove o downsell de um checkout */
    public JsonNode deleteDownsell(String checkoutId) throws IOException, InterruptedException {
        if (checkoutId == null || checkoutId.isBlank()) throw new IllegalArgumentException("checkoutId é obrigatório");
        return http.delete("/checkouts/" + checkoutId + "/upsell/downsell");
    }
}
