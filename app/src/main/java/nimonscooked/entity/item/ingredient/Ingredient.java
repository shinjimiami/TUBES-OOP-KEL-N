package nimonscooked.entity.item.ingredient;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

import nimonscooked.interfaces.Preparable;
import nimonscooked.entity.item.Item;
import nimonscooked.enums.IngredientState;
import nimonscooked.enums.IngredientType;
import nimonscooked.main.GamePanel;

public abstract class Ingredient extends Item implements Preparable {
    protected IngredientState currentState;
    protected final Map<IngredientState, BufferedImage> stateImages = new HashMap<>();
    protected String imageBasePath = null;
    protected final IngredientType type;

    public Ingredient(GamePanel gp, IngredientState currentState, IngredientType type) {
        super(gp);
        this.currentState = currentState;
        this.type = type;
    }

    public IngredientState getState() {
        return currentState;
    }

    public void setCurrentState(IngredientState state) {
        this.currentState = state;
        updateImageForState(state);
    }

    public void interact() {
        System.out.println("[INGREDIENT] Chef is interacting with Ingredient: " + this.name);
    }

    @Override
    public abstract boolean canBeChopped();

    @Override
    public abstract boolean canBeCooked();

    @Override
    public abstract boolean canBePlacedOnPlate();

    public void chop() {
        if (canBeChopped()) {
            setCurrentState(IngredientState.CHOPPED);
        }
    }

    public void cook() {
        System.out.println("[INGREDIENT.cook()] Called on " + getName() + " - currentState: " + currentState
                + ", canBeCooked: " + canBeCooked());
        if (canBeCooked()) {
            if (currentState == IngredientState.CHOPPED || currentState == IngredientState.COOKING) {
                System.out.println("[INGREDIENT.cook()] ✓ Changing state from " + currentState + " → COOKED");
                setCurrentState(IngredientState.COOKED);
                System.out.println("[INGREDIENT.cook()] ✓ State changed! New state: " + currentState);
            } else if (currentState == IngredientState.COOKED) {
                System.out.println("[INGREDIENT.cook()] ✓ Changing state from COOKED → BURNED");
                setCurrentState(IngredientState.BURNED);
            }
        } else {
            System.out.println("[INGREDIENT.cook()] ✗ Cannot cook - canBeCooked() returned false");
        }
    }

    protected BufferedImage loadImage(String path) {
        if (path == null)
            return null;
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is != null)
                return ImageIO.read(is);
        } catch (IOException ignored) {
        }
        return null;
    }

    protected void registerStateImages(String basePath) {
        this.imageBasePath = basePath;
        BufferedImage baseImg = loadImage(basePath + ".png");
        for (IngredientState s : IngredientState.values()) {
            String stateName = s.name().toLowerCase();
            BufferedImage img = loadImage(basePath + "_" + stateName + ".png");
            if (img == null)
                img = loadImage(basePath + "/" + stateName + ".png");
            if (img == null)
                img = baseImg;
            if (img != null)
                stateImages.put(s, img);
        }
        updateImageForState(this.currentState);
    }

    protected void updateImageForState(IngredientState state) {
        BufferedImage img = stateImages.get(state);
        if (img != null) {
            this.image = img;
        } else if (imageBasePath != null) {
            BufferedImage fallback = loadImage(imageBasePath + ".png");
            if (fallback != null)
                this.image = fallback;
        }
    }
}
