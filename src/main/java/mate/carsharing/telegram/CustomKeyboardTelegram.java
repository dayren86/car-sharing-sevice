package mate.carsharing.telegram;

import java.util.ArrayList;
import java.util.List;
import mate.carsharing.dto.car.CarDto;
import org.springframework.data.domain.Page;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

public class CustomKeyboardTelegram {
    public static ReplyKeyboardMarkup startKeyboard() {
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("List of cars");
        row1.add("Show rented cars");
        keyboard.add(row1);

        return ReplyKeyboardMarkup.builder()
                .keyboard(keyboard)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .selective(true)
                .build();
    }

    public static InlineKeyboardMarkup keyboardForCarList(Page<CarDto> pageAllCars) {
        int currentPage = pageAllCars.getNumber();

        InlineKeyboardRow row1 = new InlineKeyboardRow();

        if (pageAllCars.hasPrevious()) {
            row1.add(InlineKeyboardButton.builder()
                    .text("Prev")
                    .callbackData("find_" + (currentPage - 1))
                    .build());
        }

        row1.add(InlineKeyboardButton.builder()
                .text((currentPage + 1) + " / " + pageAllCars.getTotalPages())
                .callbackData("111")
                .build());

        if (pageAllCars.hasNext()) {
            row1.add(InlineKeyboardButton.builder()
                    .text("Next")
                    .callbackData("find_" + (currentPage + 1))
                    .build());
        }

        InlineKeyboardRow row2 = new InlineKeyboardRow();
        row2.add(InlineKeyboardButton.builder()
                .text("Rent Car")
                .callbackData("rentDays_" + pageAllCars.getContent().get(0).getId())
                .build());

        return InlineKeyboardMarkup
                .builder()
                .keyboardRow(row1)
                .keyboardRow(row2)
                .build();
    }

    public static InlineKeyboardMarkup selectRentDaysKeyboard(String data) {
        String carId = data.split("_")[1];
        List<InlineKeyboardRow> rows = new ArrayList<>();
        int day = 1;
        for (int i = 0; i < 3; i++) {
            InlineKeyboardRow row = new InlineKeyboardRow();
            for (int j = 0; j < 3; j++) {
                row.add(InlineKeyboardButton.builder()
                        .text(day + " day")
                        .callbackData("rent_" + carId + "_" + day)
                        .build());
                day++;
            }
            rows.add(row);
        }
        return InlineKeyboardMarkup
                .builder()
                .keyboard(rows)
                .build();
    }

    public static InlineKeyboardMarkup payCarKeyboard(Long rentalId) {
        InlineKeyboardRow row = new InlineKeyboardRow();
        row.add(InlineKeyboardButton.builder()
                .text("Pay")
                .callbackData("pay_" + rentalId)
                .build());

        return InlineKeyboardMarkup
                .builder()
                .keyboardRow(row)
                .build();
    }
}
