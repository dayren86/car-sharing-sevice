package mate.carsharing.telegram;

import mate.carsharing.dto.car.CarDto;
import mate.carsharing.dto.payment.PaymentDto;
import mate.carsharing.dto.rental.RentalDto;

public class CustomMessageForTelegram {
    public static String createResponseMessageForCar(CarDto carDto) {
        return new StringBuilder()
                .append("Brand: ").append(carDto.getBrand()).append("\n")
                .append("Model: ").append(carDto.getModel()).append("\n")
                .append("Type: ").append(carDto.getTypeCar()).append("\n")
                .append("Daile fee: ").append(carDto.getDailyFee()).append("\n")
                .append("Inventory: ").append(carDto.getInventory())
                .toString();
    }

    public static String createRentalMessage(RentalDto rentalDto) {
        return new StringBuilder()
                .append("Brand: ").append(rentalDto.getCar().getBrand()).append("\n")
                .append("Model: ").append(rentalDto.getCar().getModel()).append("\n")
                .append("Rental Date: ").append(rentalDto.getRentalDate().toLocalDate())
                .append("\n")
                .append("Return Date: ").append(rentalDto.getReturnDate().toLocalDate())
                .append("\n")
                .append("Actual Return Date: ")
                .append(rentalDto.isActive() ? "Rental is active"
                        : rentalDto.getActualReturnDate().toLocalDate())
                .append("\n")
                .toString();
    }

    public static String createPaymentMessage(PaymentDto paymentDto) {
        return new StringBuilder()
                .append("Payment status: ").append(paymentDto.getPaymentStatus()).append("\n")
                .append("Payment type: ").append(paymentDto.getPaymentType()).append("\n")
                .append("Session url: ").append(paymentDto.getSessionUrl()).append("\n")
                .append("Session id: ").append(paymentDto.getSessionId()).append("\n")
                .append("End Session: ").append(paymentDto.getEndSession()).append("\n")
                .append("Amount to pay: ").append(paymentDto.getAmountToPay())
                .toString();
    }
}
