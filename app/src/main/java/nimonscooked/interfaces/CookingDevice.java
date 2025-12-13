package nimonscooked.interfaces;

import nimonscooked.interfaces.Preparable;
import java.util.List;

public interface CookingDevice {
    boolean isPortable();
    int capacity();
    boolean canAccept(Preparable ingredient);

    void addIngredient(Preparable ingredient);
    List<Preparable> getContents();
    boolean isEmpty();
    Preparable getFirstIngredient();
    void startCooking();
    void stopCooking();
    void removeContents();
}
