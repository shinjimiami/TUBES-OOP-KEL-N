package nimonscooked.entity.item.kitchenutensil;
// import entity.item.Item;

public class FryingPan extends KitchenUtensils implements CookingDevice{
    private final int MAX_CAPACITY = 1;

    public FryingPan(String id, float x, float y) {
        super(id, "FryingPan", x, y);
    }
}