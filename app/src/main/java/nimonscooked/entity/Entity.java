package nimonscooked.entity;

import java.awt.image.BufferedImage; 

public class Entity {
    
    public int x; 
    public int y;
    public String id;
    public String name;
    public BufferedImage image;

    // Constructor
    public Entity(String id, String name, int x, int y) {
        this.id = id;
        this.name = name;
        this.x = x; 
        this.y = y;
    }
}