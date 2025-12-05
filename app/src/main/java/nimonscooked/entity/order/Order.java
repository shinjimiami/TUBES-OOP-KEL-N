package nimonscooked.entity.order;

public class Order {
    private int id;
    private Recipe recipe;
    private int duration; // Total durasi awal (detik)
    private float timeLeft; // Waktu tersisa (float agar support deltaTime presisi)
    private int reward = 100;

    public Order(int id, Recipe recipe, int duration) {
        this.id = id;
        this.recipe = recipe;
        this.duration = duration;
        this.timeLeft = duration;
    }


    public void updateTimer(float deltaTime) {
        if (timeLeft > 0) {
            timeLeft -= deltaTime;
        }
    }

    public boolean isExpired() {
        return timeLeft <= 0;
    }

    public int getId() { return id; }
    public Recipe getRecipe() { return recipe; }
    public int getReward() { return reward; }
}