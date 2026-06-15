package mate.carsharing.repository;

import java.time.LocalDateTime;
import java.util.List;
import mate.carsharing.model.Rental;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    @Query("select r from Rental r join fetch r.car join fetch r.user where r.user.id = :userId")
    List<Rental> findAllByUserId(@Param("userId") Long userId);

    @Query("select r from Rental r where r.actualReturnDate IS NULL AND r.returnDate < :now "
            + "AND (r.lastNotificationSent IS NULL "
            + "OR r.lastNotificationSent <= :notificationBorder)")
    List<Rental> findOverdueRentals(@Param("now") LocalDateTime now,
                                    @Param("notificationBorder") LocalDateTime notificationBorder);

    @EntityGraph(attributePaths = {"car"})
    Rental findLastRentalByUserTelegramId(Long telegramId);
}
