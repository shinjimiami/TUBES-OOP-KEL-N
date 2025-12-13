package nimonscooked.entity;

import nimonscooked.main.GamePanel;
import nimonscooked.enums.Direction;
import nimonscooked.enums.ChefStatus;
import nimonscooked.enums.EntityType;
import nimonscooked.object.Position;
import nimonscooked.entity.item.Item;
import nimonscooked.entity.item.kitchenutensil.DirtyPlateStack;

public class Chef {
    private String id;
    private String name;

    private Position position;
    public int screenX, screenY;

    private Direction direction;
    private Item inventory;
    private ChefStatus currentAction;

    private boolean isMoving = false;
    private int pixelCounter = 0;
    private final int speed = 4;
    private final int tileSize = 48;

    public int spriteCounter = 0;
    public int spriteNum = 1;
    private boolean stepState = false;

    private int idleCounter = 0;
    private int idleYOffset = 0;
    private boolean idleGoingUp = true;

    // Dash feature
    private long lastDashTime = 0;
    private final long DASH_COOLDOWN = 2000; // 2 seconds
    private final int DASH_DISTANCE = 3; // 3 tiles

    public Chef(String id, String name, int startX, int startY) {
        this.id = id;
        this.name = name;
        this.position = new Position(startX, startY);
        this.screenX = startX * tileSize;
        this.screenY = startY * tileSize;
        this.direction = Direction.DOWN;
        this.inventory = null;
        this.currentAction = ChefStatus.IDLE;
    }

    public void update() {
        if (isMoving) {
            moveSmoothly();
            idleYOffset = 0;
        } else {
            animateIdle();
        }
    }

    private void moveSmoothly() {
        switch (direction) {
            case UP -> screenY -= speed;
            case DOWN -> screenY += speed;
            case LEFT -> screenX -= speed;
            case RIGHT -> screenX += speed;
        }
        pixelCounter += speed;

        if (pixelCounter >= tileSize) {
            screenX = position.getX() * tileSize;
            screenY = position.getY() * tileSize;
            pixelCounter = 0;
            isMoving = false;
        }
    }

    public void attemptMove(Direction dir, int deltaX, int deltaY) {
        if (isMoving || currentAction == ChefStatus.BUSY)
            return;

        this.direction = dir;
        this.position.setX(this.position.getX() + deltaX);
        this.position.setY(this.position.getY() + deltaY);

        stepState = !stepState;
        if (!stepState) {
            spriteNum = 1;
        } else {
            spriteNum = 2;
        }

        isMoving = true;
    }

    private void animateIdle() {
        idleCounter++;
        if (idleCounter > 20) {
            if (idleGoingUp) {
                idleYOffset--;
                if (idleYOffset < -2)
                    idleGoingUp = false;
            } else {
                idleYOffset++;
                if (idleYOffset > 2)
                    idleGoingUp = true;
            }
            idleCounter = 0;
        }
    }

    public int getVisualY() {
        return screenY + idleYOffset;
    }

    public int getVisualX() {
        return screenX;
    }

    public void setDirection(Direction d) {
        this.direction = d;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setInventory(Item i) {
        this.inventory = i;
    }

    public Item getInventory() {
        return inventory;
    }

    public void setCurrentAction(ChefStatus s) {
        this.currentAction = s;
    }

    public ChefStatus getCurrentAction() {
        return currentAction;
    }

    public Position getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public boolean dash(nimonscooked.object.GameMap map) {
        // Check cooldown
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastDashTime < DASH_COOLDOWN) {
            System.out.println(
                    "[DASH] Cooldown active! " + (DASH_COOLDOWN - (currentTime - lastDashTime)) + "ms remaining");
            return false;
        }

        // Calculate dash direction
        int dx = 0, dy = 0;
        switch (direction) {
            case UP -> dy = -1;
            case DOWN -> dy = 1;
            case LEFT -> dx = -1;
            case RIGHT -> dx = 1;
        }

        // Try to dash up to 3 tiles
        int tilesMovedCount = 0;
        int currentX = position.getX();
        int currentY = position.getY();

        for (int i = 1; i <= DASH_DISTANCE; i++) {
            int newX = currentX + (dx * i);
            int newY = currentY + (dy * i);

            // Check if next tile is walkable
            if (!map.isWalkable(newX, newY)) {
                System.out.println("[DASH] Blocked at tile " + i + " by obstacle at (" + newX + ", " + newY + ")");
                break;
            }

            // Update position
            tilesMovedCount = i;
        }

        if (tilesMovedCount > 0) {
            // Apply dash movement
            int finalX = currentX + (dx * tilesMovedCount);
            int finalY = currentY + (dy * tilesMovedCount);

            position.setX(finalX);
            position.setY(finalY);
            screenX = finalX * tileSize;
            screenY = finalY * tileSize;

            lastDashTime = currentTime;
            System.out.println("[DASH] " + name + " dashed " + tilesMovedCount + " tiles " + direction + " to ("
                    + finalX + ", " + finalY + ")");
            return true;
        } else {
            System.out.println("[DASH] Cannot dash - blocked immediately!");
            return false;
        }
    }

    public long getDashCooldownRemaining() {
        long currentTime = System.currentTimeMillis();
        long remaining = DASH_COOLDOWN - (currentTime - lastDashTime);
        return Math.max(0, remaining);
    }

    public boolean isDashReady() {
        return System.currentTimeMillis() - lastDashTime >= DASH_COOLDOWN;
    }

    /**
     * Checks if the chef is carrying a DirtyPlateStack
     */
    public boolean isCarryingDirtyPlates() {
        return inventory instanceof DirtyPlateStack;
    }

    /**
     * Gets the DirtyPlateStack if chef is carrying one
     */
    public DirtyPlateStack getDirtyPlateStack() {
        if (inventory instanceof DirtyPlateStack) {
            return (DirtyPlateStack) inventory;
        }
        return null;
    }

    public boolean throwItem(nimonscooked.object.GameMap map, java.util.List<Chef> allChefs) {
        // Check if chef has an item
        if (inventory == null) {
            System.out.println("[THROW] " + name + " has nothing to throw!");
            return false;
        }

        // Check if item is an Ingredient (not Plate/Pan)
        if (!(inventory instanceof nimonscooked.entity.item.ingredient.Ingredient)) {
            System.out.println("[THROW] " + name + " cannot throw " + inventory.getName() + " (not an Ingredient)");
            return false;
        }

        Item itemToThrow = inventory;
        inventory = null; // Remove from inventory

        // Calculate throw direction
        int dx = 0, dy = 0;
        switch (direction) {
            case UP -> dy = -1;
            case DOWN -> dy = 1;
            case LEFT -> dx = -1;
            case RIGHT -> dx = 1;
        }

        // Find landing position (max 3 tiles)
        int currentX = position.getX();
        int currentY = position.getY();
        int landingX = currentX;
        int landingY = currentY;
        int throwDistance = 0;
        final int MAX_THROW_DISTANCE = 3;

        for (int i = 1; i <= MAX_THROW_DISTANCE; i++) {
            int checkX = currentX + (dx * i);
            int checkY = currentY + (dy * i);

            // Check if tile is walkable
            if (!map.isWalkable(checkX, checkY)) {
                System.out.println("[THROW] Blocked at tile " + i + " by obstacle at (" + checkX + ", " + checkY + ")");
                break;
            }

            // Valid landing spot
            landingX = checkX;
            landingY = checkY;
            throwDistance = i;
        }

        System.out.println("[THROW] " + name + " threw " + itemToThrow.getName() + " " + throwDistance + " tiles "
                + direction + " to (" + landingX + ", " + landingY + ")");

        // Check if another chef is at landing position
        for (Chef otherChef : allChefs) {
            if (otherChef != this &&
                    otherChef.getPosition().getX() == landingX &&
                    otherChef.getPosition().getY() == landingY) {

                // Check if other chef's hands are empty
                if (otherChef.getInventory() == null) {
                    otherChef.setInventory(itemToThrow);
                    System.out.println("[THROW] " + otherChef.getName() + " caught " + itemToThrow.getName() + "!");
                    return true;
                } else {
                    System.out.println("[THROW] " + otherChef.getName() + "'s hands are full, item drops to floor");
                    break;
                }
            }
        }

        // No one caught it, place on floor
        boolean placed = map.placeItemOnFloor(landingX, landingY, itemToThrow);
        if (placed) {
            System.out.println(
                    "[THROW] " + itemToThrow.getName() + " landed on floor at (" + landingX + ", " + landingY + ")");
        } else {
            System.out.println("[THROW] Failed to place " + itemToThrow.getName() + " on floor!");
        }

        return true;
    }
}