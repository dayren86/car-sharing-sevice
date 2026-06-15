package mate.carsharing.exception;

public class RentalCompletedException extends RuntimeException {
    public RentalCompletedException(String message) {
        super(message);
    }
}
