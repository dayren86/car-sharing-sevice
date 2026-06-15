package mate.carsharing.telegram.command;

import lombok.RequiredArgsConstructor;
import mate.carsharing.dto.rental.RentalDto;
import mate.carsharing.mapper.RentalMapper;
import mate.carsharing.model.Rental;
import mate.carsharing.service.RentalService;
import mate.carsharing.telegram.CustomKeyboardTelegram;
import mate.carsharing.telegram.CustomMessageForTelegram;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class ShowRentCarByUserCommand implements BotCommand {
    private final RentalService rentalService;
    private final RentalMapper rentalMapper;

    @Override
    public SendMessage command(Update update) {
        Long chatId = update.getMessage().getChatId();

        Rental actualRental = rentalService.getActualRentalByUser(chatId);

        RentalDto dto = rentalMapper.toDto(actualRental);

        return SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text(CustomMessageForTelegram.createRentalMessage(dto))
                .replyMarkup(actualRental.isActive()
                        ? CustomKeyboardTelegram.startKeyboard() :
                        CustomKeyboardTelegram.payCarKeyboard(actualRental.getId()))
                .build();
    }
}
