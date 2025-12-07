package nimonscooked.entity;

import nimonscooked.enums.Direction;
import nimonscooked.enums.ChefStatus;
import nimonscooked.object.Position;
import nimonscooked.entity.item.Item;

public class Chef {
    // --- Identitas ---
    private String id;
    private String name;

    // --- Posisi & Visual ---
    private Position position;
    public int screenX, screenY; // Koordinat visual untuk rendering
    private Direction direction;

    // --- State & Animasi ---
    private ChefStatus currentAction;
    private boolean isMoving = false;
    private int pixelCounter = 0;
    private final int speed = 4;
    public final int tileSize = 48; // Pastikan public agar bisa diakses jika perlu

    // Variabel Animasi Sprite
    public int spriteCounter = 0;
    public int spriteNum = 1;
    private boolean stepState = false;
    private int idleCounter = 0;
    private int idleYOffset = 0;
    private boolean idleGoingUp = true;

    // --- INVENTORY (Pindahan dari ChefPlayer) ---
    private Item heldItem;

    // --- Constructor ---
    public Chef(String id, String name, int startX, int startY) {
        this.id = id;
        this.name = name;
        this.position = new Position(startX, startY);
        this.screenX = startX * tileSize;
        this.screenY = startY * tileSize;
        this.direction = Direction.DOWN;
        this.heldItem = null;
        this.currentAction = ChefStatus.IDLE;
    }

    // --- Game Loop Update ---
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
            // Snap to grid
            screenX = position.getX() * tileSize;
            screenY = position.getY() * tileSize;
            pixelCounter = 0;
            isMoving = false;
        }
    }

    public void attemptMove(Direction dir, int deltaX, int deltaY) {
        // Cek status BUSY (sedang memotong/mencuci) atau sedang bergerak
        if (isMoving || currentAction == ChefStatus.BUSY)
            return;

        this.direction = dir;

        // Update posisi grid logika
        this.position.setX(this.position.getX() + deltaX);
        this.position.setY(this.position.getY() + deltaY);

        // Update animasi langkah
        stepState = !stepState;
        spriteNum = stepState ? 2 : 1;

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

    // --- METHODS INVENTORY (Penting untuk Interaksi) ---

    public Item getHeldItem() {
        return heldItem;
    }

    // Mengambil item dari tangan chef (misal: menaruh ke meja)
    public Item takeItem() {
        Item item = heldItem;
        heldItem = null;
        return item;
    }

    // Memberi item ke tangan chef (misal: mengambil dari meja)
    public void placeItem(Item item) {
        this.heldItem = item;
    }

    // --- Getters & Setters Visual ---
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

    public void setStatus(ChefStatus busy) {
        this.currentAction = busy;
        if (busy == ChefStatus.BUSY) {
            // Pastikan animasi pergerakan berhenti saat sedang sibuk
            this.isMoving = false;
            this.pixelCounter = 0;
        }
    }
}
