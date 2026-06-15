package mate.carsharing.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerServiceImpl {
    @Value("${rental.scheduler.message.repeat}")
    private int messageRepeatingHours;
    private final PaymentService paymentService;
    private final RentalService rentalService;

    @Scheduled(fixedDelayString = "${payment.scheduler.interval}")
    public void checkOverduePayment() {
        paymentService.checkOverduePayments();
    }

    @Scheduled(fixedDelayString = "${rental.scheduler.interval}")
    public void checkOverdueRentals() {
        rentalService.checkOverdueRentals(LocalDateTime.now(), messageRepeatingHours);
    }
}
