package nimonscooked.main;

public class Main {
	public static void main(String[] args){
		GamePanel gp = new GamePanel();
		gp.setupGame();
		System.out.println("Game initialized. Chef at: " + gp.getChef().getPosition().getX() + "," + gp.getChef().getPosition().getY());
		System.out.println("Stations: " + gp.getStations().size());
	}
}
