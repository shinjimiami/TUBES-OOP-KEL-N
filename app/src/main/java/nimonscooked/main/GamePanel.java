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

import nimonscooked.entity.Chef;
import nimonscooked.action.InputHandler;
import nimonscooked.object.GameMap;

public class GamePanel extends JPanel implements Runnable, KeyListener {
    public final int tileSize = 48;

    GameMap gameMap = new GameMap();

    // Layout constants
    final int topMargin = 30; // Space for orders
    final int bottomMargin = 90; // Space for character panel
    final int mapWidth = tileSize * gameMap.getCols();
    final int mapHeight = tileSize * gameMap.getRows();

    final int screenWidth = mapWidth;
    final int screenHeight = topMargin + mapHeight + bottomMargin;

    Thread gameThread;
    InputHandler inputHandler;
    List<Chef> chefs = new ArrayList<>();
    int activeChefIndex = 0;
    long lastChefSwitchTime = 0;
    final long CHEF_INDICATOR_DURATION = 3000; // 3 seconds

    BufferedImage[] chef1Sprites = new BufferedImage[8];
    BufferedImage[] chef2Sprites = new BufferedImage[8];
    BufferedImage menuBackground;

    // Warna Pastel Lantai
    Color pastelOrange = new Color(255, 223, 186);
    Color pastelYellow = new Color(255, 253, 208);

    // Warna Shiny Yellow (Emas)
    Color shinyGold = new Color(255, 215, 0);
    Color shinyGoldGlow = new Color(255, 223, 0, 120); // Transparan

    // Order management
    private OrderManager orderManager;
    private long lastOrderTime = 0;
    private final long orderInterval = 30000; // Generate order every 30 seconds
    private int frameCounter = 0;

    // Game state management
    private enum GameState {
        MENU, // Main menu
        HOW_TO_PLAY, // Instructions screen
        STAGE_SELECT, // Stage selection
        PLAYING, // In-game
        WIN, // Win screen
        LOSE // Lose screen
    }

    private GameState gameState = GameState.MENU; // Start at menu
    private final int WIN_COMPLETED_ORDERS = 2; // Win after 2 completed orders
    private final int LOSE_SCORE = -10; // Lose condition

    // Menu buttons
    private Rectangle startButton;
    private Rectangle howToPlayButton;
    private Rectangle exitButton;
    private Rectangle backButton;
    private Rectangle selectStageButton;
    private Rectangle tryAgainButton;

    // Button hover/press state
    private Rectangle hoveredButton = null;
    private Rectangle pressedButton = null;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(this);
        this.setFocusable(true);

        // Add mouse listener for all button clicks
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                Point clickPoint = e.getPoint();
                // Track pressed button for visual feedback
                if (startButton != null && startButton.contains(clickPoint)) {
                    pressedButton = startButton;
                } else if (howToPlayButton != null && howToPlayButton.contains(clickPoint)) {
                    pressedButton = howToPlayButton;
                } else if (exitButton != null && exitButton.contains(clickPoint)) {
                    pressedButton = exitButton;
                } else if (backButton != null && backButton.contains(clickPoint)) {
                    pressedButton = backButton;
                } else if (selectStageButton != null && selectStageButton.contains(clickPoint)) {
                    pressedButton = selectStageButton;
                } else if (tryAgainButton != null && tryAgainButton.contains(clickPoint)) {
                    pressedButton = tryAgainButton;
                }
                repaint();
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                pressedButton = null;
                repaint();
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                Point clickPoint = e.getPoint();

                switch (gameState) {
                    case MENU:
                        if (startButton != null && startButton.contains(clickPoint)) {
                            gameState = GameState.STAGE_SELECT;
                        } else if (howToPlayButton != null && howToPlayButton.contains(clickPoint)) {
                            gameState = GameState.HOW_TO_PLAY;
                        } else if (exitButton != null && exitButton.contains(clickPoint)) {
                            System.exit(0);
                        }
                        break;

                    case HOW_TO_PLAY:
                        if (backButton != null && backButton.contains(clickPoint)) {
                            gameState = GameState.MENU;
                        }
                        break;

                    case STAGE_SELECT:
                        if (selectStageButton != null && selectStageButton.contains(clickPoint)) {
                            startGame();
                        } else if (backButton != null && backButton.contains(clickPoint)) {
                            gameState = GameState.MENU;
                        }
                        break;

                    case WIN:
                    case LOSE:
                        if (tryAgainButton != null && tryAgainButton.contains(clickPoint)) {
                            restartGame();
                        } else if (backButton != null && backButton.contains(clickPoint)) {
                            gameState = GameState.MENU;
                            resetGame();
                        }
                        break;

                    default:
                        break;
                }
            }
        });

        // Add mouse motion listener for hover effects
        this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                Point mousePoint = e.getPoint();
                Rectangle oldHovered = hoveredButton;
                hoveredButton = null;

                // Check which button is hovered
                if (startButton != null && startButton.contains(mousePoint)) {
                    hoveredButton = startButton;
                } else if (howToPlayButton != null && howToPlayButton.contains(mousePoint)) {
                    hoveredButton = howToPlayButton;
                } else if (exitButton != null && exitButton.contains(mousePoint)) {
                    hoveredButton = exitButton;
                } else if (backButton != null && backButton.contains(mousePoint)) {
                    hoveredButton = backButton;
                } else if (selectStageButton != null && selectStageButton.contains(mousePoint)) {
                    hoveredButton = selectStageButton;
                } else if (tryAgainButton != null && tryAgainButton.contains(mousePoint)) {
                    hoveredButton = tryAgainButton;
                }

                // Repaint only if hover state changed
                if (oldHovered != hoveredButton) {
                    repaint();
                }
            }
        });

        chefs.add(new Chef("C1", "Kirby", 6, 2));
        chefs.add(new Chef("C2", "Waddle Dee", 8, 5));

        inputHandler = new InputHandler(gameMap, this, chefs);
        orderManager = OrderManager.getInstance();

        // Note: Orders will be generated when game starts from stage select

        try {
            // Load menu background
            menuBackground = load("/menu/start.png");
            if (menuBackground != null) {
                System.out.println("[MENU] Menu background loaded successfully!");
            } else {
                System.out.println("[MENU] Failed to load menu background!");
            }

            String p1 = "/chef/KIRBY_";
            chef1Sprites[0] = load(p1 + "BELAKANG - KIRI NAIK.png");
            chef1Sprites[1] = load(p1 + "BELAKANG - KANAN NAIK.png");
            chef1Sprites[2] = load(p1 + "DEPAN - KIRI NAIK.png");
            chef1Sprites[3] = load(p1 + "DEPAN - KANAN NAIK.png");
            chef1Sprites[4] = load(p1 + "KIRI - KIRI NAIK.png");
            chef1Sprites[5] = load(p1 + "KIRI - KANAN NAIK.png");
            chef1Sprites[6] = load(p1 + "KANAN - KIRI NAIK.png");
            chef1Sprites[7] = load(p1 + "KANAN - KANAN NAIK.png");

            String p2 = "/chef/WADDLE DEE_";
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
        lastChefSwitchTime = System.currentTimeMillis(); // Record switch time
        System.out.println("[PLAYER] Switching active chef...");
    }

    private void startGame() {
        // Start the game from stage select
        gameState = GameState.PLAYING;
        lastOrderTime = System.currentTimeMillis();
        System.out.println("[GAME] Starting game...");
    }

    private void resetGame() {
        // Complete reset back to initial state
        orderManager.reset();

        // Reset chefs
        chefs.clear();
        chefs.add(new Chef("C1", "Kirby", 6, 2));
        chefs.add(new Chef("C2", "Waddle Dee", 8, 5));
        activeChefIndex = 0;
        lastChefSwitchTime = 0;

        // TODO: Reset map state - need to implement resetAllStations() in GameMap

        // Generate initial orders
        orderManager.generateOrder();
        orderManager.generateOrder();
        lastOrderTime = System.currentTimeMillis();

        System.out.println("[GAME] Game reset complete");
    }

    private void restartGame() {
        // Quick restart - stay in game
        resetGame();
        gameState = GameState.PLAYING;
        System.out.println("[GAME] Restarting game...");
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
        // Only update game logic if playing
        if (gameState != GameState.PLAYING) {
            return;
        }

        for (Chef c : chefs)
            c.update();

        // Update stations (for cooking/cutting timers)
        frameCounter++;
        if (frameCounter % 60 == 0) { // Update every second
            updateStations();
        }

        // Generate new orders periodically
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastOrderTime > orderInterval && orderManager.getActiveOrders().size() < 3) {
            orderManager.generateOrder();
            lastOrderTime = currentTime;
            System.out.println("[ORDER] New order generated!");
        }

        // Update order timers
        orderManager.update(1.0f / 60.0f);

        // Check win/lose conditions
        int currentScore = orderManager.getScore();
        int completedOrders = orderManager.getCompletedOrders();

        // Debug: Log check every 5 seconds
        if (frameCounter % 300 == 0) {
            System.out.println(
                    "[GAME] Check - Orders: " + completedOrders + "/" + WIN_COMPLETED_ORDERS + ", Score: "
                            + currentScore + " (Lose: <=" + LOSE_SCORE + ")");
        }

        if (completedOrders >= WIN_COMPLETED_ORDERS) {
            gameState = GameState.WIN;
            System.out
                    .println("[GAME] YOU WIN! Completed " + completedOrders + " orders! Final Score: " + currentScore);
        } else if (currentScore <= LOSE_SCORE) {
            gameState = GameState.LOSE;
            System.out.println("[GAME] YOU LOSE! Final Score: " + currentScore);
        }
    }

    private void updateStations() {
        long currentTime = System.currentTimeMillis();
        // Update all stations
        for (int row = 0; row < gameMap.getRows(); row++) {
            for (int col = 0; col < gameMap.getCols(); col++) {
                nimonscooked.entity.station.Station station = gameMap.getStationAt(col, row);
                if (station instanceof nimonscooked.entity.station.CuttingStation) {
                    nimonscooked.entity.station.CuttingStation cuttingStation = (nimonscooked.entity.station.CuttingStation) station;
                    if (cuttingStation.getContainedItem() != null) {
                        cuttingStation.processCut(1000); // Process 1 second worth of cutting
                    }
                } else if (station instanceof nimonscooked.entity.station.WashingStation) {
                    nimonscooked.entity.station.WashingStation washingStation = (nimonscooked.entity.station.WashingStation) station;
                    washingStation.processWash(1000); // Process 1 second worth of washing
                } else if (station instanceof nimonscooked.entity.station.CookingStation) {
                    nimonscooked.entity.station.CookingStation cookingStation = (nimonscooked.entity.station.CookingStation) station;
                    // Auto-start cooking jika ada item dan belum cooking
                    if (!cookingStation.isCooking() && cookingStation.getContainedItem() != null) {
                        cookingStation.startCooking(currentTime);
                    }
                    cookingStation.update(currentTime);
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Route rendering based on game state
        switch (gameState) {
            case MENU:
                drawMainMenu(g2);
                break;
            case HOW_TO_PLAY:
                drawHowToPlay(g2);
                break;
            case STAGE_SELECT:
                drawStageSelect(g2);
                break;
            case PLAYING:
                drawGame(g2);
                break;
            case WIN:
            case LOSE:
                drawGame(g2); // Draw game in background
                drawGameOverScreen(g2);
                break;
        }

        g2.dispose();
    }

    private void drawGame(Graphics2D g2) {
        // Draw black background for top margin (order panel area)
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, screenWidth, topMargin);

        // Draw Map (offset by topMargin)
        char[][] grid = gameMap.getGrid();
        for (int row = 0; row < gameMap.getRows(); row++) {
            for (int col = 0; col < gameMap.getCols(); col++) {
                int x = col * tileSize;
                int y = row * tileSize + topMargin; // Offset map down
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

                // Draw progress bars for stations
                nimonscooked.entity.station.Station station = gameMap.getStationAt(col, row);
                if (station instanceof nimonscooked.entity.station.CuttingStation) {
                    nimonscooked.entity.station.CuttingStation cuttingStation = (nimonscooked.entity.station.CuttingStation) station;
                    if (cuttingStation.getContainedItem() != null &&
                            cuttingStation.getSavedTime() > 0) {
                        drawProgressBar(g2, x, y, cuttingStation.getSavedTime(),
                                cuttingStation.getCuttingDurationMs(), new Color(70, 130, 180));
                    }
                } else if (station instanceof nimonscooked.entity.station.CookingStation) {
                    nimonscooked.entity.station.CookingStation cookingStation = (nimonscooked.entity.station.CookingStation) station;
                    if (cookingStation.getContainedItem() != null && cookingStation.isCooking()) {
                        float progress = cookingStation.getCookingProgress();
                        Color barColor = progress < 0.8f ? new Color(255, 165, 0) : new Color(255, 69, 0);
                        drawProgressBar(g2, x, y, (int) (progress * 100), 100, barColor);
                    }
                }
            }
        }

        // Draw Chefs (offset by topMargin)
        for (int i = 0; i < chefs.size(); i++) {
            Chef c = chefs.get(i);
            int px = c.getVisualX();
            int py = c.getVisualY() + topMargin; // Offset chef position down

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

                // --- RENDER HELD ITEM ---
                if (c.getInventory() != null) {
                    BufferedImage itemImage = c.getInventory().getSprite();
                    if (itemImage != null) {
                        // Draw item above chef's head (smaller size)
                        int itemSize = tileSize / 2;
                        int itemX = px + (tileSize - itemSize) / 2;
                        int itemY = py - itemSize / 2;

                        // Draw white background circle for item
                        g2.setColor(new Color(255, 255, 255, 200));
                        g2.fillOval(itemX - 2, itemY - 2, itemSize + 4, itemSize + 4);

                        // Draw item
                        g2.drawImage(itemImage, itemX, itemY, itemSize, itemSize, null);
                    } else {
                        // Fallback: draw colored square if no image
                        int itemSize = tileSize / 2;
                        int itemX = px + (tileSize - itemSize) / 2;
                        int itemY = py - itemSize / 2;

                        g2.setColor(new Color(255, 255, 255, 200));
                        g2.fillRect(itemX - 2, itemY - 2, itemSize + 4, itemSize + 4);
                        g2.setColor(new Color(100, 200, 100));
                        g2.fillRect(itemX, itemY, itemSize, itemSize);

                        // Draw item initial
                        g2.setColor(Color.BLACK);
                        g2.setFont(new Font("Arial", Font.BOLD, 10));
                        String itemName = c.getInventory().getName();
                        String initial = itemName.length() > 0 ? itemName.substring(0, Math.min(3, itemName.length()))
                                : "?";
                        g2.drawString(initial, itemX + 4, itemY + 14);
                    }
                }
                // -----------------------

                // --- INDIKATOR AKTIF (SHINY YELLOW) ---
                // Hanya muncul 3 detik setelah switch chef
                if (i == activeChefIndex && !c.isMoving()) {
                    long timeSinceSwitch = System.currentTimeMillis() - lastChefSwitchTime;
                    if (timeSinceSwitch < CHEF_INDICATOR_DURATION) {
                        // Glow Luar
                        g2.setColor(shinyGoldGlow);
                        g2.setStroke(new BasicStroke(5));
                        g2.drawRect(px - 2, py - 2, tileSize + 4, tileSize + 4);

                        // Border Dalam (Solid)
                        g2.setColor(shinyGold);
                        g2.setStroke(new BasicStroke(2));
                        g2.drawRect(px, py, tileSize, tileSize);
                    }
                }
                // --------------------------------------
            } else {
                g2.setColor(i == activeChefIndex ? Color.WHITE : Color.GRAY);
                g2.fillRect(px, py, tileSize, tileSize);
            }
        }

        // Draw items on floor
        for (int row = 0; row < gameMap.getRows(); row++) {
            for (int col = 0; col < gameMap.getCols(); col++) {
                if (gameMap.hasItemOnFloor(col, row)) {
                    nimonscooked.entity.item.Item floorItem = gameMap.getItemOnFloor(col, row);
                    if (floorItem != null) {
                        int x = col * tileSize;
                        int y = row * tileSize + topMargin;

                        // Draw item sprite or colored circle
                        if (floorItem.getSprite() != null) {
                            g2.drawImage(floorItem.getSprite(), x + 8, y + 8, tileSize - 16, tileSize - 16, null);
                        } else {
                            g2.setColor(new Color(255, 200, 0));
                            g2.fillOval(x + 12, y + 12, tileSize - 24, tileSize - 24);
                        }

                        // Draw indicator
                        g2.setColor(new Color(255, 255, 255, 150));
                        g2.setStroke(new BasicStroke(2));
                        g2.drawOval(x + 10, y + 10, tileSize - 20, tileSize - 20);
                    }
                }
            }
        }

        // --- UI OVERLAY ---
        drawOrders(g2);
        drawUI(g2);
    }

    private void drawProgressBar(Graphics2D g2, int x, int y, int current, int max, Color barColor) {
        int barWidth = tileSize - 8;
        int barHeight = 6;
        int barX = x + 4;
        int barY = y + tileSize - 10;

        // Background
        g2.setColor(new Color(50, 50, 50));
        g2.fillRect(barX, barY, barWidth, barHeight);

        // Progress
        float progress = Math.min(1.0f, (float) current / max);
        int fillWidth = (int) (barWidth * progress);
        g2.setColor(barColor);
        g2.fillRect(barX, barY, fillWidth, barHeight);

        // Border
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(1));
        g2.drawRect(barX, barY, barWidth, barHeight);
    }

    private void drawOrders(Graphics2D g2) {
        java.util.List<nimonscooked.entity.order.Order> orders = orderManager.getActiveOrders();
        if (orders.isEmpty())
            return;

        // Horizontal strip in top margin
        int orderWidth = (screenWidth - 10) / Math.max(orders.size(), 1);
        int orderHeight = topMargin - 4;
        int startY = 2;

        // Background strip
        g2.setColor(new Color(30, 30, 30, 200));
        g2.fillRect(0, 0, screenWidth, topMargin);
        g2.setColor(new Color(255, 215, 0, 100));
        g2.fillRect(0, topMargin - 2, screenWidth, 2);

        // SCOREBOARD - kanan atas (tidak menimpa orders)
        int scoreBoxWidth = 150;
        int scoreBoxX = screenWidth - scoreBoxWidth - 5;

        // Background box untuk score
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRoundRect(scoreBoxX, 2, scoreBoxWidth, topMargin - 4, 5, 5);

        // Border
        g2.setColor(new Color(255, 215, 0));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(scoreBoxX, 2, scoreBoxWidth, topMargin - 4, 5, 5);

        // Score text
        g2.setFont(new Font("SansSerif", Font.BOLD, 12));
        String scoreText = "SCORE: " + orderManager.getScore();
        g2.drawString(scoreText, scoreBoxX + 10, 14);

        // Stats
        g2.setFont(new Font("SansSerif", Font.PLAIN, 8));
        g2.setColor(new Color(100, 255, 100));
        g2.drawString("✓ " + orderManager.getCompletedOrders(), scoreBoxX + 10, 24);
        g2.setColor(new Color(255, 100, 100));
        g2.drawString("✗ " + orderManager.getExpiredOrders(), scoreBoxX + 70, 24);

        for (int i = 0; i < orders.size(); i++) {
            nimonscooked.entity.order.Order order = orders.get(i);
            int orderX = i * orderWidth + 2;

            // Determine color based on time remaining
            float timeRatio = order.getRemainingTime() / order.getDuration();
            Color indicatorColor;
            if (timeRatio > 0.5f) {
                indicatorColor = new Color(100, 255, 100); // Green
            } else if (timeRatio > 0.25f) {
                indicatorColor = new Color(255, 255, 100); // Yellow
            } else {
                indicatorColor = new Color(255, 100, 100); // Red
            }

            // Order box
            g2.setColor(new Color(50, 50, 50, 220));
            g2.fillRect(orderX, startY, orderWidth - 4, orderHeight);

            // Top indicator bar
            g2.setColor(indicatorColor);
            g2.fillRect(orderX, startY, orderWidth - 4, 2);

            // Order info - horizontal layout
            int textX = orderX + 6;
            int textY = startY + 11;

            // Order number
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 10));
            g2.drawString("#" + order.getId(), textX, textY);

            // Recipe name
            g2.setFont(new Font("SansSerif", Font.PLAIN, 8));
            String recipeName = order.getRecipe().getName().replace(" Burger", "");
            g2.drawString(recipeName, textX + 20, textY);

            // Requirements - horizontal dengan spacing jelas
            g2.setFont(new Font("Monospaced", Font.PLAIN, 8));
            g2.setColor(new Color(200, 200, 200));

            StringBuilder ingredients = new StringBuilder();
            for (nimonscooked.entity.order.Recipe.Requirement r : order.getRecipe().getRequirements()) {
                // Singkatan ingredient
                String ingShort = "";
                switch (r.name) {
                    case "Bun":
                        ingShort = "Bun";
                        break;
                    case "Meat":
                        ingShort = "Meat";
                        break;
                    case "Cheese":
                        ingShort = "Ches";
                        break;
                    case "Lettuce":
                        ingShort = "Lett";
                        break;
                    case "Tomato":
                        ingShort = "Toma";
                        break;
                }

                // Simbol state - ASCII only
                String stateSymbol = "";
                switch (r.state) {
                    case RAW:
                        stateSymbol = "";
                        break;
                    case CHOPPED:
                        stateSymbol = "*";
                        break;
                    case COOKING:
                        stateSymbol = "~";
                        break;
                    case COOKED:
                        stateSymbol = "+";
                        break;
                    case BURNED:
                        stateSymbol = "X";
                        break;
                }

                ingredients.append(ingShort).append(stateSymbol).append("  ");
            }
            g2.drawString(ingredients.toString().trim(), textX, textY + 10);

            // Time
            g2.setColor(indicatorColor);
            g2.setFont(new Font("SansSerif", Font.BOLD, 8));
            g2.drawString(String.format("%.0fs", order.getRemainingTime()), textX, textY + 18);
        }
    }

    private void drawUI(Graphics2D g2) {
        // Draw semi-transparent panel at bottom
        int uiHeight = 80;
        int uiY = screenHeight - uiHeight;
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, uiY, screenWidth, uiHeight);

        // Draw border
        g2.setColor(new Color(255, 215, 0));
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(0, uiY, screenWidth, uiHeight);

        // Draw info for each chef
        int chefUIWidth = screenWidth / chefs.size();
        for (int i = 0; i < chefs.size(); i++) {
            Chef chef = chefs.get(i);
            int uiX = i * chefUIWidth;

            // Highlight active chef (only show for 3 seconds after switch)
            if (i == activeChefIndex) {
                long timeSinceSwitch = System.currentTimeMillis() - lastChefSwitchTime;
                if (timeSinceSwitch < CHEF_INDICATOR_DURATION) {
                    g2.setColor(new Color(255, 215, 0, 100));
                    g2.fillRect(uiX + 5, uiY + 5, chefUIWidth - 10, uiHeight - 10);
                }
            }

            // Draw chef name
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString(chef.getName(), uiX + 15, uiY + 25);

            // Draw held item info
            if (chef.getInventory() != null) {
                // Draw item sprite
                BufferedImage itemSprite = chef.getInventory().getSprite();
                int itemSize = 40;
                int itemX = uiX + 15;
                int itemY = uiY + 30;

                // Background
                g2.setColor(new Color(255, 255, 255, 230));
                g2.fillRoundRect(itemX - 2, itemY - 2, itemSize + 4, itemSize + 4, 5, 5);

                if (itemSprite != null) {
                    g2.drawImage(itemSprite, itemX, itemY, itemSize, itemSize, null);
                } else {
                    g2.setColor(new Color(100, 200, 100));
                    g2.fillRect(itemX, itemY, itemSize, itemSize);
                }

                // Draw item name
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.PLAIN, 12));
                String itemName = chef.getInventory().getName();
                g2.drawString(itemName, itemX + itemSize + 8, itemY + 15);

                // Draw ingredient state if applicable
                if (chef.getInventory() instanceof nimonscooked.entity.item.ingredient.Ingredient) {
                    nimonscooked.entity.item.ingredient.Ingredient ing = (nimonscooked.entity.item.ingredient.Ingredient) chef
                            .getInventory();
                    g2.setFont(new Font("Arial", Font.ITALIC, 10));
                    g2.setColor(new Color(200, 200, 200));
                    g2.drawString(ing.getState().toString(), itemX + itemSize + 8, itemY + 30);
                }
            } else {
                // No item held
                g2.setColor(new Color(150, 150, 150));
                g2.setFont(new Font("Arial", Font.ITALIC, 12));
                g2.drawString("Empty hands", uiX + 15, uiY + 50);
            }
        }

        // Draw controls hint
        g2.setColor(new Color(200, 200, 200));
        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        g2.drawString("WASD: Move | V: Interact | TAB: Switch Chef", 10, screenHeight - 5);
    }

    private void drawGameOverScreen(Graphics2D g2) {
        System.out.println("[RENDER] drawGameOverScreen() called! State: " + gameState);

        // Semi-transparent overlay
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(0, 0, screenWidth, screenHeight);

        System.out.println("[RENDER] Drew black overlay");

        // Title
        g2.setFont(new Font("Arial", Font.BOLD, 72));
        String title = (gameState == GameState.WIN) ? "YOU WIN!" : "YOU LOSE!";
        Color titleColor = (gameState == GameState.WIN) ? new Color(255, 215, 0) : new Color(255, 50, 50);
        g2.setColor(titleColor);

        FontMetrics fm = g2.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        g2.drawString(title, (screenWidth - titleWidth) / 2, screenHeight / 2 - 100);

        // Final score
        g2.setFont(new Font("Arial", Font.PLAIN, 32));
        String scoreText = "Final Score: " + orderManager.getScore();
        int scoreWidth = g2.getFontMetrics().stringWidth(scoreText);
        g2.setColor(Color.WHITE);
        g2.drawString(scoreText, (screenWidth - scoreWidth) / 2, screenHeight / 2 - 20);

        // Stats
        g2.setFont(new Font("Arial", Font.PLAIN, 24));
        String statsText = "Completed: " + orderManager.getCompletedOrders() + " | Expired: "
                + orderManager.getExpiredOrders();
        int statsWidth = g2.getFontMetrics().stringWidth(statsText);
        g2.drawString(statsText, (screenWidth - statsWidth) / 2, screenHeight / 2 + 30);

        // Try Again button
        int buttonWidth = 200;
        int buttonHeight = 60;
        int buttonX = (screenWidth - buttonWidth) / 2;
        int buttonY = screenHeight / 2 + 80;

        tryAgainButton = new Rectangle(buttonX, buttonY, buttonWidth, buttonHeight);

        // Button background
        g2.setColor(new Color(50, 150, 50));
        g2.fillRoundRect(buttonX, buttonY, buttonWidth, buttonHeight, 15, 15);

        // Button border
        g2.setColor(new Color(100, 255, 100));
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(buttonX, buttonY, buttonWidth, buttonHeight, 15, 15);

        // Button text
        g2.setFont(new Font("Arial", Font.BOLD, 28));
        String buttonText = "Try Again";
        int buttonTextWidth = g2.getFontMetrics().stringWidth(buttonText);
        g2.setColor(Color.WHITE);
        g2.drawString(buttonText, buttonX + (buttonWidth - buttonTextWidth) / 2, buttonY + 38);

        // Back to Menu button
        int backButtonY = buttonY + 70;
        backButton = new Rectangle(buttonX, backButtonY, buttonWidth, buttonHeight);
        g2.setColor(new Color(100, 100, 100));
        g2.fillRoundRect(buttonX, backButtonY, buttonWidth, buttonHeight, 15, 15);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(buttonX, backButtonY, buttonWidth, buttonHeight, 15, 15);

        String backText = "Back to Menu";
        int backTextWidth = g2.getFontMetrics().stringWidth(backText);
        g2.drawString(backText, buttonX + (buttonWidth - backTextWidth) / 2, backButtonY + 38);

        // Instruction
        g2.setFont(new Font("Arial", Font.ITALIC, 18));
        g2.setColor(new Color(200, 200, 200));
        String instruction = "Click button or press SPACE to restart";
        int instrWidth = g2.getFontMetrics().stringWidth(instruction);
        g2.drawString(instruction, (screenWidth - instrWidth) / 2, screenHeight - 40);
    }

    private void drawMainMenu(Graphics2D g2) {
        // Draw menu background image if available
        if (menuBackground != null) {
            // Scale image to fit screen
            g2.drawImage(menuBackground, 0, 0, screenWidth, screenHeight, null);

            // Define button hitboxes and text based on image layout
            // Buttons are centered horizontally
            int buttonWidth = 385;
            int buttonHeight = 70;
            int buttonX = (screenWidth - buttonWidth) / 2; // Center buttons

            // Start Game button (green, top)
            int startY = 382;
            startButton = new Rectangle(buttonX, startY, buttonWidth, buttonHeight);

            // How to Play button (blue, middle)
            int howToPlayY = 486;
            howToPlayButton = new Rectangle(buttonX, howToPlayY, buttonWidth, buttonHeight);

            // Exit button (red, bottom)
            int exitY = 588;
            exitButton = new Rectangle(buttonX, exitY, buttonWidth, buttonHeight);

            // Draw hover/press effects
            drawButtonEffect(g2, startButton, new Color(50, 205, 50, 100));
            drawButtonEffect(g2, howToPlayButton, new Color(30, 144, 255, 100));
            drawButtonEffect(g2, exitButton, new Color(220, 20, 60, 100));

            // Draw button text with custom font and colors
            g2.setFont(new Font("Arial", Font.BOLD, 36));
            FontMetrics fm = g2.getFontMetrics();

            // Start button text - bright yellow/gold to pop against background
            g2.setColor(new Color(255, 255, 100)); // Bright yellow
            String startText = "START GAME";
            int startTextWidth = fm.stringWidth(startText);
            g2.drawString(startText, buttonX + (buttonWidth - startTextWidth) / 2, startY + 45);

            // How to Play button text - cyan/light blue
            g2.setColor(new Color(100, 255, 255)); // Bright cyan
            String howToPlayText = "HOW TO PLAY";
            int howToPlayTextWidth = fm.stringWidth(howToPlayText);
            g2.drawString(howToPlayText, buttonX + (buttonWidth - howToPlayTextWidth) / 2, howToPlayY + 45);

            // Exit button text - bright red/pink
            g2.setColor(new Color(255, 100, 150)); // Bright pink
            String exitText = "EXIT";
            int exitTextWidth = fm.stringWidth(exitText);
            g2.drawString(exitText, buttonX + (buttonWidth - exitTextWidth) / 2, exitY + 45);

        } else {
            // Fallback: Draw original menu if image not loaded
            g2.setColor(new Color(255, 228, 196));
            g2.fillRect(0, 0, screenWidth, screenHeight);

            g2.setFont(new Font("Arial", Font.BOLD, 80));
            g2.setColor(new Color(255, 100, 50));
            String title = "NIMONSCOOKED";
            int titleWidth = g2.getFontMetrics().stringWidth(title);
            g2.drawString(title, (screenWidth - titleWidth) / 2, 150);

            g2.setFont(new Font("Arial", Font.ITALIC, 24));
            g2.setColor(new Color(100, 100, 100));
            String subtitle = "Kelompok N - OOP Project";
            int subtitleWidth = g2.getFontMetrics().stringWidth(subtitle);
            g2.drawString(subtitle, (screenWidth - subtitleWidth) / 2, 190);

            int buttonWidth = 300;
            int buttonHeight = 60;
            int buttonX = (screenWidth - buttonWidth) / 2;
            int startY = 280;
            int spacing = 80;

            startButton = new Rectangle(buttonX, startY, buttonWidth, buttonHeight);
            g2.setColor(new Color(50, 205, 50));
            g2.fillRoundRect(buttonX, startY, buttonWidth, buttonHeight, 20, 20);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(buttonX, startY, buttonWidth, buttonHeight, 20, 20);
            g2.setFont(new Font("Arial", Font.BOLD, 32));
            String startText = "Start Game";
            int startTextWidth = g2.getFontMetrics().stringWidth(startText);
            g2.drawString(startText, buttonX + (buttonWidth - startTextWidth) / 2, startY + 40);

            int howToPlayY = startY + spacing;
            howToPlayButton = new Rectangle(buttonX, howToPlayY, buttonWidth, buttonHeight);
            g2.setColor(new Color(30, 144, 255));
            g2.fillRoundRect(buttonX, howToPlayY, buttonWidth, buttonHeight, 20, 20);
            g2.setColor(Color.WHITE);
            g2.drawRoundRect(buttonX, howToPlayY, buttonWidth, buttonHeight, 20, 20);
            String howToPlayText = "How to Play";
            int howToPlayTextWidth = g2.getFontMetrics().stringWidth(howToPlayText);
            g2.drawString(howToPlayText, buttonX + (buttonWidth - howToPlayTextWidth) / 2, howToPlayY + 40);

            int exitY = howToPlayY + spacing;
            exitButton = new Rectangle(buttonX, exitY, buttonWidth, buttonHeight);
            g2.setColor(new Color(220, 20, 60));
            g2.fillRoundRect(buttonX, exitY, buttonWidth, buttonHeight, 20, 20);
            g2.setColor(Color.WHITE);
            g2.drawRoundRect(buttonX, exitY, buttonWidth, buttonHeight, 20, 20);
            String exitText = "Exit";
            int exitTextWidth = g2.getFontMetrics().stringWidth(exitText);
            g2.drawString(exitText, buttonX + (buttonWidth - exitTextWidth) / 2, exitY + 40);
        }
    }

    private void drawButtonEffect(Graphics2D g2, Rectangle button, Color baseColor) {
        if (button == null)
            return;

        // Draw pressed effect (darker overlay + slight offset)
        if (button == pressedButton) {
            g2.setColor(new Color(0, 0, 0, 100));
            g2.fillRoundRect(button.x + 2, button.y + 2, button.width, button.height, 15, 15);
        }
        // Draw hover effect (lighter overlay + glow)
        else if (button == hoveredButton) {
            g2.setColor(new Color(255, 255, 255, 80));
            g2.fillRoundRect(button.x, button.y, button.width, button.height, 15, 15);

            // Add glow border
            g2.setColor(new Color(255, 255, 255, 150));
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(button.x, button.y, button.width, button.height, 15, 15);
        }
    }

    private void drawHowToPlay(Graphics2D g2) {
        // Background
        g2.setColor(new Color(240, 248, 255));
        g2.fillRect(0, 0, screenWidth, screenHeight);

        // Title
        g2.setFont(new Font("Arial", Font.BOLD, 48));
        g2.setColor(new Color(30, 144, 255));
        String title = "How to Play";
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, (screenWidth - titleWidth) / 2, 80);

        // Instructions
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.setColor(Color.BLACK);
        int startY = 150;
        int lineHeight = 35;

        String[] instructions = {
                "Controls:",
                "  WASD - Move your chef",
                "  V - Interact (pick/place/use items)",
                "  C - Drop item",
                "  B - Switch between chefs",
                "  F - Serve order from plate",
                "  X - Trash item",
                "",
                "Objective:",
                "  • Prepare orders correctly and quickly",
                "  • Complete 2 orders to WIN",
                "  • Score drops to -10: GAME OVER",
                "",
                "Tips:",
                "  • Raw ingredients need preparation",
                "  • Watch cooking times to avoid burning",
                "  • Complete orders before timer expires"
        };

        for (int i = 0; i < instructions.length; i++) {
            g2.drawString(instructions[i], 100, startY + i * lineHeight);
        }

        // Back Button
        int buttonWidth = 200;
        int buttonHeight = 50;
        int buttonX = (screenWidth - buttonWidth) / 2;
        int buttonY = screenHeight - 100;

        backButton = new Rectangle(buttonX, buttonY, buttonWidth, buttonHeight);
        g2.setColor(new Color(100, 100, 100));
        g2.fillRoundRect(buttonX, buttonY, buttonWidth, buttonHeight, 15, 15);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(buttonX, buttonY, buttonWidth, buttonHeight, 15, 15);

        g2.setFont(new Font("Arial", Font.BOLD, 24));
        String backText = "Back";
        int backTextWidth = g2.getFontMetrics().stringWidth(backText);
        g2.drawString(backText, buttonX + (buttonWidth - backTextWidth) / 2, buttonY + 33);
    }

    private void drawStageSelect(Graphics2D g2) {
        // Background
        g2.setColor(new Color(255, 250, 205));
        g2.fillRect(0, 0, screenWidth, screenHeight);

        // Title
        g2.setFont(new Font("Arial", Font.BOLD, 56));
        g2.setColor(new Color(255, 140, 0));
        String title = "Select Stage";
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, (screenWidth - titleWidth) / 2, 100);

        // Stage info
        g2.setFont(new Font("Arial", Font.PLAIN, 24));
        g2.setColor(Color.BLACK);
        String[] info = {
                "Map: C - Burger Kitchen",
                "Win: Complete 2 orders",
                "Lose: Score drops to -10",
                "Recipes: Classic, Cheeseburger, BLT, Deluxe"
        };

        int infoY = 200;
        for (int i = 0; i < info.length; i++) {
            int infoWidth = g2.getFontMetrics().stringWidth(info[i]);
            g2.drawString(info[i], (screenWidth - infoWidth) / 2, infoY + i * 40);
        }

        // Select button
        int buttonWidth = 250;
        int buttonHeight = 60;
        int buttonX = (screenWidth - buttonWidth) / 2;
        int selectY = 380;

        selectStageButton = new Rectangle(buttonX, selectY, buttonWidth, buttonHeight);
        g2.setColor(new Color(50, 205, 50));
        g2.fillRoundRect(buttonX, selectY, buttonWidth, buttonHeight, 20, 20);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(buttonX, selectY, buttonWidth, buttonHeight, 20, 20);

        g2.setFont(new Font("Arial", Font.BOLD, 32));
        String selectText = "START!";
        int selectTextWidth = g2.getFontMetrics().stringWidth(selectText);
        g2.drawString(selectText, buttonX + (buttonWidth - selectTextWidth) / 2, selectY + 40);

        // Back button
        int backY = selectY + 80;
        backButton = new Rectangle(buttonX, backY, buttonWidth, buttonHeight);
        g2.setColor(new Color(100, 100, 100));
        g2.fillRoundRect(buttonX, backY, buttonWidth, buttonHeight, 20, 20);
        g2.setColor(Color.WHITE);
        g2.drawRoundRect(buttonX, backY, buttonWidth, buttonHeight, 20, 20);

        g2.setFont(new Font("Arial", Font.BOLD, 28));
        String backText = "Back";
        int backTextWidth = g2.getFontMetrics().stringWidth(backText);
        g2.drawString(backText, buttonX + (buttonWidth - backTextWidth) / 2, backY + 38);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // Handle menu navigation
        if (gameState == GameState.HOW_TO_PLAY && keyCode == KeyEvent.VK_ESCAPE) {
            gameState = GameState.MENU;
            return;
        }

        if (gameState == GameState.STAGE_SELECT && keyCode == KeyEvent.VK_ESCAPE) {
            gameState = GameState.MENU;
            return;
        }

        // Handle game over/win screens
        if (gameState == GameState.WIN || gameState == GameState.LOSE) {
            if (keyCode == KeyEvent.VK_SPACE) {
                restartGame();
            } else if (keyCode == KeyEvent.VK_ESCAPE) {
                gameState = GameState.MENU;
                resetGame();
            }
            return;
        }

        // Handle in-game controls
        if (gameState == GameState.PLAYING) {
            if (keyCode == KeyEvent.VK_ESCAPE) {
                gameState = GameState.MENU;
                resetGame();
            } else {
                inputHandler.handleInput(keyCode, getActiveChef());
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}