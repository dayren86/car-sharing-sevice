package mate.carsharing.telegram.command;

import mate.carsharing.telegram.CustomKeyboardTelegram;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class StartCommand implements BotCommand {
    @Override
    public SendMessage command(Update update) {
        return SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text(update.getMessage().getText())
                .replyMarkup(CustomKeyboardTelegram.startKeyboard())
                .build();
    }
}
