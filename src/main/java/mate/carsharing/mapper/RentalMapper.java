package mate.carsharing.mapper;

import java.time.LocalDateTime;
import mate.carsharing.config.MapperConfig;
import mate.carsharing.dto.rental.RentalCreteDto;
import mate.carsharing.dto.rental.RentalDto;
import mate.carsharing.model.Car;
import mate.carsharing.model.Rental;
import mate.carsharing.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface RentalMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Rental createNewRental(User user,
                           Car car,
                           LocalDateTime rentalDate,
                           LocalDateTime returnDate);

    RentalDto toDto(Rental rental);

    RentalCreteDto toRentalCreateDto(Long carId, Integer rentalDays);
}
