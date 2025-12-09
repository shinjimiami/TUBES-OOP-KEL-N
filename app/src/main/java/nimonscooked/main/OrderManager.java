package nimonscooked.main;

import nimonscooked.entity.item.ingredient.Ingredient;
import nimonscooked.entity.order.Order;
import nimonscooked.entity.order.Recipe;
import nimonscooked.enums.IngredientState;
import nimonscooked.interfaces.Preparable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList; // Thread-safe list

public class OrderManager {
    private static OrderManager instance;

    // Gunakan CopyOnWriteArrayList untuk mencegah error saat menghapus order di
    // tengah loop
    private List<Order> activeOrders = new CopyOnWriteArrayList<>();
    private List<Recipe> recipes = new ArrayList<>();
    private Random random = new Random();
    private int orderCounter = 0;

    // Scoring system
    private int score = 0;
    private int completedOrders = 0;
    private int expiredOrders = 0;

    private OrderManager() {
        initBurgerRecipes();
    }

    public static OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    // Definisi Resep Sesuai Map Type C
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
        if (activeOrders.size() < 4) { // Max 4 order aktif
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
                expiredOrders++;
                System.out.println("ORDER EXPIRED: " + o.getRecipe().getName() + " (-10 points)");
                score = Math.max(0, score - 10); // Kurangi score tapi tidak boleh negatif
            }
        }
    }

    // === CORE LOGIC: Validasi Piring ===
    public boolean validateDish(List<Preparable> plateContents) {
        if (plateContents == null || plateContents.isEmpty())
            return false;

        for (Order order : activeOrders) {
            if (checkMatch(plateContents, order.getRecipe())) {
                // Hitung score berdasarkan waktu tersisa
                int earnedScore = calculateScore(order);
                score += earnedScore;
                completedOrders++;

                System.out.println("ORDER COMPLETED: " + order.getRecipe().getName()
                        + " +\" + earnedScore + \" points! Total: " + score);
                activeOrders.remove(order); // Hapus order yang selesai
                return true;
            }
        }
        return false;
    }

    private boolean checkMatch(List<Preparable> contents, Recipe recipe) {
        List<Recipe.Requirement> reqs = new ArrayList<>(recipe.getRequirements());

        // Cek jumlah bahan harus sama persis
        if (contents.size() != reqs.size())
            return false;

        // Cek setiap bahan di piring apakah ada di resep
        for (Preparable item : contents) {
            // Casting aman karena kita tahu Preparable diimplementasi Ingredient
            if (item instanceof Ingredient) {
                Ingredient ing = (Ingredient) item;
                boolean found = false;

                for (int i = 0; i < reqs.size(); i++) {
                    Recipe.Requirement r = reqs.get(i);
                    // Cek Nama (Case Insensitive) & State
                    if (ing.getName().equalsIgnoreCase(r.name) && ing.getState() == r.state) {
                        reqs.remove(i); // Tandai requirement ini sudah terpenuhi
                        found = true;
                        break;
                    }
                }
                if (!found)
                    return false; // Bahan ini tidak ada di resep atau status salah
            }
        }
        return true;
    }

    public List<Order> getActiveOrders() {
        return activeOrders;
    }

    // Calculate score based on remaining time
    private int calculateScore(Order order) {
        float timeRatio = order.getRemainingTime() / order.getDuration();
        int baseScore = 100;

        // Bonus berdasarkan kecepatan
        if (timeRatio > 0.75f) {
            return baseScore + 50; // Super fast: 150 points
        } else if (timeRatio > 0.5f) {
            return baseScore + 25; // Fast: 125 points
        } else if (timeRatio > 0.25f) {
            return baseScore; // Normal: 100 points
        } else {
            return baseScore - 25; // Slow: 75 points
        }
    }

    // Getters for UI
    public int getScore() {
        return score;
    }

    public int getCompletedOrders() {
        return completedOrders;
    }

    public int getExpiredOrders() {
        return expiredOrders;
    }
}