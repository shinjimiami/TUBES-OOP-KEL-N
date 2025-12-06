package nimonscooked.entity.station;

import nimonscooked.entity.station.ChefPlayer;
import nimonscooked.entity.station.Station;
import nimonscooked.interfaces.Preparable;
import nimonscooked.enums.IngredientState;
import javax.swing.Timer;


// berfungsi untuk memotong ingredient yang dapat dipotong
// hanya akan bergerak apabila chefPlayer berada di sebelah cutting station
public class CuttingStation extends Station {
    private final int cuttingDuration = 15000; //15 detik
    private final int interval = 1000; //buat UI nya nanti
    private Timer cuttingTimer;
    private int cuttingStartTime = 0;
    private int savedTime = 0;

    public CuttingStation(String id, float x, float y) {
        super(id, "Cutting Station", x, y);
    }

    @Override
    public void interact(ChefPlayer player) {
        // cek apakah ada item di cutting station
        if(this.containedItem == null){
            if(player.getHeldItem() != null){ //menunggu update dari chefPlayer, ini berfungsi untuk ngecek apakah player pegang item atau nggak
                super.placeItem(player.takeItem()); //item ditaro di cutting station
                this.savedTime = 0;
            }
        }

        // lanjut apabila ada item di cutting station
        // cek apakah item bisa dipotong
        if(!(containedItem instanceof Preparable)){
            System.out.println("Item tidak bisa dipotong");
            return;
        }

        Preparable preparableItem = (Preparable) containedItem;
        if(!preparableItem.canBeChopped()){
            System.out.println("Item tidak bisa dipotong");
            return;
        }
        // potong item
        preparableItem.chop();
        startCuttingTimer(preparableItem);
        System.out.println("Item berhasil dipotong");
        return;
    }

    // implementasi pemotongan dengan timer
    private void startCuttingTimer(Preparable item) {
        stopCuttingTimer();

        cuttingTimer = new Timer(cuttingDuration, e -> {
            item.chop();
            cuttingTimer.stop();
        });

        cuttingTimer.setRepeats(false);
        cuttingTimer.start();
    }

    private void stopCuttingTimer() {
        if (cuttingTimer != null && cuttingTimer.isRunning()) {
            cuttingTimer.stop();
        }
    }

    private void finishCutting(Preparable item) {
        item.chop();
        // harusnya nanti UI diupdate disini
    }
}