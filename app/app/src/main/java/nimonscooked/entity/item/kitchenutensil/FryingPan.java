package nimonscooked.entity.item.kitchenutensil;

import nimonscooked.entity.item.kitchenutensil.CookingDevice;
import nimonscooked.interfaces.Preparable;
import nimonscooked.entity.item.kitchenutensil.KitchenUtensils;
import java.util.ArrayList;
import java.util.List;

public class FryingPan extends KitchenUtensils implements CookingDevice{
    private final int MAX_CAPACITY = 1;
    private boolean isCooking = false;

    public FryingPan(String id, float x, float y) {
        super(id, "FryingPan", x, y, new ArrayList<Preparable>());
    }

    @Override
    public boolean isPortable(){
        return true;
    }

    @Override
    public int capacity(){
        return MAX_CAPACITY;
    }

    @Override
    public boolean canAccept(Preparable ingredient){
        return contents.size() < MAX_CAPACITY && !isCooking;
    }

    @Override
    public void addIngredient(Preparable ingredient){
        if(canAccept(ingredient)){
            contents.add(ingredient);
        }
    }

    @Override
    public List<Preparable> getContents(){
        return contents;
    }


    @Override
    public void removeContents(){
        if(isCooking){
            this.isCooking = false;
        }
        contents.clear();
    }

    @Override
    public void startCooking(){
        if(isEmpty() || isCooking){
            return;
        }
        this.isCooking = true;
    }

    @Override
    public void stopCooking(){
        this.isCooking = false;
    }

    public boolean isEmpty(){
        return contents.size() == 0;
    }
}