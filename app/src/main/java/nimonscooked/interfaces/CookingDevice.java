package nimonscooked.entity.item.kitchenutensil;

import nimonscooked.interfaces.Preparable;

public interface CookingDevice {
    boolean isPortable();
    int capacity();
    boolean canAccept(Preparable ingredient);

    void addIngredient(Preparable ingredient);
    void startCooking();
    void stopCooking();
    void removeContents();
}
