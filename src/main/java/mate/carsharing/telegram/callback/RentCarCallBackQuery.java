package mate.carsharing.telegram.callback;

import lombok.RequiredArgsConstructor;
import mate.carsharing.dto.rental.RentalCreteDto;
import mate.carsharing.dto.rental.RentalDto;
import mate.carsharing.exception.EntityNotFoundException;
import mate.carsharing.mapper.RentalMapper;
import mate.carsharing.model.User;
import mate.carsharing.service.RentalService;
import mate.carsharing.service.UserService;
import mate.carsharing.telegram.CustomMessageForTelegram;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class RentCarCallBackQuery implements CallBackQuery {
    private static final Integer INDEX_CAR_ID = 1;
    private static final Integer INDEX_RENT_DAYS = 2;
    private final RentalService rentalService;
    private final UserService userService;
    private final RentalMapper rentalMapper;

    @Override
    public BotApiMethod<?> command(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();

        String[] parseCarIdAndRentDays = update.getCallbackQuery().getData().split("_");
        Long carId = Long.valueOf(parseCarIdAndRentDays[INDEX_CAR_ID]);
        Integer rentalDays = Integer.valueOf(parseCarIdAndRentDays[INDEX_RENT_DAYS]);

        RentalCreteDto rentalCreteDto = rentalMapper.toRentalCreateDto(carId, rentalDays);

        User user = userService.findByTelegramId(chatId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find user by chat id:" + chatId));

        RentalDto newRental = rentalService.createNewRental(user, rentalCreteDto);

        return SendMessage.builder()
                .chatId(chatId)
                .text(CustomMessageForTelegram.createRentalMessage(newRental))
                .build();
    }
}
