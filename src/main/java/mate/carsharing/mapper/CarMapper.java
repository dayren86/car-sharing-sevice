package mate.carsharing.mapper;

import mate.carsharing.config.MapperConfig;
import mate.carsharing.dto.car.CarDto;
import mate.carsharing.dto.car.CreateNewCarDto;
import mate.carsharing.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CarMapper {
    CarDto toCarDto(Car car);

    Car toCarFromCreate(CreateNewCarDto newCarDto);

    void updateCar(@MappingTarget Car car, Integer inventory);
}
