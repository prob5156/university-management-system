package lab_tasks.problem2;

interface PaymentProcessor {
    void processPayment(double amount, String currency);
}

class ThirdPartyPaymentGateway {
    public void executeVendorCharge(String vendorMerchantId, double totalMoney, String currencyCode) {
        System.out.println("[3rd-Party Gateway] Transaction Success: " + totalMoney + " " + currencyCode + " (Merchant: " + vendorMerchantId + ")");
    }
}

class ThirdPartyPaymentAdapter implements PaymentProcessor {
    private final ThirdPartyPaymentGateway vendorGateway;
    private final String merchantId;

    public ThirdPartyPaymentAdapter(ThirdPartyPaymentGateway vendorGateway, String merchantId) {
        this.vendorGateway = vendorGateway;
        this.merchantId = merchantId;
    }

    @Override
    public void processPayment(double amount, String currency) {
        vendorGateway.executeVendorCharge(this.merchantId, amount, currency);
    }
}

class SecurePaymentProxy implements PaymentProcessor {
    private final PaymentProcessor realPaymentProcessor;
    private String userAuthToken;
    private static final String VALID_SECRET_TOKEN = "AUTH-TOKEN-SECURE-2207040";

    public SecurePaymentProxy(PaymentProcessor realPaymentProcessor, String userAuthToken) {
        this.realPaymentProcessor = realPaymentProcessor;
        this.userAuthToken = userAuthToken;
    }

    public void setUserAuthToken(String userAuthToken) {
        this.userAuthToken = userAuthToken;
    }

    private boolean isTokenValid() {
        return VALID_SECRET_TOKEN.equals(this.userAuthToken);
    }

    @Override
    public void processPayment(double amount, String currency) {
        System.out.println("[Security Proxy] Checking auth token: " + this.userAuthToken);
        if (!isTokenValid()) {
            System.out.println("[Security Proxy] ACCESS DENIED: Invalid or expired token!\n");
            return;
        }
        System.out.println("[Security Proxy] Token verified. Delegating payment...");
        realPaymentProcessor.processPayment(amount, currency);
        System.out.println();
    }
}

public class PaymentModuleDemo {
    public static void main(String[] args) {
        ThirdPartyPaymentGateway vendorGateway = new ThirdPartyPaymentGateway();
        PaymentProcessor paymentAdapter = new ThirdPartyPaymentAdapter(vendorGateway, "STORE-MERCHANT-99");

        SecurePaymentProxy secureProxy = new SecurePaymentProxy(paymentAdapter, "INVALID_TOKEN_123");
        secureProxy.processPayment(150.00, "USD");

        secureProxy.setUserAuthToken("AUTH-TOKEN-SECURE-2207040");
        secureProxy.processPayment(2500.00, "BDT");
    }
}
