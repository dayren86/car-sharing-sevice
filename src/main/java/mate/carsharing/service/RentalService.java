package mate.carsharing.service;

import java.time.LocalDateTime;
import java.util.List;
import mate.carsharing.dto.rental.RentalCreteDto;
import mate.carsharing.dto.rental.RentalDto;
import mate.carsharing.model.Rental;
import mate.carsharing.model.User;

public interface RentalService {
    RentalDto createNewRental(User user, RentalCreteDto rentalCreteDto);

    List<RentalDto> getAllRentalsByUser(User user);

    RentalDto setReturnDate(Long id);

    void checkOverdueRentals(LocalDateTime now, Integer interval);

    Rental getActualRentalByUser(Long telegramId);

    Rental findRentalById(Long rentalId);
}
