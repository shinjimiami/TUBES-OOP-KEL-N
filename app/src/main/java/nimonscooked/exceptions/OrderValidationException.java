package nimonscooked.exceptions;

/**
 * Exception thrown when order validation fails
 */
public class OrderValidationException extends GameException {
    public OrderValidationException(String message) {
        super(message);
    }
}
