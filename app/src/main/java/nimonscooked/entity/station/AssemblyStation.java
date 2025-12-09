package nimonscooked.entity.station;

import nimonscooked.main.GamePanel;
import nimonscooked.entity.Chef;
import nimonscooked.entity.item.Item;
import nimonscooked.interfaces.Preparable;
import nimonscooked.entity.item.kitchenutensil.Plate;
import nimonscooked.entity.item.dish.Dish;

// berfungsi seperti countertop biasa
// namun bisa merakit/menggabungkan beberapa ingredient menjadi sebuah dish
public class AssemblyStation extends Station {

    public AssemblyStation(GamePanel gp) {
        super(gp);
        this.name = "Assembly Station";

        down1 = setup("/stations/assembly_station");
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
		Item item = player.getHeldItem();

        // station kosong, bisa meletakkan item apapun
        if(this.containedItem == null){
            if(item != null){
                super.placeItem(player.takeItem());
                System.out.println("[ASSEMBLY] Item placed on counter");
            } return;
        }
        

        // station ada item, mengambil atau merakit item tersebut
        
        // mengambil barang
        if(item == null){
            player.placeItem(super.takeItem());
            System.out.println("[ASSEMBLY] Mengambil item dari counter");
            return;
        }

        // merakit
        if(this.containedItem instanceof Plate && item instanceof Preparable){
            Plate plate = (Plate) this.containedItem;
            Preparable ingredient = (Preparable) item;
            Dish dish = plate.getContainedDish();

            if(dish.addComponent(ingredient)){
                player.takeItem();
                System.out.println("[ASSEMBLY] Added " + ingredient.getName());
                return;
            }
        } else{
            System.out.println("[ASSEMBLY] Can't add that item");
            return;
        }

        System.out.println("[ASSEMBLY] Counter is FULL. Can't place more items");
	}    
}