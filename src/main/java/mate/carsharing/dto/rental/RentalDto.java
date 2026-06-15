package mate.carsharing.dto.rental;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import mate.carsharing.dto.car.CarDto;

@Getter
@Setter
public class RentalDto {
    private Long id;
    private LocalDateTime rentalDate;
    private LocalDateTime returnDate;
    private LocalDateTime actualReturnDate;
    private boolean isActive;
    private CarDto car;
}
