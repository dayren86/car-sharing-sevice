package mate.carsharing.conroller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.carsharing.dto.payment.PaymentDto;
import mate.carsharing.model.User;
import mate.carsharing.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Payment Api")
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(summary = "Show all payments by user")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'CUSTOMER')")
    @GetMapping
    public List<PaymentDto> getPaymentByUser(@AuthenticationPrincipal User user) {
        return paymentService.getPaymentByUser(user);
    }

    @Operation(summary = "Create payment by rental id")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'CUSTOMER')")
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentDto createPayment(@PathVariable Long id) {
        return paymentService.createPayment(id);
    }
}
