package nimonscooked.entity.order;

import nimonscooked.enums.IngredientState;
import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private String name;
    private List<Requirement> requirements;

    public Recipe(String name) {
        this.name = name;
        this.requirements = new ArrayList<>();
    }

    public void addRequirement(String ingredientName, IngredientState state) {
        requirements.add(new Requirement(ingredientName, state));
    }

    public String getName() { return name; }
    public List<Requirement> getRequirements() { return requirements; }

    public static class Requirement {
        public String name;
        public IngredientState state;

        public Requirement(String name, IngredientState state) {
            this.name = name;
            this.state = state;
        }
    }
}