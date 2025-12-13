package nimonscooked.util;

import nimonscooked.interfaces.GameObserver;

/**
 * OBSERVER PATTERN: Concrete Observer
 * Observes and logs game statistics
 */
public class StatisticsObserver implements GameObserver {
    private int totalOrdersCompleted = 0;
    private int totalOrdersExpired = 0;
    private int highestScore = 0;

    @Override
    public void onOrderCompleted(int score) {
        totalOrdersCompleted++;
        System.out.println("[STATS OBSERVER] Order completed! Total: " + totalOrdersCompleted);
    }

    @Override
    public void onOrderExpired(int penalty) {
        totalOrdersExpired++;
        System.out.println("[STATS OBSERVER] Order expired! Total expired: " + totalOrdersExpired);
    }

    @Override
    public void onScoreChanged(int newScore) {
        if (newScore > highestScore) {
            highestScore = newScore;
            System.out.println("[STATS OBSERVER] New high score: " + highestScore);
        }
    }

    @Override
    public void onTimeUpdate(float timeRemaining) {
        // Optional: Log time warnings
        if (timeRemaining == 60.0f) {
            System.out.println("[STATS OBSERVER] ⚠️ Only 1 minute remaining!");
        } else if (timeRemaining == 30.0f) {
            System.out.println("[STATS OBSERVER] ⚠️ Only 30 seconds remaining!");
        }
    }

    public int getTotalOrdersCompleted() {
        return totalOrdersCompleted;
    }

    public int getTotalOrdersExpired() {
        return totalOrdersExpired;
    }

    public int getHighestScore() {
        return highestScore;
    }
}
