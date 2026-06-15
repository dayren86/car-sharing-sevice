package mate.carsharing.service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.carsharing.dto.rental.RentalCreteDto;
import mate.carsharing.dto.rental.RentalDto;
import mate.carsharing.exception.EntityNotFoundException;
import mate.carsharing.exception.NegativeInventoryException;
import mate.carsharing.exception.RentalCompletedException;
import mate.carsharing.mapper.RentalMapper;
import mate.carsharing.model.Car;
import mate.carsharing.model.Rental;
import mate.carsharing.model.User;
import mate.carsharing.repository.RentalRepository;
import mate.carsharing.telegram.CustomMessageForTelegram;
import mate.carsharing.telegram.TelegramNotificationService;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    private static final Integer CAR_UNIT = 1;
    private final UserService userService;
    private final CarService carService;
    private final TelegramNotificationService telegramNotificationService;
    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;

    @Override
    public RentalDto createNewRental(User user, RentalCreteDto rentalCreteDto) {
        User userById = userService.findUserById(user.getId());
        Car carById = carService.findCarById(rentalCreteDto.getCarId());

        if (userById.getRentals().stream().anyMatch(Rental::isActive)) {
            throw new RentalCompletedException(
                    "You already have an active rental. You can rent only 1 car at a time");
        }

        if (carById.getInventory() <= 0) {
            throw new NegativeInventoryException(
                    "It is impossible to rent a car, it is not available.");
        }

        LocalDateTime startDate = LocalDateTime.now();

        Rental newRental = rentalMapper.createNewRental(
                userById,
                carById,
                startDate,
                startDate.plusDays(rentalCreteDto.getRentalDays()));

        carService.updateCar(carById.getId(), carById.getInventory() - CAR_UNIT);
        rentalRepository.save(newRental);

        RentalDto rentalDto = rentalMapper.toDto(newRental);

        telegramNotificationService.sendNotification(userById.getTelegramId(),
                CustomMessageForTelegram.createRentalMessage(rentalDto));
        return rentalDto;
    }

    @Override
    public List<RentalDto> getAllRentalsByUser(User user) {
        return rentalRepository.findAllByUserId(user.getId())
                .stream()
                .map(rentalMapper::toDto)
                .toList();
    }

    @Override
    public RentalDto setReturnDate(Long id) {
        Rental rental = rentalRepository.findById(id).orElseThrow();
        if (!rental.isActive()) {
            throw new RentalCompletedException("Rental completed");
        }
        rental.setActualReturnDate(LocalDateTime.now());
        rental.setActive(false);
        carService.updateCar(rental.getCar().getId(), rental.getCar().getInventory() + CAR_UNIT);
        rentalRepository.save(rental);
        return rentalMapper.toDto(rental);
    }

    @Override
    public void checkOverdueRentals(LocalDateTime now, Integer interval) {
        List<Rental> overdueRentals = rentalRepository
                .findOverdueRentals(now, now.minusHours(interval));

        overdueRentals.forEach(rental -> {
            telegramNotificationService.sendNotification(
                    rental.getUser().getTelegramId(),
                    CustomMessageForTelegram.createRentalMessage(
                            rentalMapper.toDto(rental)));
            rental.setLastNotificationSent(now);
        });
    }

    @Override
    public Rental getActualRentalByUser(Long telegramId) {
        return rentalRepository.findLastRentalByUserTelegramId(telegramId);
    }

    @Override
    public Rental findRentalById(Long rentalId) {
        return rentalRepository.findById(rentalId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find rental"));
    }
}
