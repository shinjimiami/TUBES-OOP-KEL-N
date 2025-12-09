package nimonscooked.entity.item.dish;

import java.util.ArrayList;
import java.util.List;

import nimonscooked.main.GamePanel;
import nimonscooked.entity.item.Item;
import nimonscooked.enums.IngredientState;
import nimonscooked.interfaces.Preparable;

public class Dish extends Item {
    private final List<Preparable> components;

    public Dish(GamePanel gp) {
        super(gp);
        this.components = new ArrayList<>();
    }

    public boolean addComponent(Preparable component) {
        if(component == null){
            return false;
        }

        if(component.getState() == IngredientState.RAW || component.getState() == IngredientState.BURNED) {
            return false;
        }

        for(Preparable existingComponent : components){
            if(existingComponent.getName().equals(component.getName())){
                return false;
            }
        }

        return components.add(component);
    }

    public List<Preparable> getComponents() {
        return new ArrayList<>(components);
    }

    public boolean isEmpty() {
        return components.isEmpty();
    }

    public void clearDish(){
        components.clear();
    }
    
}
