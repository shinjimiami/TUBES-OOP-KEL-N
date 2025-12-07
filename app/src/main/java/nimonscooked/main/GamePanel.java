package nimonscooked.main;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {

    final int originalTileSize = 32;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 14;
    public final int maxScreenRow = 10;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    TileManager tileManager = new TileManager(this);
    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;

    // Set player default position
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    int fps = 60;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
//        this.addKeyListener(keyH);
        this.setFocusable(true);
        this.requestFocusInWindow();


    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = (double) 1000000000 / fps;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {

                update();
                repaint();
                delta--;
            }

            if (timer >= 1000000000) {
                timer = 0;
            }
        }

    }

    public void update(){

        player.update();

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.red);

        g2d.fillRect(playerX, playerY, tileSize, tileSize);

        tileManager.draw(g2d);

        player.draw(g2d);

        g2d.dispose();
    }

>>>>>>> try#1

import nimonscooked.entity.Chef;
import nimonscooked.action.InputHandler;
import nimonscooked.object.GameMap;
import nimonscooked.enums.Direction;

public class GamePanel extends JPanel implements Runnable, KeyListener {
    final int tileSize = 48;

    GameMap gameMap = new GameMap();
    final int screenWidth = tileSize * gameMap.getCols();
    final int screenHeight = tileSize * gameMap.getRows();

    Thread gameThread;
    InputHandler inputHandler;
    List<Chef> chefs = new ArrayList<>();
    int activeChefIndex = 0;

    BufferedImage[] chef1Sprites = new BufferedImage[8];
    BufferedImage[] chef2Sprites = new BufferedImage[8];

    // Warna Pastel Lantai
    Color pastelOrange = new Color(255, 223, 186);
    Color pastelYellow = new Color(255, 253, 208);

    // Warna Shiny Yellow (Emas)
    Color shinyGold = new Color(255, 215, 0);
    Color shinyGoldGlow = new Color(255, 223, 0, 120); // Transparan

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(this);
        this.setFocusable(true);

        chefs.add(new Chef("C1", "Kirby", 6, 2));
        chefs.add(new Chef("C2", "Waddle Dee", 8, 5));

        inputHandler = new InputHandler(gameMap, this, chefs);

        try {
            String p1 = "/nimonscooked/resources/KIRBY_";
            chef1Sprites[0] = load(p1 + "BELAKANG - KIRI NAIK.png");
            chef1Sprites[1] = load(p1 + "BELAKANG - KANAN NAIK.png");
            chef1Sprites[2] = load(p1 + "DEPAN - KIRI NAIK.png");
            chef1Sprites[3] = load(p1 + "DEPAN - KANAN NAIK.png");
            chef1Sprites[4] = load(p1 + "KIRI - KIRI NAIK.png");
            chef1Sprites[5] = load(p1 + "KIRI - KANAN NAIK.png");
            chef1Sprites[6] = load(p1 + "KANAN - KIRI NAIK.png");
            chef1Sprites[7] = load(p1 + "KANAN - KANAN NAIK.png");

            String p2 = "/nimonscooked/resources/WADDLE DEE_";
            chef2Sprites[0] = load(p2 + "BELAKANG - KIRI ATAS.png");
            chef2Sprites[1] = load(p2 + "BELAKANG - KANAN ATAS.png");
            chef2Sprites[2] = load(p2 + "DEPAN - KIRI ATAS.png");
            chef2Sprites[3] = load(p2 + "DEPAN - KANAN ATAS.png");
            chef2Sprites[4] = load(p2 + "KIRI - KIRI ATAS.png");
            chef2Sprites[5] = load(p2 + "KIRI - KANAN ATAS.png");
            chef2Sprites[6] = load(p2 + "KANAN - KIRI ATAS.png");
            chef2Sprites[7] = load(p2 + "KANAN - KANAN ATAS.png");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BufferedImage load(String path) {
        try {
            InputStream is = getClass().getResourceAsStream(path);
            if (is == null)
                return null;
            return ImageIO.read(is);
        } catch (Exception e) {
            return null;
        }
    }

    public Chef getActiveChef() {
        return chefs.get(activeChefIndex);
    }

    public void switchChef() {
        activeChefIndex = (activeChefIndex + 1) % chefs.size();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (gameThread != null) {
            update();
            repaint();
            try {
                Thread.sleep(1000 / 60);
            } catch (Exception e) {
            }
        }
    }

    public void update() {
        for (Chef c : chefs)
            c.update();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Draw Map
        char[][] grid = gameMap.getGrid();
        for (int row = 0; row < gameMap.getRows(); row++) {
            for (int col = 0; col < gameMap.getCols(); col++) {
                int x = col * tileSize;
                int y = row * tileSize;
                char tile = grid[row][col];

                if (tile == 'X') {
                    g2.setColor(Color.DARK_GRAY);
                    g2.fillRect(x, y, tileSize, tileSize);
                } else if (tile == '.' || tile == 'V') {
                    if ((row + col) % 2 == 0)
                        g2.setColor(pastelOrange);
                    else
                        g2.setColor(pastelYellow);
                    g2.fillRect(x, y, tileSize, tileSize);
                } else {
                    g2.setColor(new Color(100, 150, 255));
                    g2.fillRect(x, y, tileSize, tileSize);
                }

                if (tile != 'X' && tile != '.' && tile != 'V') {
                    g2.setColor(Color.BLACK);
                    g2.drawRect(x, y, tileSize, tileSize);
                    g2.drawString(String.valueOf(tile), x + 20, y + 30);
                }
            }
        }

        // Draw Chefs
        for (int i = 0; i < chefs.size(); i++) {
            Chef c = chefs.get(i);
            int px = c.getVisualX();
            int py = c.getVisualY();

            BufferedImage[] sprites = (i == 0) ? chef1Sprites : chef2Sprites;
            BufferedImage imageToDraw = null;

            int baseIndex = 0;
            switch (c.getDirection()) {
                case UP -> baseIndex = 0;
                case DOWN -> baseIndex = 2;
                case LEFT -> baseIndex = 4;
                case RIGHT -> baseIndex = 6;
            }

            if (c.spriteNum == 1)
                imageToDraw = sprites[baseIndex];
            else
                imageToDraw = sprites[baseIndex + 1];

            if (imageToDraw != null) {
                g2.setColor(new Color(0, 0, 0, 70));
                g2.fillOval(px + 8, py + 40, 32, 10);

                g2.drawImage(imageToDraw, px, py, tileSize, tileSize, null);

                // --- INDIKATOR AKTIF (SHINY YELLOW) ---
                // Hanya muncul jika:
                // 1. Chef ini adalah chef aktif (i == activeChefIndex)
                // 2. Chef ini TIDAK sedang bergerak (!c.isMoving())
                if (i == activeChefIndex && !c.isMoving()) {
                    // Glow Luar
                    g2.setColor(shinyGoldGlow);
                    g2.setStroke(new BasicStroke(5));
                    g2.drawRect(px - 2, py - 2, tileSize + 4, tileSize + 4);

                    // Border Dalam (Solid)
                    g2.setColor(shinyGold);
                    g2.setStroke(new BasicStroke(2));
                    g2.drawRect(px, py, tileSize, tileSize);
                }
                // --------------------------------------
            } else {
                g2.setColor(i == activeChefIndex ? Color.WHITE : Color.GRAY);
                g2.fillRect(px, py, tileSize, tileSize);
            }
        }
        g2.dispose();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        inputHandler.handleInput(e.getKeyCode(), getActiveChef());
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}