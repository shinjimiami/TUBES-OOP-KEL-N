
package nimonscooked.entity.station;

// berfungsi untuk menghapus item yang dibuang pemain
// item yang dapat dibuang hanya ingredient atau dish pada plate
// item yang dibuang akan dihapus dari game
public class TrashStation extends Station {
    public TrashStation(String id, float x, float y) {
        super(id, "Trash Station", x, y);
    }

	@Override
	public void interact(ChefPlayer player) {
		// placeholder
	}        
}
