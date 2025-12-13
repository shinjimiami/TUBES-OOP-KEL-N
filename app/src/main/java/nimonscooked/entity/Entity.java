package nimonscooked.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

import nimonscooked.main.GamePanel;
import nimonscooked.enums.EntityType;
import nimonscooked.enums.Direction;

public abstract class Entity {
    public GamePanel gp;
    public int x, y;
    public String id;
    public String name;
    public BufferedImage image;
    protected BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public boolean collisionON = false;
    public boolean collision = false;
    public EntityType type;
    public Rectangle solidArea;
    public int solidAreaDefaultX, solidAreaDefaultY;
    public Direction direction = Direction.DOWN;
    public Direction defaultDirection = Direction.DOWN;
    public int imageWidth, imageHeight;

    public Entity(GamePanel gp) {
        this.gp = gp;
        if (gp != null) {
            this.imageWidth = gp.tileSize;
            this.imageHeight = gp.tileSize;
        }
    }

    public Entity(String id, String name, int x, int y) {
        this.gp = null;
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.imageWidth = this.imageHeight = 48;
    }

    protected BufferedImage getEntityImage(String imagePath) {
        if (imagePath == null)
            return null;
        try (InputStream is = getClass().getResourceAsStream(imagePath)) {
            if (is != null)
                return ImageIO.read(is);
        } catch (IOException ignored) {
        }
        return null;
    }

    protected BufferedImage setup(String basePath) {
        if (basePath == null)
            return null;
        BufferedImage img = getEntityImage(basePath + ".png");
        if (img == null)
            img = getEntityImage(basePath);
        return img;
    }

    public void update() {
        collisionON = false;
        checkCollisionAndMove();
        updateSprite();
    }

    public boolean use(Entity chef) {
        System.out.println("[PLAYER] Trying to use " + this.name + " (Default Action is do nothing)");
        return false;
    }

    public void checkCollisionAndMove() {
        collisionON = false;
        // Use CollisionChecker from GamePanel if available
        if (gp != null && gp.getCollisionChecker() != null) {
            gp.getCollisionChecker().checkTile(this);
            gp.getCollisionChecker().chef(this);
            collisionON = this.collision;
        }
    }

    public void updateSprite() {
        spriteCounter++;
        if (spriteCounter > 12) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage imageToDraw = null;
        if (type == EntityType.PLAYER) {
            switch (direction) {
                case UP:
                    imageToDraw = (spriteNum == 1) ? up1 : up2;
                    break;
                case DOWN:
                    imageToDraw = (spriteNum == 1) ? down1 : down2;
                    break;
                case LEFT:
                    imageToDraw = (spriteNum == 1) ? left1 : left2;
                    break;
                case RIGHT:
                    imageToDraw = (spriteNum == 1) ? right1 : right2;
                    break;
                default:
                    imageToDraw = down1;
            }
        } else {
            imageToDraw = (this.image != null) ? this.image : this.down1;
        }

        if (imageToDraw != null && gp != null) {
            g2.drawImage(imageToDraw, x, y, gp.tileSize, gp.tileSize, null);
        }
    }

    public String getName() {
        return this.name;
    }

    public BufferedImage getSprite() {
        // Return the current sprite image for rendering
        if (down1 != null)
            return down1;
        if (image != null)
            return image;
        return null;
    }
}