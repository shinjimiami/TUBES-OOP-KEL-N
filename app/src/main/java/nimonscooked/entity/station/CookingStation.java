package nimonscooked.entity.station;

import nimonscooked.entity.station.Station;
import nimonscooked.interfaces.Preparable;
import nimonscooked.interfaces.CookingDevice;
import nimonscooked.enums.IngredientState;
import nimonscooked.entity.item.kitchenutensil.FryingPan;
import nimonscooked.entity.Chef;
import nimonscooked.main.GamePanel;

import java.util.List;

// berfungsi untuk memasak ingredient yang dapat dimasak
// hanya bisa mulai apabila sudah ada fryingpan di cooking station
public class CookingStation extends Station {
    private final int cookingDuration = 15000;
    private final int burntDuration = 24000;
    private long cookingStartTime = 0;
    private booelan isCooking = false;

    public CookingStation(GamePanel gp) {
        super(gp);
        this.name = "Cooking Station";

        down1 = setup("/stations/cooking_station");
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
        if (this.containedItem == null && player.getHeldItem() instanceof CookingDevice) {
            super.placeItem(player.takeItem());
        } else if (player.getHeldItem() == null && this.containedItem != null) {
            player.placeItem(super.takeItem());
        }
    }

    // buat di gamepanel
    public void update(long currentTime){
        if(!isCooking || getContainedItem() == null) return;

        int elapsedTime = (int)currentTime - cookingStartTime;
        Preparable item = ((CookingDevice) getContainedItem()).getFirstIngredient();

        if(elapsedTime >= burntDuration){
            if(item.getState() != IngredientState.BURNED){
                item.cook();
                System.out.println("[COOKING] Item BURNED");
            }
        } else if (elapsedTime <= cookingDuration){
            if(item.getState() == IngredientState.CHOPPED){
                item.cook();
                System.out.println("[COOKING] Item COOKING");
            }
        }

    }

    public void startCooking(long currentTime){
        // placeholder...
    }

    public void stopCooking(){
        this.isCooking = false;
        ((CookingDevice) getContainedItem()).stopCooking();
    }
}
