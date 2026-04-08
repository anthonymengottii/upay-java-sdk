<p align="center">
  <picture>
    <source media="(prefers-color-scheme: dark)" srcset="logo/light.png">
    <source media="(prefers-color-scheme: light)" srcset="logo/dark.png">
    <img src="logo/dark.png" alt="Upay" height="60">
  </picture>
</p>

# Upay Java SDK

SDK oficial da Upay para Java - integração fácil com a API de pagamentos Upay.

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java 17+](https://img.shields.io/badge/java-17+-blue.svg)](https://www.java.com/downloads)

## 📦 Requisitos

- Java 17+
- Maven 3.8+
- Acesso à API Upay e uma API Key válida

## 🚀 Instalação (projeto Maven)

```bash
git clone https://github.com/anthonymengottii/upay-java-sdk.git
cd upay-java-sdk
mvn compile
```

## 🔑 Configuração básica

```java
import com.upay.sdk.UpayClient;

String apiKey = "SUA_API_KEY_AQUI";
String baseUrl = "https://upay-sistema-api.onrender.com"; // ou http://localhost:3001 em dev

UpayClient upay = new UpayClient(apiKey, baseUrl, "v1", 30);
```

## 💳 Payment Links

### Criar Payment Link

```java
import com.fasterxml.jackson.databind.JsonNode;

Map<String, Object> options = new HashMap<>();
options.put("description", "Produto Premium");
options.put("status", "ACTIVE");

JsonNode link = upay.paymentLinks.create(
    "Produto Premium",
    10000, // R$ 100,00 em centavos
    options
);

System.out.println("ID: " + link.get("id").asText());
System.out.println("Slug: " + link.get("slug").asText());
```

### Listar Payment Links

```java
JsonNode links = upay.paymentLinks.list(1, 10);
System.out.println(links.toPrettyString());
```

## 📦 Produtos

```java
JsonNode products = upay.products.list(1, 10);
System.out.println(products.toPrettyString());
```

## 💳 Transações

```java
JsonNode transactions = upay.transactions.list(1, 10);
System.out.println(transactions.toPrettyString());
```

## 🎫 Cupons

```java
JsonNode validation = upay.coupons.validate("CUPOM10", 10000);
System.out.println(validation.toPrettyString());
```

## 🌐 Webhooks

```java
import com.upay.sdk.utils.WebhookUtils;

String payload = requestBody; // JSON recebido
String signature = request.getHeader("X-Upay-Signature");
String secret = "SEU_WEBHOOK_SECRET";

boolean valid = WebhookUtils.verifySignature(payload, signature, secret);
if (!valid) {
    response.setStatus(401);
    return;
}

// processar evento
```

## 🧪 Exemplo pronto (Main)

Já existe um exemplo em `src/main/java/com/upay/sdk/examples/Main.java` que:

- Usa sua API Key de teste
- Aponta para `http://localhost:3001`
- Lista Payment Links e Produtos

Para executar:

```bash
mvn compile
mvn exec:java
```

## 📝 Tratamento de erros

Todos os erros HTTP são encapsulados em `UpayException`:

```java
import com.upay.sdk.utils.UpayException;

try {
    JsonNode links = upay.paymentLinks.list(1, 10);
} catch (UpayException e) {
    System.err.println("Erro na API Upay: " + e.getMessage());
    System.err.println("Status: " + e.getStatus());
    System.err.println("Code: " + e.getCode());
}
```

## 🔗 Links Úteis

- [Documentação da API](https://docs.upaybr.com)
- [Dashboard](https://app.upaybr.com)
- [Suporte](mailto:suporte@upaybr.com)
- [Repositório](https://github.com/anthonymengottii/upay-java-sdk)
- [Issues](https://github.com/anthonymengottii/upay-java-sdk/issues)

## 📄 Licença

MIT

## 🤝 Contribuindo

Contribuições são bem-vindas! Veja as [issues abertas](https://github.com/anthonymengottii/upay-java-sdk/issues) para sugestões.
