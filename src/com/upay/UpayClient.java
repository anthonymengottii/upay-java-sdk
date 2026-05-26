package com.upay;

import com.upay.http.HttpClientWrapper;
import com.upay.resources.PaymentLinksResource;
import com.upay.resources.TransactionsResource;
import com.upay.resources.ProductsResource;
import com.upay.resources.CouponsResource;
import com.upay.resources.SubscriptionsResource;
import com.upay.resources.CheckoutsResource;
import com.upay.resources.OffersResource;
import com.upay.resources.WithdrawalsResource;
import com.upay.resources.WalletResource;
import com.upay.utils.WebhookUtils;

public class UpayClient {

    private final HttpClientWrapper http;

    public final PaymentLinksResource paymentLinks;
    public final TransactionsResource transactions;
    public final ProductsResource products;
    public final CouponsResource coupons;
    public final SubscriptionsResource subscriptions;
    public final CheckoutsResource checkouts;
    public final OffersResource offers;
    public final WithdrawalsResource withdrawals;
    public final WalletResource wallet;

    public UpayClient(String apiKey, String baseUrl, String version, int timeoutSeconds) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("API Key is required");
        }
        String url = baseUrl != null ? baseUrl : "https://upay-sistema-api.onrender.com";
        this.http = new HttpClientWrapper(apiKey, url, version, timeoutSeconds);

        this.paymentLinks  = new PaymentLinksResource(http);
        this.transactions  = new TransactionsResource(http);
        this.products      = new ProductsResource(http);
        this.coupons       = new CouponsResource(http);
        this.subscriptions = new SubscriptionsResource(http);
        this.checkouts     = new CheckoutsResource(http);
        this.offers        = new OffersResource(http);
        this.withdrawals   = new WithdrawalsResource(http);
        this.wallet        = new WalletResource(http);
    }

    public UpayClient(String apiKey) {
        this(apiKey, null, "v1", 30);
    }

    public boolean verifyWebhookSignature(String payload, String signature, String secret) {
        if (payload == null) {
            throw new IllegalArgumentException("payload must not be null");
        }
        if (signature == null) {
            throw new IllegalArgumentException("signature must not be null");
        }
        if (secret == null) {
            throw new IllegalArgumentException("secret must not be null");
        }
        return WebhookUtils.verifySignature(payload, signature, secret);
    }
}
