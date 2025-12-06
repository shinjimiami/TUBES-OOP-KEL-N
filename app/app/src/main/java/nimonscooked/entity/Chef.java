package nimonscooked.entity;

import nimonscooked.enums.Direction;
import nimonscooked.enums.ChefStatus;
import nimonscooked.object.Position;
import nimonscooked.object.Item;

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
        if (isMoving || currentAction == ChefStatus.BUSY) return;

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
                if (idleYOffset < -2) idleGoingUp = false;
            } else {
                idleYOffset++;
                if (idleYOffset > 2) idleGoingUp = true;
            }
            idleCounter = 0;
        }
    }

    public int getVisualY() { return screenY + idleYOffset; }
    public int getVisualX() { return screenX; }
    
    public void setDirection(Direction d) { this.direction = d; }
    public Direction getDirection() { return direction; }
    public void setInventory(Item i) { this.inventory = i; }
    public Item getInventory() { return inventory; }
    public void setCurrentAction(ChefStatus s) { this.currentAction = s; }
    public ChefStatus getCurrentAction() { return currentAction; }
    public Position getPosition() { return position; }
    public String getName() { return name; }
    public boolean isMoving() { return isMoving; }
}