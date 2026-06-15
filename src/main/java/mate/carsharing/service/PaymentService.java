package mate.carsharing.service;

import java.util.List;
import mate.carsharing.dto.payment.PaymentDto;
import mate.carsharing.model.Payment;
import mate.carsharing.model.User;

public interface PaymentService {
    List<PaymentDto> getPaymentByUser(User user);

    PaymentDto createPayment(Long rentalId);

    void paymentStatus(String orderId, Payment.PaymentStatus paymentStatus);

    public void checkOverduePayments();
}
