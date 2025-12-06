
package nimonscooked.entity.station;

public class WashingStation extends Station {
    public WashingStation(String id, float x, float y) {
        super(id, "Washing Station", x, y);
    }

	@Override
	public void interact(ChefPlayer player) {
		// placeholder
	}    
}
