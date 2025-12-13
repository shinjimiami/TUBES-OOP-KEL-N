package nimonscooked.main;

import nimonscooked.entity.Entity;
import nimonscooked.entity.Chef;

import nimonscooked.object.GameMap;

import java.util.List;


public class CollisionChecker {
	public void checkTile(Entity e) {
		if (e == null || e.gp == null) return;
		GamePanel gp = e.gp;
		int tileX = e.x / gp.tileSize;
		int tileY = e.y / gp.tileSize;
		if (!gp.gameMap.isWalkable(tileX, tileY)) {
			e.collision = true;
		} else {
			e.collision = false;
		}
	}

	public void chef(Entity e) {
		if (e == null || e.gp == null) return;
		GamePanel gp = e.gp;
		// Check collision against all chefs on the panel
		List<Chef> chefs = gp.chefs;
		int ex = e.x / gp.tileSize;
		int ey = e.y / gp.tileSize;
		for (Chef c : chefs) {
			if (c.getPosition().getX() == ex && c.getPosition().getY() == ey) {
				e.collision = true;
				return;
			}
		}
		e.collision = false;
	}
}
