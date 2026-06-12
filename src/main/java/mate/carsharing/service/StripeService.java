package mate.carsharing.service;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import java.math.BigDecimal;
import mate.carsharing.config.StripeConfig;
import mate.carsharing.exception.StripeWebhookException;
import mate.carsharing.model.Payment;
import mate.carsharing.model.Rental;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class StripeService {
    private final StripeConfig stripeConfig;
    private final PaymentService paymentService;

    public StripeService(StripeConfig stripeConfig, @Lazy PaymentService paymentService) {
        this.stripeConfig = stripeConfig;
        this.paymentService = paymentService;
    }

    public Session createSession(Rental rental, BigDecimal amount) {
        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/success")
                .setCancelUrl("http://localhost:8080/cancel")
                .setPaymentIntentData(
                        SessionCreateParams.PaymentIntentData.builder()
                                .putMetadata("rental_id", String.valueOf(rental.getId()))
                                .build()
                )
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(amount.longValue() * 100)
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName("Payment by rental id " + rental.getId())
                                                .build())
                                .build())
                        .build())
                .build();

        try {
            return stripeConfig.stripeClient().v1().checkout().sessions().create(params);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    public void getStripeEvent(String payload, String sigHeader) {
        String webhookSecret = stripeConfig.getWebhookSecret();

        if (webhookSecret == null || sigHeader == null) {
            throw new StripeWebhookException("Webhook secret or Sig Header empty");
        }

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            throw new StripeWebhookException("Stripe signature verification failed " + e);
        }

        getType(event);
    }

    private void getType(Event event) {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();

        if (dataObjectDeserializer.getObject().isEmpty()) {
            throw new StripeWebhookException("Failed to deserialize");
        }

        StripeObject stripeObject = dataObjectDeserializer.getObject().get();

        switch (event.getType()) {
            case "payment_intent.succeeded" -> {
                PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                paymentService.paymentStatus(
                        paymentIntent.getMetadata().get("rental_id"),
                        Payment.PaymentStatus.PAID);
            }

            case "payment_intent.canceled" -> {
                PaymentIntent canselIntent = (PaymentIntent) stripeObject;
                paymentService.paymentStatus(
                        canselIntent.getMetadata().get("rental_id"),
                        Payment.PaymentStatus.CANCELLED);
            }

            default -> throw new StripeWebhookException("Unhandled event type: " + event.getType());
        }
    }
}
