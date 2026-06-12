package mate.carsharing.telegram.command;

import lombok.RequiredArgsConstructor;
import mate.carsharing.dto.car.CarDto;
import mate.carsharing.service.CarService;
import mate.carsharing.telegram.CustomKeyboardTelegram;
import mate.carsharing.telegram.CustomMessageForTelegram;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class GetAllCarCommand implements BotCommand {
    private final CarService carService;

    @Override
    public SendMessage command(Update update) {
        Page<CarDto> pageAllCars = carService.findAllCars(PageRequest.ofSize(1));

        String responseMessageForCar =
                CustomMessageForTelegram.createResponseMessageForCar(
                        pageAllCars.getContent().get(0));

        return SendMessage.builder()
                .chatId(update.getMessage().getChatId().toString())
                .text(responseMessageForCar)
                .replyMarkup(CustomKeyboardTelegram.keyboardForCarList(pageAllCars))
                .build();
    }
}
