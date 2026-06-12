package mate.carsharing.repository;

import java.util.List;
import java.util.Optional;
import mate.carsharing.model.Payment;
import mate.carsharing.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("select p from Payment p join fetch p.rental r where p.rental.id = :rentalId")
    Optional<Payment> findByRentalId(@Param("rentalId") Long rentalId);

    List<Payment> findByPaymentStatus(Payment.PaymentStatus paymentStatus);

    List<Payment> findByRentalUser(User user);
}
