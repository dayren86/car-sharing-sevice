package mate.carsharing.telegram.callback;

import mate.carsharing.telegram.CustomKeyboardTelegram;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class RentDaysCallBackQuery implements CallBackQuery {
    @Override
    public BotApiMethod<?> command(Update update) {
        return SendMessage.builder()
                .chatId(update.getCallbackQuery().getMessage().getChatId())
                .text("Select rental days")
                .replyMarkup(CustomKeyboardTelegram
                        .selectRentDaysKeyboard(update.getCallbackQuery().getData()))
                .build();
    }
}
