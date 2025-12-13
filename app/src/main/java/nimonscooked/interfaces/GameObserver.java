package nimonscooked.interfaces;

/**
 * OBSERVER PATTERN: Observer interface
 * Observers get notified when game events occur
 */
public interface GameObserver {
    void onOrderCompleted(int score);

    void onOrderExpired(int penalty);

    void onScoreChanged(int newScore);

    void onTimeUpdate(float timeRemaining);
}
