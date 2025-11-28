package nimonscooked.entity.item.dish;

import nimonscooked.entity.item.ingredient.Preparable;

public abstract class Dish extends Item implements Preparable{
    protected List<Preparable> components;

    public Dish(String id, String name, float x, float y) {
        super(id, name, x, y);
    }
    
}
