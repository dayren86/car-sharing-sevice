package mate.carsharing.conroller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.carsharing.dto.car.CarDto;
import mate.carsharing.dto.car.CreateNewCarDto;
import mate.carsharing.service.CarService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Cars api")
@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @Operation(summary = "Output all machines")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'CUSTOMER')")
    @GetMapping
    public Page<CarDto> getAllCars(Pageable pageable) {
        return carService.findAllCars(pageable);
    }

    @Operation(summary = "Detail car information by id")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'CUSTOMER')")
    @GetMapping("/{id}")
    public CarDto getCarsDetailInformation(@PathVariable Long id) {
        return carService.getCarsDetailInformation(id);
    }

    @Operation(summary = "Add new car")
    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarDto addNewCar(@RequestBody @Valid CreateNewCarDto newCarDto) {
        return carService.createNewCar(newCarDto);
    }

    @Operation(summary = "Update car information")
    @PreAuthorize("hasAuthority('MANAGER')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CarDto updateCarInventory(@PathVariable Long id,
                             @RequestBody Integer inventory) {
        return carService.updateCar(id, inventory);
    }

    @Operation(summary = "Delete car")
    @PreAuthorize("hasAuthority('MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable Long id) {
        carService.deleteCarById(id);
    }
}
