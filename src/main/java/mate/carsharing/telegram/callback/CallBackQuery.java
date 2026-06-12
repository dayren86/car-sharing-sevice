package mate.carsharing.telegram.callback;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CallBackQuery {
    BotApiMethod<?> command(Update update);
}
