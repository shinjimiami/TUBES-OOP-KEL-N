
package nimonscooked.entity.station;

import nimonscooked.entity.Chef;

public class WashingStation extends Station {
    public WashingStation(String id, int x, int y) {
        super(id, "Washing Station", x, y);
    }

	@Override
	public void interact(Chef player) {
		// placeholder
	}    
}
