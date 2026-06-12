package mate.carsharing.dto.rental;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RentalCreteDto {
    @NotNull
    private Long carId;
    @NotNull
    @Min(value = 1, message = "Min one day")
    private Integer rentalDays;
}
