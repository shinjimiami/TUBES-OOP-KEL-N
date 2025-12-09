package nimonscooked.entity.item.kitchenutensil;

import nimonscooked.main.GamePanel;
import nimonscooked.interfaces.CookingDevice;
import nimonscooked.interfaces.Preparable;
import nimonscooked.entity.item.kitchenutensil.KitchenUtensils;
import java.util.ArrayList;
import java.util.List;

public class FryingPan extends KitchenUtensils implements CookingDevice {
    private final int MAX_CAPACITY = 1;
    private boolean isCooking = false;

    public FryingPan(GamePanel gp) {
        super(gp, new ArrayList<Preparable>());
        name = "Frying Pan";
        down1 = setup("/items/kitchen_utensils/frying_pan");

    }

    @Override
    public boolean isPortable() {
        return true;
    }

    @Override
    public int capacity() {
        return MAX_CAPACITY;
    }

    @Override
    public boolean canAccept(Preparable ingredient) {
        // FryingPan hanya terima ingredient yang bisa dimasak (CHOPPED Meat)
        // Reject RAW ingredients
        if (ingredient == null)
            return false;

        // Cek capacity dan status cooking
        if (contents.size() >= MAX_CAPACITY || isCooking)
            return false;

        // Validasi state: hanya terima ingredient yang siap dimasak (CHOPPED)
        // Reject RAW (harus dipotong dulu) dan reject COOKED/BURNED (sudah jadi)
        return ingredient.getState() == nimonscooked.enums.IngredientState.CHOPPED;
    }

    @Override
    public void addIngredient(Preparable ingredient) {
        System.out.println("[FRYINGPAN] DEBUG: addIngredient called");
        System.out.println("[FRYINGPAN] DEBUG: Ingredient: "
                + (ingredient != null ? ingredient.getName() + " (state: " + ingredient.getState() + ")" : "NULL"));
        System.out.println("[FRYINGPAN] DEBUG: canAccept: " + canAccept(ingredient));
        System.out.println("[FRYINGPAN] DEBUG: Contents size BEFORE: " + contents.size());

        if (canAccept(ingredient)) {
            contents.add(ingredient);
            System.out.println("[FRYINGPAN] ✓ Ingredient added! Contents size NOW: " + contents.size());
        } else {
            System.out.println("[FRYINGPAN] ✗ canAccept returned false - ingredient NOT added");
        }
    }

    @Override
    public List<Preparable> getContents() {
        return contents;
    }

    @Override
    public Preparable getFirstIngredient() {
        if (contents.isEmpty())
            return null;
        return contents.get(0);
    }

    @Override
    public void removeContents() {
        if (isCooking) {
            this.isCooking = false;
        }
        contents.clear();
    }

    @Override
    public void startCooking() {
        if (isEmpty() || isCooking) {
            return;
        }
        this.isCooking = true;
    }

    @Override
    public void stopCooking() {
        this.isCooking = false;
    }

    public boolean isEmpty() {
        return contents.size() == 0;
    }
}