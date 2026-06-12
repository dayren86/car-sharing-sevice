package mate.carsharing.exception;

public class StripeWebhookException extends RuntimeException {
    public StripeWebhookException(String message) {
        super(message);
    }
}
