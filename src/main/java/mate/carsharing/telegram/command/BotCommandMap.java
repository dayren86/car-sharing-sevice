package mate.carsharing.telegram.command;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BotCommandMap {
    private final StartCommand startCommand;
    private final GetAllCarCommand getAllCarCommand;
    private final ShowRentCarByUserCommand showRentCarByUserCommand;

    public Map<String, BotCommand> getCommandMap() {
        Map<String, BotCommand> botCommandMap = new HashMap<>();
        botCommandMap.put("/start", startCommand);
        botCommandMap.put("List of cars", getAllCarCommand);
        botCommandMap.put("Show rented cars", showRentCarByUserCommand);

        return botCommandMap;
    }
}
