package nimonscooked.util;

import nimonscooked.entity.item.kitchenutensil.Plate;

/**
 * BUILDER PATTERN: Builder for constructing complex dishes on plates
 * Allows step-by-step construction of dishes with validation
 */
public class DishBuilder {
    private Plate plate;
    private boolean hasError = false;
    private String errorMessage = "";

    public DishBuilder(Plate plate) {
        this.plate = plate;
    }

    /**
     * Add bun to the dish
     */
    public DishBuilder addBun() {
        try {
            // Logic to add bun
            System.out.println("[DISH BUILDER] Added bun");
        } catch (Exception e) {
            hasError = true;
            errorMessage = "Failed to add bun: " + e.getMessage();
        }
        return this;
    }

    /**
     * Add meat to the dish
     */
    public DishBuilder addMeat() {
        try {
            System.out.println("[DISH BUILDER] Added meat");
        } catch (Exception e) {
            hasError = true;
            errorMessage = "Failed to add meat: " + e.getMessage();
        }
        return this;
    }

    /**
     * Add cheese to the dish
     */
    public DishBuilder addCheese() {
        try {
            System.out.println("[DISH BUILDER] Added cheese");
        } catch (Exception e) {
            hasError = true;
            errorMessage = "Failed to add cheese: " + e.getMessage();
        }
        return this;
    }

    /**
     * Add lettuce to the dish
     */
    public DishBuilder addLettuce() {
        try {
            System.out.println("[DISH BUILDER] Added lettuce");
        } catch (Exception e) {
            hasError = true;
            errorMessage = "Failed to add lettuce: " + e.getMessage();
        }
        return this;
    }

    /**
     * Add tomato to the dish
     */
    public DishBuilder addTomato() {
        try {
            System.out.println("[DISH BUILDER] Added tomato");
        } catch (Exception e) {
            hasError = true;
            errorMessage = "Failed to add tomato: " + e.getMessage();
        }
        return this;
    }

    /**
     * Build the final dish
     */
    public Plate build() {
        if (hasError) {
            System.err.println("[DISH BUILDER] Build failed: " + errorMessage);
            return null;
        }
        System.out.println("[DISH BUILDER] Dish built successfully!");
        return plate;
    }

    /**
     * Reset the builder for a new dish
     */
    public DishBuilder reset() {
        hasError = false;
        errorMessage = "";
        return this;
    }

    /**
     * Validate the dish before building
     */
    public boolean validate() {
        if (plate == null) {
            hasError = true;
            errorMessage = "No plate provided";
            return false;
        }
        return !hasError;
    }
}
