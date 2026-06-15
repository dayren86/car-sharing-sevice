package mate.carsharing.dto.car;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import mate.carsharing.model.Car;

@Getter
@Setter
public class CarDto {
    private Long id;
    private String model;
    private String brand;
    private Integer inventory;
    private BigDecimal dailyFee;
    private Car.TypeCar typeCar;
}
