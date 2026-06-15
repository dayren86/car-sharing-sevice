package mate.carsharing.service;

import lombok.RequiredArgsConstructor;
import mate.carsharing.dto.car.CarDto;
import mate.carsharing.dto.car.CreateNewCarDto;
import mate.carsharing.exception.EntityNotFoundException;
import mate.carsharing.exception.NegativeInventoryException;
import mate.carsharing.mapper.CarMapper;
import mate.carsharing.model.Car;
import mate.carsharing.repository.CarRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Override
    public Page<CarDto> findAllCars(Pageable pageable) {
        return carRepository.findAll(pageable)
                .map(carMapper::toCarDto);
    }

    @Override
    public Car findCarById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cant find car by id: " + id));
    }

    @Override
    public CarDto getCarsDetailInformation(Long id) {
        Car carById = findCarById(id);
        return carMapper.toCarDto(carById);
    }

    @Override
    public CarDto createNewCar(CreateNewCarDto newCarDto) {
        Car car = carMapper.toCarFromCreate(newCarDto);
        carRepository.save(car);
        return carMapper.toCarDto(car);
    }

    @Override
    public CarDto updateCar(Long id, Integer inventory) {
        Car carById = findCarById(id);
        if (inventory < 0) {
            throw new NegativeInventoryException("");
        }
        carMapper.updateCar(carById, inventory);
        carRepository.save(carById);
        return carMapper.toCarDto(carById);
    }

    @Override
    public void deleteCarById(Long id) {
        carRepository.deleteById(id);
    }
}
