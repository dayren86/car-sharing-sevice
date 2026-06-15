package mate.carsharing.conroller;

import lombok.RequiredArgsConstructor;
import mate.carsharing.service.StripeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stripe")
@RequiredArgsConstructor
public class StripeWebhookController {
    private final StripeService stripeService;

    @PostMapping
    public ResponseEntity<Void> getStatusPayment(@RequestBody String payload,
                                 @RequestHeader("Stripe-Signature") String sigHeader) {
        stripeService.getStripeEvent(payload, sigHeader);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
