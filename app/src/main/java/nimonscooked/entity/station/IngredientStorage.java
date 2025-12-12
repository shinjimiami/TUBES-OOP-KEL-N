package nimonscooked.entity.station;

import nimonscooked.main.GamePanel;
import nimonscooked.entity.Chef;
import nimonscooked.entity.item.Item;
import nimonscooked.entity.item.ingredient.Ingredient;
import nimonscooked.entity.item.ingredient.Bun;
import nimonscooked.entity.item.ingredient.Cheese;
import nimonscooked.entity.item.ingredient.Lettuce;
import nimonscooked.entity.item.ingredient.Meat;
import nimonscooked.entity.item.ingredient.Tomato;
import nimonscooked.enums.IngredientType;
import nimonscooked.enums.IngredientState;

// berfungsi untuk menyimpan ingredient yang diambil pemain
// ingredient ini bersifat unlimited
public class IngredientStorage extends Station {
    private final IngredientType type;
    private long lastTakenTime = 0; // timestamp when an ingredient was last taken (for UI feedback)

    public IngredientStorage(GamePanel gp, IngredientType type) {
        super(gp);
        this.type = type;
        this.name = type + " Storage";

        if (gp != null) {
            down1 = setup("/stations/ingredient_storage");
            int tilesWide = 1;
            int tilesHigh = 1;
            this.imageWidth = gp.tileSize * tilesWide;
            this.imageHeight = gp.tileSize * tilesHigh;
            solidArea = new java.awt.Rectangle(0, 0, this.imageWidth, this.imageHeight);
            solidAreaDefaultX = solidArea.x;
            solidAreaDefaultY = solidArea.y;
        }
    }

    // cache representative sprite to avoid recreating ingredients repeatedly
    private transient java.awt.image.BufferedImage representativeSprite = null;

    private java.awt.image.BufferedImage loadRepresentativeSprite() {
        if (representativeSprite != null)
            return representativeSprite;
        try {
            nimonscooked.entity.item.ingredient.Ingredient sample = creteIngredientByType(this.gp, this.type);
            if (sample != null) {
                representativeSprite = sample.getSprite();
            }
        } catch (Exception ignored) {
        }
        return representativeSprite;
    }

    @Override
    public void interact(Chef player) {
        Item heldItem = player.getInventory();

        // kalo tangan chef kosong, bisa ambil ingredient
        if (heldItem == null) {
            Ingredient newIngredient = creteIngredientByType(gp, this.type);

            if (newIngredient != null) {
                player.setInventory(newIngredient);
                this.lastTakenTime = System.currentTimeMillis();
                System.out.println("[STORAGE] take" + this.type);
            } else {
                System.out.println("[STORAGE] failed to take new ingredient");
            }
            return;
        }
        System.out.println("[STORAGE] player is already holding an item, can't take a new ingredient");
    }

    // factory pattern
    public Ingredient creteIngredientByType(GamePanel gp, IngredientType type) {
        final IngredientState defaultState = IngredientState.RAW;

        switch (type) {
            case BUN:
                return new Bun(gp, defaultState);
            case CHEESE:
                return new Cheese(gp, defaultState);
            case LETTUCE:
                return new Lettuce(gp, defaultState);
            case MEAT:
                return new Meat(gp, defaultState);
            case TOMATO:
                return new Tomato(gp, defaultState);
            default:
                return null;
        }
    }

    // For UI: expose ingredient type and last-taken timestamp
    public IngredientType getIngredientType() {
        return this.type;
    }

    public long getLastTakenTime() {
        return this.lastTakenTime;
    }

    // Provide a representative sprite for the storage (for overlay)
    public java.awt.image.BufferedImage getRepresentativeSprite() {
        return loadRepresentativeSprite();
    }
}
