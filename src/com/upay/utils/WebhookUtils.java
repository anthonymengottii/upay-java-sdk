package com.upay.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public final class WebhookUtils {

    private WebhookUtils() {}

    // Tipos de eventos de webhook
    public static final String EVENT_TRANSACTION_CREATED        = "transaction.created";
    public static final String EVENT_TRANSACTION_PAID           = "transaction.paid";
    public static final String EVENT_TRANSACTION_FAILED         = "transaction.failed";
    public static final String EVENT_TRANSACTION_CANCELLED      = "transaction.cancelled";
    public static final String EVENT_TRANSACTION_REFUNDED       = "transaction.refunded";
    public static final String EVENT_PAYMENT_LINK_CREATED       = "payment_link.created";
    public static final String EVENT_PAYMENT_LINK_UPDATED       = "payment_link.updated";
    public static final String EVENT_PAYMENT_LINK_DELETED       = "payment_link.deleted";
    public static final String EVENT_SUBSCRIPTION_CREATED       = "subscription.created";
    public static final String EVENT_SUBSCRIPTION_CANCELLED     = "subscription.cancelled";
    public static final String EVENT_SUBSCRIPTION_PAUSED        = "subscription.paused";
    public static final String EVENT_SUBSCRIPTION_RESUMED       = "subscription.resumed";
    public static final String EVENT_SUBSCRIPTION_PAYMENT_FAILED = "subscription.payment_failed";
    public static final String EVENT_SUBSCRIPTION_RENEWED       = "subscription.renewed";
    public static final String EVENT_WITHDRAWAL_CREATED         = "withdrawal.created";
    public static final String EVENT_WITHDRAWAL_COMPLETED       = "withdrawal.completed";
    public static final String EVENT_WITHDRAWAL_FAILED          = "withdrawal.failed";
    public static final String EVENT_CHECKOUT_CREATED           = "checkout.created";
    public static final String EVENT_CHECKOUT_COMPLETED         = "checkout.completed";

    public static boolean verifySignature(String payload, String signature, String secret) {
        if (payload == null || payload.trim().isEmpty() ||
            signature == null || signature.trim().isEmpty() ||
            secret == null || secret.trim().isEmpty()) {
            return false;
        }
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            String expected = bytesToHex(hash);
            return constantTimeEquals(expected, stripPrefix(signature));
        } catch (Exception e) {
            return false;
        }
    }

    private static String stripPrefix(String sig) {
        return sig.startsWith("sha256=") ? sig.substring(7) : sig;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    private static boolean constantTimeEquals(String a, String b) {
        if (a.length() != b.length()) return false;
        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }
        return result == 0;
    }
}
