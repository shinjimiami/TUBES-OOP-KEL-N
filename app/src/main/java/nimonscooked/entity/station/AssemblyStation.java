package nimonscooked.entity.station;

// berfungsi seperti countertop biasa
// namun hanya untuk merakit/menggabungkan beberapa ingredient menjadi sebuah dish
public class AssemblyStation extends Station {

    public AssemblyStation(String id, float x, float y) {
        super(id, "Assembly Station", x, y);
    }
	@Override
	public void interact(ChefPlayer player) {
		// placeholder
	}    
}
