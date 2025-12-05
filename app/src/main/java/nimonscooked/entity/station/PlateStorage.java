package nimonscooked.entity.station;

import nimonscooked.entity.Chef;

// berfungsi untuk menyimpan plate yang diambil pemain
// pada awal game, plate akan tersedia di plate storage dalam kondisi bersih
// tetapi apabila telah 
public class PlateStorage extends Station {

    public PlateStorage(String id, int x, int y) {
        super(id, "Plate Storage", x, y);
    }
    
	@Override
	public void interact(Chef player) {
		// placeholder
	}    
}