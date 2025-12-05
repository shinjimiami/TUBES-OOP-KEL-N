package nimonscooked.main;

import nimonscooked.entity.Chef;
import nimonscooked.entity.station.*;
import java.util.ArrayList;
import java.util.List;

public class GamePanel {
    private Chef chef;
    private List<Station> stations;

    public GamePanel(){
        stations = new ArrayList<>();
    }

    public void setupGame(){
        // create player
        this.chef = new Chef("Player", 5, 5);

        // create stations and entities
        stations.add(new IngredientStorage("ing1", 1f, 1f));
        stations.add(new PlateStorage("plate1", 2f, 1f));
        stations.add(new CuttingStation("cut1", 3f, 1f));
        stations.add(new CookingStation("cook1", 4f, 1f));
        stations.add(new WashingStation("wash1", 5f, 1f));
        stations.add(new TrashStation("trash1", 6f, 1f));
        stations.add(new ServingCounter("serve1", 7f, 1f));
        stations.add(new AssemblyStation("assembly1", 8f, 1f));
    }

    public Chef getChef(){ return chef; }
    public List<Station> getStations(){ return stations; }
}
 
