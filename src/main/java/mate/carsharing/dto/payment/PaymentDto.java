package mate.carsharing.dto.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import mate.carsharing.model.Payment;

@Getter
@Setter
public class PaymentDto {
    private Payment.PaymentStatus paymentStatus;
    private Payment.PaymentType paymentType;
    private Long rentalId;
    private String sessionUrl;
    private String sessionId;
    private LocalDateTime endSession;
    private BigDecimal amountToPay;
}
