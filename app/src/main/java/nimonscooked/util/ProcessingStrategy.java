package nimonscooked.util;

/**
 * STRATEGY PATTERN: Strategy interface for ingredient processing
 * Different strategies for processing ingredients (cutting, cooking, etc.)
 */
public interface ProcessingStrategy {
    /**
     * Process the ingredient
     * 
     * @param durationMs Time taken to process
     * @return true if processing is complete
     */
    boolean process(int durationMs);

    /**
     * Get the type of processing
     */
    String getProcessType();

    /**
     * Get required duration in milliseconds
     */
    int getRequiredDuration();

    /**
     * Reset processing progress
     */
    void reset();
}
