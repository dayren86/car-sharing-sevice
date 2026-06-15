package mate.carsharing.telegram.callback;

import lombok.RequiredArgsConstructor;
import mate.carsharing.dto.payment.PaymentDto;
import mate.carsharing.service.PaymentService;
import mate.carsharing.telegram.CustomMessageForTelegram;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class PayCallBackQuery implements CallBackQuery {
    private static final Integer INDEX_RENTAL_ID = 1;
    private final PaymentService paymentService;

    @Override
    public BotApiMethod<?> command(Update update) {
        Long rentalId =
                Long.valueOf(
                        update.getCallbackQuery().getData().split("_")[INDEX_RENTAL_ID]);
        PaymentDto paymentDto = paymentService.createPayment(rentalId);
        return SendMessage.builder()
                .chatId(update.getCallbackQuery().getMessage().getChatId())
                .text(CustomMessageForTelegram.createPaymentMessage(paymentDto))
                .build();
    }
}
