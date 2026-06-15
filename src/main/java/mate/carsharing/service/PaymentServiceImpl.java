package mate.carsharing.service;

import com.stripe.model.checkout.Session;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.carsharing.dto.payment.PaymentDto;
import mate.carsharing.exception.EntityNotFoundException;
import mate.carsharing.exception.RentalCompletedException;
import mate.carsharing.mapper.PaymentMapper;
import mate.carsharing.model.Payment;
import mate.carsharing.model.Rental;
import mate.carsharing.model.User;
import mate.carsharing.repository.PaymentRepository;
import mate.carsharing.telegram.CustomMessageForTelegram;
import mate.carsharing.telegram.TelegramNotificationService;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final RentalService rentalService;
    private final PaymentMapper paymentMapper;
    private final StripeService stripeService;
    private final TelegramNotificationService telegramNotificationService;

    @Override
    public List<PaymentDto> getPaymentByUser(User user) {
        return paymentRepository.findByRentalUser(user)
                .stream()
                .map(paymentMapper::paymentToDto)
                .toList();
    }

    @Override
    public PaymentDto createPayment(Long rentalId) {
        Rental rental = rentalService.findRentalById(rentalId);

        if (rental.getActualReturnDate() == null) {
            throw new RentalCompletedException("The car is not returned");
        }

        if (rental.getPayment() != null) {
            return paymentMapper.paymentToDto(rental.getPayment());
        }

        Session session = stripeService
                .createSession(rental, calculateAmount(rental));

        Payment payment = paymentMapper.createNewPayment(rental,
                Payment.PaymentStatus.PENDING,
                Payment.PaymentType.PAYMENT,
                calculateAmount(rental),
                session,
                sessionEndTime(session.getExpiresAt()));

        paymentRepository.save(payment);

        return paymentMapper.paymentToDto(payment);
    }

    @Override
    public void paymentStatus(String rentalId, Payment.PaymentStatus paymentStatus) {
        Payment payment = findPayment(Long.valueOf(rentalId));

        payment.setPaymentStatus(paymentStatus);
        paymentRepository.save(payment);

        telegramNotificationService.sendNotification(
                payment.getRental().getUser().getTelegramId(),
                "Payment for car "
                        + payment.getRental().getCar().getModel()
                        + " changed to "
                        + paymentStatus);
    }

    @Override
    public void checkOverduePayments() {
        List<Payment> expiredPayments =
                paymentRepository.findByPaymentStatus(Payment.PaymentStatus.PENDING)
                        .stream()
                        .filter(payment -> payment.getEndSession() != null
                                && payment.getEndSession().isBefore(LocalDateTime.now()))
                        .toList();

        expiredPayments.forEach(payment ->
                payment.setPaymentStatus(Payment.PaymentStatus.EXPIRED));

        paymentRepository.saveAll(expiredPayments);

        expiredPayments.forEach(payment ->
                telegramNotificationService.sendNotification(
                        payment.getRental().getUser().getTelegramId(),
                        CustomMessageForTelegram.createPaymentMessage(
                                paymentMapper.paymentToDto(payment)))
        );
    }

    private LocalDateTime sessionEndTime(Long expiresAtRaw) {
        Instant instant = Instant.ofEpochSecond(expiresAtRaw);

        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    private Payment findPayment(Long rentalID) {
        return paymentRepository.findByRentalId(rentalID)
                .orElseThrow(() -> new EntityNotFoundException("Can't find payment"));
    }

    private BigDecimal calculateAmount(Rental rental) {
        long rentalDays = ChronoUnit.DAYS.between(
                rental.getRentalDate().toLocalDate(),
                rental.getActualReturnDate().toLocalDate()
        );

        if (rentalDays == 0) {
            rentalDays = 1;
        }

        return rental.getCar().getDailyFee().multiply(BigDecimal.valueOf(rentalDays));
    }
}
