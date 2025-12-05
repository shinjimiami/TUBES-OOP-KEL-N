package nimonscooked.main;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import nimonscooked.entity.Chef;
import nimonscooked.action.InputHandler;
import nimonscooked.enums.Direction;
import nimonscooked.object.GameMap;

public class GamePanel extends JPanel implements Runnable, KeyListener {
    final int tileSize = 48;
    
    GameMap gameMap = new GameMap();
    final int screenWidth = tileSize * gameMap.getCols();
    final int screenHeight = tileSize * gameMap.getRows();
    
    Thread gameThread;
    InputHandler inputHandler;
    
    List<Chef> chefs = new ArrayList<>();
    int activeChefIndex = 0;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(this);
        this.setFocusable(true);

        // Spawn 2 Chef di posisi V (6,2) dan (8,5)
        chefs.add(new Chef("C1", "Chef Kebin", 6, 2));
        chefs.add(new Chef("C2", "Chef Stewart", 8, 5));
        
        inputHandler = new InputHandler(gameMap, this);
    }

    public Chef getActiveChef() {
        return chefs.get(activeChefIndex);
    }

    public void switchChef() {
        activeChefIndex = (activeChefIndex + 1) % chefs.size();
        System.out.println("Switched to: " + getActiveChef().getName());
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while(gameThread != null) {
            repaint();
            try { 
                Thread.sleep(1000/60); 
            } catch (InterruptedException e) { 
                e.printStackTrace(); 
            }
        }
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
                } else if (tile == '.') {
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.fillRect(x, y, tileSize, tileSize);
                    g2.setColor(Color.GRAY);
                    g2.drawRect(x, y, tileSize, tileSize);
                } else if (tile == 'V') {
                    g2.setColor(new Color(200, 200, 255));
                    g2.fillRect(x, y, tileSize, tileSize);
                } else {
                    g2.setColor(new Color(100, 150, 255));
                    g2.fillRect(x, y, tileSize, tileSize);
                    g2.setColor(Color.BLACK);
                    g2.drawString(String.valueOf(tile), x + 20, y + 30);
                }
            }
        }

        // Draw Chefs
        for (int i = 0; i < chefs.size(); i++) {
            Chef c = chefs.get(i);
            
            if (i == activeChefIndex) {
                g2.setColor(Color.WHITE);
            } else {
                g2.setColor(Color.GRAY);
            }
            
            int px = c.getPosition().getX() * tileSize;
            int py = c.getPosition().getY() * tileSize;
            g2.fillRect(px, py, tileSize, tileSize);
            
            g2.setColor(Color.BLACK);
            int eyeX = px, eyeY = py;
            Direction dir = c.getDirection();
            
            if (dir == Direction.RIGHT) { eyeX += 30; eyeY += 10; }
            if (dir == Direction.LEFT)  { eyeX += 5; eyeY += 10; }
            if (dir == Direction.UP)    { eyeX += 10; eyeY += 5; }
            if (dir == Direction.DOWN)  { eyeX += 10; eyeY += 30; }
            
            g2.fillOval(eyeX, eyeY, 10, 10);
        }
        g2.dispose();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        inputHandler.handleInput(e.getKeyCode(), getActiveChef());
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}