package mate.carsharing.telegram.command;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotCommand {
    BotApiMethod<?> command(Update update);
}
