package nimonscooked.entity.station;

// berfungsi untuk menyimpan plate yang diambil pemain
// pada awal game, plate akan tersedia di plate storage dalam kondisi bersih
// tetapi apabila telah 
public class PlateStorage extends Station {

    public PlateStorage(String id, float x, float y) {
        super(id, "Plate Storage", x, y);
    }
    
	@Override
	public void interact(ChefPlayer player) {
		// placeholder
	}    
}