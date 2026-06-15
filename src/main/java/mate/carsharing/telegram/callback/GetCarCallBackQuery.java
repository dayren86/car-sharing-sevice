package mate.carsharing.telegram.callback;

import lombok.RequiredArgsConstructor;
import mate.carsharing.dto.car.CarDto;
import mate.carsharing.service.CarService;
import mate.carsharing.telegram.CustomKeyboardTelegram;
import mate.carsharing.telegram.CustomMessageForTelegram;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class GetCarCallBackQuery implements CallBackQuery {
    private static final Integer PAGE_SIZE = 1;
    private final CarService carService;

    @Override
    public BotApiMethod<?> command(Update update) {
        int page = Integer.parseInt(update.getCallbackQuery().getData().substring(5));
        Page<CarDto> pageAllCars = carService.findAllCars(PageRequest.of(page, PAGE_SIZE));

        String responseMessageForCar =
                CustomMessageForTelegram.createResponseMessageForCar(
                        pageAllCars.getContent().get(0));

        return EditMessageText.builder()
                .chatId(update.getCallbackQuery().getMessage().getChatId())
                .messageId(update.getCallbackQuery().getMessage().getMessageId())
                .text(responseMessageForCar)
                .replyMarkup(CustomKeyboardTelegram.keyboardForCarList(pageAllCars))
                .build();
    }
}
