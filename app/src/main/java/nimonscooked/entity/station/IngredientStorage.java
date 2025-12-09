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

    public IngredientStorage(GamePanel gp, IngredientType type) {
        super(gp);
        this.type = type;
        this.name = type + " Storage";


        down1 = setup("/stations/ingredient_storage");
        int tilesWide = 1;
        int tilesHigh = 1;
        this.imageWidth = gp.tileSize * tilesWide;
        this.imageHeight = gp.tileSize * tilesHigh;
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = this.imageWidth;
        solidArea.height = this.imageHeight;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
    
	@Override
	public void interact(Chef player) {
		Item heldItem = player.getHeldItem();

        // kalo tangan chef kosong, bisa ambil ingredient
        if(heldItem == null){
            Ingredient newIngredient = creteIngredientByType(gp, this.type);

            if(newIngredient != null){
                player.placeItem(newIngredient);
                System.out.println("[STORAGE] take" + this.type);
            } else{
                System.out.println("[STORAGE] failed to take new ingredient");
            }
            return;
        }
        System.out.println("[STORAGE] player is already holding an item, can't take a new ingredient");
	}    

    // factory pattern
    public Ingredient creteIngredientByType(GamePanel gp, IngredientType type){
        final IngredientState defaultState = IngredientState.RAW;

        switch(type){
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
}
