package nimonscooked.exceptions;

/**
 * Exception thrown when ingredient processing fails
 */
public class IngredientProcessingException extends GameException {
    public IngredientProcessingException(String ingredientName, String operation) {
        super("Failed to " + operation + " ingredient: " + ingredientName);
    }

    public IngredientProcessingException(String message) {
        super(message);
    }
}
