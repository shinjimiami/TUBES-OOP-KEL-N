package nimonscooked.entity.station;

import nimonscooked.entity.station.Station;
import nimonscooked.interfaces.Preparable;
import nimonscooked.enums.IngredientState;
import nimonscooked.entity.item.kitchenutensil.FryingPan;
import nimonscooked.entity.station.ChefPlayer;
import javax.swing.Timer;
import java.util.List;

// berfungsi untuk memasak ingredient yang dapat dimasak
// hanya bisa mulai apabila sudah ada fryingpan di cooking station
public class CookingStation extends Station {
    private final int cookingDuration = 15000;
    private final int burntDuration = 25000;
    private final int interval = 1000; //buat UI nya nanti
    
    private Timer cookingTimer;
    private int cookingStartTime = 0;
    private Preparable currentItem;

    public CookingStation(String id, float x, float y) {
        super(id, "Cooking Station", x, y);
    }

    @Override
    public void interact(ChefPlayer chefPlayer) {
        if(this.containedItem == null && chefPlayer.getHeldItem() != null){
            super.placeItem(chefPlayer.takeItem());
            startCookingTimer();
        } else if(chefPlayer.getHeldItem() == null && this.containedItem != null){
            stopCookingTimer();
            chefPlayer.placeItem(this.takeItem());
        }
    }

    private void startCookingTimer() {
        stopCookingTimer();

        if(!(this.containedItem instanceof FryingPan)){
            return;
        }

        FryingPan fryingPan = (FryingPan) this.containedItem;
        List<Preparable> contents = fryingPan.getContents();
        if(contents.isEmpty()){
            return;
        }
        Preparable itemToCook = contents.get(0);

        if(itemToCook == null || !itemToCook.canBeCooked()){
            return;
        }

        this.currentItem = itemToCook;

        cookingTimer = new Timer(interval, e -> {
            cookingStartTime += interval;

            if(cookingStartTime >= burntDuration){
                currentItem.cook(); 
                stopCookingTimer();
            } else if(cookingStartTime >= cookingDuration){
                currentItem.cook();
            }
        });

        cookingTimer.start();
    }

    private void stopCookingTimer() {
        if (cookingTimer != null && cookingTimer.isRunning()) {
            cookingTimer.stop();
            cookingStartTime = 0;
        }
    }
    
}