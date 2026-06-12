package mate.carsharing.telegram;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import mate.carsharing.service.UserService;
import mate.carsharing.telegram.callback.CallBackQuery;
import mate.carsharing.telegram.callback.CallBackQueryMap;
import mate.carsharing.telegram.command.BotCommand;
import mate.carsharing.telegram.command.BotCommandMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@RequiredArgsConstructor
public class TelegramBot implements SpringLongPollingBot,
        LongPollingSingleThreadUpdateConsumer {
    private static final Integer INDEX_CALL_BACK_COMMAND = 0;
    @Value("${telegram.token}")
    private String token;
    private final TelegramClient telegramClient;
    private final UserService userService;
    private final BotCommandMap botCommandMap;
    private final CallBackQueryMap callBackQueryMap;

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        Long chatId = getChatId(update);

        if (chatId != null && userService.findByTelegramId(chatId).isEmpty()) {
            String returnUserPassword = userService.registerUserFromTelegram(update);
            sendTextMessage(SendMessage.builder()
                        .chatId(chatId)
                        .text("Your password: " + returnUserPassword)
                        .build());
        }

        if (update.hasMessage()) {
            Map<String, BotCommand> commandMap = botCommandMap.getCommandMap();
            BotCommand botCommand = commandMap.get(update.getMessage().getText());

            sendTextMessage(botCommand.command(update));
        } else if (update.hasCallbackQuery()) {
            String callBackCommand =
                    update.getCallbackQuery().getData().split("_")[INDEX_CALL_BACK_COMMAND];
            CallBackQuery callBackQuery = callBackQueryMap.getCallBackMap().get(callBackCommand);

            sendTextMessage(callBackQuery.command(update));
        }
    }

    private Long getChatId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getChatId();
        }
        if (update.hasCallbackQuery() && update.getCallbackQuery().getMessage() != null) {
            return update.getCallbackQuery().getMessage().getChatId();
        }
        return null;
    }

    private void sendTextMessage(BotApiMethod<?> botApiMethod) {
        try {
            telegramClient.execute(botApiMethod);
        } catch (TelegramApiException e) {
            throw new RuntimeException("" + e);
        }
    }
}
