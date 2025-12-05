package nimonscooked.main;

import nimonscooked.entity.item.ingredient.Ingredient;
import nimonscooked.entity.order.Order;
import nimonscooked.entity.order.Recipe;
import nimonscooked.enums.IngredientState;
import nimonscooked.interfaces.Preparable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList; 

public class OrderManager {
    private List<Order> activeOrders = new CopyOnWriteArrayList<>();
    private List<Recipe> recipes = new ArrayList<>();
    private Random random = new Random();
    private int orderCounter = 0;

    public OrderManager() {
        initBurgerRecipes();
    }

    private void initBurgerRecipes() {
        // 1. Classic: Bun(RAW) + Meat(COOKED)
        Recipe r1 = new Recipe("Classic Burger");
        r1.addRequirement("Bun", IngredientState.RAW);
        r1.addRequirement("Meat", IngredientState.COOKED);
        recipes.add(r1);

        // 2. Cheese: Bun(RAW) + Meat(COOKED) + Cheese(CHOPPED)
        Recipe r2 = new Recipe("Cheeseburger");
        r2.addRequirement("Bun", IngredientState.RAW);
        r2.addRequirement("Meat", IngredientState.COOKED);
        r2.addRequirement("Cheese", IngredientState.CHOPPED);
        recipes.add(r2);

        // 3. BLT: Bun(RAW) + Meat(COOKED) + Lettuce(CHOPPED) + Tomato(CHOPPED)
        Recipe r3 = new Recipe("BLT Burger");
        r3.addRequirement("Bun", IngredientState.RAW);
        r3.addRequirement("Meat", IngredientState.COOKED);
        r3.addRequirement("Lettuce", IngredientState.CHOPPED);
        r3.addRequirement("Tomato", IngredientState.CHOPPED);
        recipes.add(r3);

        // 4. Deluxe: Bun(RAW) + Meat(COOKED) + Lettuce(CHOPPED) + Cheese(CHOPPED)
        Recipe r4 = new Recipe("Deluxe Burger");
        r4.addRequirement("Bun", IngredientState.RAW);
        r4.addRequirement("Meat", IngredientState.COOKED);
        r4.addRequirement("Lettuce", IngredientState.CHOPPED);
        r4.addRequirement("Cheese", IngredientState.CHOPPED);
        recipes.add(r4);
    }

    public void generateOrder() {
        if (activeOrders.size() < 4) { 
            Recipe r = recipes.get(random.nextInt(recipes.size()));
            activeOrders.add(new Order(++orderCounter, r, 60)); // Durasi 60 detik
            System.out.println("NEW ORDER: " + r.getName());
        }
    }

    public void update(float deltaTime) {
        for (Order o : activeOrders) {
            o.updateTimer(deltaTime);
            if (o.isExpired()) {
                activeOrders.remove(o);
                System.out.println("ORDER EXPIRED: " + o.getRecipe().getName());

            }
        }
    }

    // === CORE LOGIC: Validasi Piring ===
    public boolean validateDish(List<Preparable> plateContents) {
        if (plateContents == null || plateContents.isEmpty()) return false;

        for (Order order : activeOrders) {
            if (checkMatch(plateContents, order.getRecipe())) {
                System.out.println("ORDER COMPLETED: " + order.getRecipe().getName());
                activeOrders.remove(order);
                return true;
            }
        }
        return false;
    }

    private boolean checkMatch(List<Preparable> contents, Recipe recipe) {
        List<Recipe.Requirement> reqs = new ArrayList<>(recipe.getRequirements());

        if (contents.size() != reqs.size()) return false;

        for (Preparable item : contents) {
            if (item instanceof Ingredient) {
                Ingredient ing = (Ingredient) item;
                boolean found = false;

                for (int i = 0; i < reqs.size(); i++) {
                    Recipe.Requirement r = reqs.get(i);
                    if (ing.getName().equalsIgnoreCase(r.name) && ing.getCurrentState() == r.state) {
                        reqs.remove(i); 
                        found = true;
                        break;
                    }
                }
                if (!found) return false; 
            }
        }
        return true;
    }

    public List<Order> getActiveOrders() { return activeOrders; }
}