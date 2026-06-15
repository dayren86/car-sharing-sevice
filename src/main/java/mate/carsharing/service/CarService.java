package mate.carsharing.service;

import mate.carsharing.dto.car.CarDto;
import mate.carsharing.dto.car.CreateNewCarDto;
import mate.carsharing.model.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CarService {
    Page<CarDto> findAllCars(Pageable pageable);

    Car findCarById(Long id);

    CarDto getCarsDetailInformation(Long id);

    CarDto createNewCar(CreateNewCarDto newCarDto);

    CarDto updateCar(Long id, Integer inventory);

    void deleteCarById(Long id);
}
