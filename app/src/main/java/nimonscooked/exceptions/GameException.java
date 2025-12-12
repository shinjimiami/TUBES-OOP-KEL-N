package nimonscooked.exceptions;

/**
 * Base exception class for all game-related exceptions
 * Following Exception Handling best practices
 */
public class GameException extends Exception {
    public GameException(String message) {
        super(message);
    }

    public GameException(String message, Throwable cause) {
        super(message, cause);
    }
}
