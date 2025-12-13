package nimonscooked.interfaces;

/**
 * OBSERVER PATTERN: Subject interface
 * Subjects maintain list of observers and notify them of changes
 */
public interface GameSubject {
    void addObserver(GameObserver observer);

    void removeObserver(GameObserver observer);

    void notifyObservers();
}
