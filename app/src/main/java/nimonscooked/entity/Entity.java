package nimonscooked.entity;

import java.awt.image.BufferedImage;

public abstract class Entity{
    protected String id;
    protected String name;
    protected int x;
    protected int y;

    public BufferedImage image;
    public boolean collision = false;
    
    public Entity(String id, String name, int x, int y) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public void moveTo(int X, int Y) {
        this.x += X;
        this.y += Y;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

}
