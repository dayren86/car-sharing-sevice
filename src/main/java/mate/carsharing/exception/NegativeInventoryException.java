package mate.carsharing.exception;

public class NegativeInventoryException extends RuntimeException {
    public NegativeInventoryException(String message) {
        super(message);
    }
}
