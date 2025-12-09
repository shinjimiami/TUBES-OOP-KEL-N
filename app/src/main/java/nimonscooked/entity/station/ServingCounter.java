package nimonscooked.entity.station;

import nimonscooked.main.GamePanel;
import nimonscooked.entity.Chef;
import nimonscooked.entity.item.Item;
import nimonscooked.entity.item.kitchenutensil.Plate;
import nimonscooked.entity.item.dish.Dish;
import nimoncooked.object.OrderManager;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

public class ServingCounter extends Station {
    private final int ServingReturnTime = 10000;
    private final List<PlateReturnTimer> returnQueue = new LinkedList<>();

    private final PlateStorage plateStorage;

    public ServingCounter(GamePanel gp, PlateStorage ps) {
        super(gp);
        this.name = "Serving Counter";
        this.plateStorage = ps;

        down1 = setup("/stations/serving_counter");
        int tilesWide = 1;
        int tilesHigh = 2;
        this.imageWidth = gp.tileSize * tilesWide;
        this.imageHeight = gp.tileSize * tilesHigh;
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = this.imageWidth;
        solidArea.height = this.imageHeight;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    private static class PlateReturnTimer{
        final Plate plate;
        int remainingTime;

        public PlateReturnTimer(Plate p, int time){
            this.plate = p;
            this.remainingTime = time;
        }
    }

    // ini dipake buat di gamepanel, biar ada tracker waktu untuk servingTime
    public void updateDirtyPlate(int timePassed){
        if(returnQueue.isEmpty()) return;

        Iterator<PlateReturnTimer> iterator = returnQueue.iterator();
        while(iterator.hasNext()){
            PlateReturnTimer timer = iterator.next();
            timer.remainingTime -= timePassed;

            if(timer.remainingTime <= 0){
                System.out.println("[SERVING] Returning plate");
                plateStorage.receiveDirtyPlate(timer.plate);
                iterator.remove();
            }
        }
    }
    
	@Override
	public void interact(Chef player) {
		Item heldItem = player.getHeldItem();

        if(heldItem != null){
            // yang bisa diserve adalah plate
            if(heldItem instanceof Plate){
                Plate servedPlate = (Plate) player.takeItem();
                Dish servedDish = servedPlate.getContainedDish();

                if (servedDish!=null){
                    boolean success = OrderManager.getInstance().submitOrder(servedDish);

                    if(success){
                        System.out.println("[SERVING] the dish has been served.");
                        // keluarin sfx RIGHT

                    } else{
                        System.out.println("[SERVING] the dish is incorrect");
                        // keluarin sfx WRONG
                    }

                    servedPlate.setDirty(true);
                    servedPlate.clearDish();

                    returnQueue.add(new PlateReturnTimer(servedPlate, ServingReturnTime));
                    System.out.println("[SERVING] Plate is being served");
                    return;
                } else{
                    System.out.println("[SERVING] Plate is empty");
                    // keluarin sfx WRONG
                    return;
                }
            }
            // kalo bukan plate, langsung ga diterima
            System.out.println("[SERVING] Can't serve other than plate");
            // keluarin sfx WRONG
            return;                
        } else{return; }
	}    

}