package nimonscooked.entity;

import nimonscooked.enums.Direction;
import nimonscooked.enums.ChefStatus;
import nimonscooked.entity.item.Item;
import nimonscooked.entity.station.Station;
import nimonscooked.object.Position;
import java.awt.Graphics2D;
import java.awt.Color;

public class Chef extends Entity {
    private Direction direction;
    private ChefStatus status;
    private Item heldItem; // Slot inventory (max 1)

    // Untuk visualisasi sederhana (kotak)
    private Color color;

    public Chef(String id, String name, int startX, int startY, Color color) {
        super(id, name, startX, startY);
        this.direction = Direction.RIGHT;
        this.status = ChefStatus.IDLE;
        this.color = color;
        // Inisialisasi posisi world (asumsi 1 tile = 32*3 = 96 pixel, disesuaikan di
        // GamePanel)
    }

    // --- LOGIC PERGERAKAN ---
    public void move(int deltaX, int deltaY, Station[][] stationMap, int maxCol, int maxRow) {
        if (status == ChefStatus.BUSY) {
            return;
        }

        int nextX = this.x + deltaX;
        int nextY = this.y + deltaY;

        // 1. Cek Batas Map jika disediakan
        if (maxCol > 0 && (nextX < 0 || nextX >= maxCol)) {
            return;
        }
        if (maxRow > 0 && (nextY < 0 || nextY >= maxRow)) {
            return;
        }

        // 2. Cek Tabrakan dengan Station/Tembok
        if (stationMap != null && stationMap.length > 0) {
            if (nextX >= 0 && nextY >= 0 && nextX < stationMap.length && nextY < stationMap[0].length) {
                if (stationMap[nextX][nextY] != null) {
                    // Ada station/tembok, tidak bisa jalan
                    return;
                }
            }
        }

        // TODO: Cek tabrakan dengan Chef lain (nanti di GamePanel/CollisionChecker)

        this.x = nextX;
        this.y = nextY;
    }

    public void move(int deltaX, int deltaY) {
        move(deltaX, deltaY, null, -1, -1);
    }

    public void setDirection(Direction dir) {
        if (status != ChefStatus.BUSY) {
            this.direction = dir;
        }
    }

    // --- LOGIC INVENTORY & INTERAKSI ---

    public Item getHeldItem() {
        return heldItem;
    }

    public void setHeldItem(Item item) {
        this.heldItem = item;
    }

    // Ambil item dari tangan (mengosongkan tangan)
    public Item takeHeldItem() {
        Item item = heldItem;
        heldItem = null;
        return item;
    }

    public void setStatus(ChefStatus status) {
        this.status = status;
    }

    public ChefStatus getStatus() {
        return status;
    }

    public Direction getDirection() {
        return direction;
    }

    // Helper untuk mendapatkan koordinat di depan Chef
    public Position getFacingPosition() {
        int targetX = x;
        int targetY = y;

        switch (direction) {
            case UP -> targetY--;
            case DOWN -> targetY++;
            case LEFT -> targetX--;
            case RIGHT -> targetX++;
        }
        return new Position(targetX, targetY);
    }

    // Draw sederhana untuk debugging
    public void draw(Graphics2D g2, int tileSize) {
        g2.setColor(color);
        g2.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);

        // Penanda arah hadap (kotak kecil)
        g2.setColor(Color.WHITE);
        int dirSize = tileSize / 4;
        int dirX = x * tileSize + (tileSize / 2) - (dirSize / 2);
        int dirY = y * tileSize + (tileSize / 2) - (dirSize / 2);

        switch (direction) {
            case UP -> dirY -= tileSize / 3;
            case DOWN -> dirY += tileSize / 3;
            case LEFT -> dirX -= tileSize / 3;
            case RIGHT -> dirX += tileSize / 3;
        }
        g2.fillRect(dirX, dirY, dirSize, dirSize);

        // Visualisasi Item di tangan
        if (heldItem != null) {
            g2.setColor(Color.YELLOW);
            g2.drawString(heldItem.getName(), x * tileSize, y * tileSize - 5);
        }
    }
}
