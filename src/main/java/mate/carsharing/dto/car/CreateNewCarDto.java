package mate.carsharing.dto.car;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import mate.carsharing.model.Car;

@Getter
@Setter
public class CreateNewCarDto {
    @NotBlank
    private String model;
    @NotBlank
    @Size(min = 2, max = 50)
    private String brand;
    @NotNull
    private Integer inventory;
    @NotNull
    private BigDecimal dailyFee;
    @NotNull
    private Car.TypeCar typeCar;
}
