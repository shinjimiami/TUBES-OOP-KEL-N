package nimonscooked.exceptions;

/**
 * Exception thrown when invalid station interactions occur
 */
public class InvalidStationInteractionException extends GameException {
    public InvalidStationInteractionException(String stationName, String reason) {
        super("Invalid interaction with " + stationName + ": " + reason);
    }
}
