package nimonscooked.entity;

import nimonscooked.enums.Direction;
import nimonscooked.enums.ChefStatus;
import nimonscooked.object.Position;

public class Chef {
    private String name;
    private Position position;
    private Direction direction;
    private ChefStatus status;

    public Chef(String name, int startX, int startY) {
        this.name = name;
        this.position = new Position(startX, startY);
        this.direction = Direction.RIGHT; // Default hadap kanan
        this.status = ChefStatus.IDLE;
    }

    // Logic pindah posisi
    public void move(int deltaX, int deltaY) {
        // Nanti bisa tambah validasi tembok disini
        this.position.setX(this.position.getX() + deltaX);
        this.position.setY(this.position.getY() + deltaY);
    }

    public void setDirection(Direction dir) {
        this.direction = dir;
    }

    // Getter
    public Position getPosition() { return position; }
    public Direction getDirection() { return direction; }
    public ChefStatus getStatus() { return status; }
    public String getName() { return name; }
}