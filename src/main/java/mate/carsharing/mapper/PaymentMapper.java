package mate.carsharing.mapper;

import com.stripe.model.checkout.Session;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import mate.carsharing.config.MapperConfig;
import mate.carsharing.dto.payment.PaymentDto;
import mate.carsharing.model.Payment;
import mate.carsharing.model.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface PaymentMapper {
    @Mapping(target = "rentalId", source = "rental.id")
    PaymentDto paymentToDto(Payment payment);

    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sessionUrl", source = "session.url")
    @Mapping(target = "sessionId", source = "session.id")
    @Mapping(target = "paymentStatus", source = "paymentStatus")
    Payment createNewPayment(Rental rental,
                             Payment.PaymentStatus paymentStatus,
                             Payment.PaymentType paymentType,
                             BigDecimal amountToPay,
                             Session session,
                             LocalDateTime endSession);
}
