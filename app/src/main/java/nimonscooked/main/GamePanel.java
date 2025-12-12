package nimonscooked.main;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.BasicStroke;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;

import nimonscooked.entity.Chef;
// input handling moved to KeyHandler in main
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
    KeyHandler keyHandler;
    AssetSetter assetSetter;
    UI ui;
    SetupGame setupGame;
    CollisionChecker collisionChecker;

    public CollisionChecker getCollisionChecker() {
        return collisionChecker;
    }
    List<Chef> chefs = new ArrayList<>();
    int activeChefIndex = 0;
    long lastChefSwitchTime = 0;
    final long CHEF_INDICATOR_DURATION = 3000; // 3 seconds

    BufferedImage[] chef1Sprites = new BufferedImage[8];
    BufferedImage[] chef2Sprites = new BufferedImage[8];
    BufferedImage menuBackground;
    BufferedImage winScreenBackground;
    BufferedImage loseScreenBackground;
    BufferedImage mapBackground; // Background image for the map
    BufferedImage pauseOverlayImage;

    // Warna Pastel Lantai
    Color pastelOrange = new Color(255, 223, 186);
    Color pastelYellow = new Color(255, 253, 208);

    // Warna Shiny Yellow (Emas)
    Color shinyGold = new Color(255, 215, 0);
    Color shinyGoldGlow = new Color(255, 223, 0, 120); // Transparan

    boolean isPaused = false;
    int pauseSelectedIndex = 0; // 0=Resume, 1=Controls, 2=Quit
    final int PAUSE_MENU_SIZE = 3;
    boolean showPauseControls = false; // Flag to show controls in pause menu
    // Order management
    OrderManager orderManager;
    long lastOrderTime = 0;
    private final long orderInterval = 30000; // Generate order every 30 seconds
    private int frameCounter = 0;

    public Sound sound = new Sound(); // <- SUDAH ADA

    // Game state management
    enum GameState {
        MENU, // Main menu
        HOW_TO_PLAY, // Instructions screen
        STAGE_SELECT, // Stage selection
        PLAYING, // In-game
        WIN, // Win screen
        LOSE // Lose screen
    }

    GameState gameState = GameState.MENU; // Start at menu
    private final int WIN_COMPLETED_ORDERS = 2; // Win after 2 completed orders
    private final int LOSE_SCORE = -10; // Lose condition

    // Menu navigation
    int selectedMenuIndex = 0; // 0=Start, 1=How to Play, 2=Exit
    int maxMenuIndex = 2;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(this);
        this.setFocusable(true);

        chefs.add(new Chef("C1", "Kirby", 6, 2));
        chefs.add(new Chef("C2", "Waddle Dee", 8, 5));

        keyHandler = new KeyHandler(gameMap, this, chefs);
        orderManager = OrderManager.getInstance();

        // Initialize helpers
        assetSetter = new AssetSetter(this);
        assetSetter.setAssets();
        setupGame = new SetupGame(this);
        collisionChecker = new CollisionChecker();
        ui = new UI(this);

        // --- INTEGRASI SOUND: Memulai musik awal ---
        playMusicByState();
    }

    // Asset loading moved to AssetSetter

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
        // Delegate to SetupGame
        if (setupGame != null) {
            setupGame.resetGame();
        }
    }

    private void restartGame() {
        // Quick restart - stay in game
        resetGame();
        gameState = GameState.PLAYING;
        selectedMenuIndex = 0;
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

        // Pause updates while paused or viewing pause controls
        if (isPaused || showPauseControls) {
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
            // --- INTEGRASI SOUND: Win state ---
            playMusicByState();
        } else if (currentScore <= LOSE_SCORE) {
            gameState = GameState.LOSE;
            System.out.println("[GAME] YOU LOSE! Final Score: " + currentScore);
            // --- INTEGRASI SOUND: Lose state ---
            playMusicByState();
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
                        // Anda bisa memanggil sound FRY di sini jika ingin bunyi langsung
                        // playSoundEffect(Sound.FRY);
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
                if (isPaused || showPauseControls) {
                    drawPauseScreen(g2);
                }
                break;
            case WIN:
            case LOSE:
                drawGame(g2); // Draw game in background
                drawGameOverScreen(g2);
                break;
        }

        g2.dispose();
    }

    public void playMusicByState() {
        // Stop any currently playing music before changing
        sound.stop();

        switch (gameState) {
            case MENU:
                sound.playMusic(Sound.TITLE);
                break;
            case PLAYING:
                sound.playMusic(Sound.PLAYING);
                break;
            case WIN:
                sound.playSE(Sound.VICTORY);
                break;
            case LOSE:
                sound.playSE(Sound.GAME_OVER);
                break;
            case HOW_TO_PLAY:
            case STAGE_SELECT:
                sound.playMusic(Sound.LEVEL);
                break;
        }
    }

    public void playSoundEffect(int index) {
        sound.playSE(index);
    }

    private void drawGame(Graphics2D g2) {
        ui.drawGame(g2);
    }

    private void drawPauseScreen(Graphics2D g2) {
        if (showPauseControls) {
            ui.drawControlsScreen(g2);
        } else {
            ui.drawPauseScreen(g2);
        }

    }

    private void drawDefaultPauseMenu(Graphics2D g2) {
        ui.drawDefaultPauseMenu(g2);
    }

    private void drawControlsScreen(Graphics2D g2) {
        ui.drawControlsScreen(g2);
    }

    private void drawProgressBar(Graphics2D g2, int x, int y, int current, int max, Color barColor) {
        ui.drawProgressBar(g2, x, y, current, max, barColor);
    }

    private void drawOrders(Graphics2D g2) {
        ui.drawOrders(g2);
    }

    private void drawUI(Graphics2D g2) {
        ui.drawUI(g2);
    }

    private void drawGameOverScreen(Graphics2D g2) {
        ui.drawGameOverScreen(g2);
    }

    private void drawMainMenu(Graphics2D g2) {
        ui.drawMainMenu(g2);
    }

    private void drawHowToPlay(Graphics2D g2) {
        ui.drawHowToPlay(g2);
    }

    private void drawStageSelect(Graphics2D g2) {
        ui.drawStageSelect(g2);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // Handle MENU state
        if (gameState == GameState.MENU) {
            if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {
                selectedMenuIndex--;
                if (selectedMenuIndex < 0)
                    selectedMenuIndex = maxMenuIndex;
                playSoundEffect(Sound.LOADING); // SE: Pindah pilihan
                repaint();
            } else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {
                selectedMenuIndex++;
                if (selectedMenuIndex > maxMenuIndex)
                    selectedMenuIndex = 0;
                playSoundEffect(Sound.LOADING); // SE: Pindah pilihan
                repaint();
            } else if (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_SPACE) {
                playSoundEffect(Sound.LEVEL); // SE: Konfirmasi pilihan
                switch (selectedMenuIndex) {
                    case 0: // Start Game
                        gameState = GameState.STAGE_SELECT;
                        selectedMenuIndex = 0;
                        maxMenuIndex = 1; // Select Stage / Back
                        playMusicByState(); // Ganti musik jika perlu
                        break;
                    case 1: // How to Play
                        gameState = GameState.HOW_TO_PLAY;
                        playMusicByState();
                        break;
                    case 2: // Exit
                        System.exit(0);
                        break;
                }
                repaint();
            }
            return;
        }

        // Handle HOW_TO_PLAY state
        if (gameState == GameState.HOW_TO_PLAY) {
            if (keyCode == KeyEvent.VK_ESCAPE || keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_SPACE) {
                gameState = GameState.MENU;
                selectedMenuIndex = 0;
                maxMenuIndex = 2;
                playSoundEffect(Sound.LEVEL); // SE: Kembali
                playMusicByState();
                repaint();
            }
            return;
        }

        // Handle STAGE_SELECT state
        if (gameState == GameState.STAGE_SELECT) {
            if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {
                selectedMenuIndex--;
                if (selectedMenuIndex < 0)
                    selectedMenuIndex = maxMenuIndex;
                playSoundEffect(Sound.LOADING); // SE: Pindah pilihan
                repaint();
            } else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {
                selectedMenuIndex++;
                if (selectedMenuIndex > maxMenuIndex)
                    selectedMenuIndex = 0;
                playSoundEffect(Sound.LOADING); // SE: Pindah pilihan
                repaint();
            } else if (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_SPACE) {
                playSoundEffect(Sound.LEVEL); // SE: Konfirmasi pilihan
                if (selectedMenuIndex == 0) {
                    // Start the game
                    startGame();
                    playMusicByState(); // Mulai musik game
                } else {
                    // Back to menu
                    gameState = GameState.MENU;
                    selectedMenuIndex = 0;
                    maxMenuIndex = 2;
                    playMusicByState(); // Kembali ke musik menu
                }
                repaint();
            } else if (keyCode == KeyEvent.VK_ESCAPE) {
                gameState = GameState.MENU;
                selectedMenuIndex = 0;
                maxMenuIndex = 2;
                playSoundEffect(Sound.LEVEL); // SE: Kembali
                playMusicByState(); // Kembali ke musik menu
                repaint();
            }
            return;
        }

        // Handle game over/win screens
        if (gameState == GameState.WIN || gameState == GameState.LOSE) {
            if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {
                selectedMenuIndex = 0; // Try Again
                playSoundEffect(Sound.LOADING); // SE: Pindah pilihan
                repaint();
            } else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {
                selectedMenuIndex = 1; // Back to Menu
                playSoundEffect(Sound.LOADING); // SE: Pindah pilihan
                repaint();
            } else if (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_SPACE) {
                playSoundEffect(Sound.LEVEL); // SE: Konfirmasi pilihan
                if (selectedMenuIndex == 0) {
                    restartGame();
                    playMusicByState(); // Mulai ulang musik game
                } else {
                    gameState = GameState.MENU;
                    selectedMenuIndex = 0;
                    maxMenuIndex = 2;
                    resetGame();
                    playMusicByState(); // Kembali ke musik menu
                }
                repaint();
            } else if (keyCode == KeyEvent.VK_ESCAPE) {
                gameState = GameState.MENU;
                selectedMenuIndex = 0;
                maxMenuIndex = 2;
                resetGame();
                playSoundEffect(Sound.LEVEL); // SE: Kembali
                playMusicByState(); // Kembali ke musik menu
                repaint();
            }
            return;
        }

        if (gameState == GameState.PLAYING) {
            // Enter pause with ESC (do not auto-resume with ESC)
            if (keyCode == KeyEvent.VK_ESCAPE) {
                if (!isPaused) {
                    isPaused = true;
                    pauseSelectedIndex = 0; // Reset to Resume option
                    showPauseControls = false;
                    System.out.println("[GAME] Paused");
                    // --- INTEGRASI SOUND: Musik dihentikan saat pause ---
                    sound.stop();
                } else {
                    // Jika sudah di-pause dan Controls ditampilkan, ESC akan menutup Controls
                }
            } else if (isPaused) {
                if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {
                    pauseSelectedIndex = (pauseSelectedIndex - 1 + PAUSE_MENU_SIZE) % PAUSE_MENU_SIZE;
                    playSoundEffect(Sound.LOADING); // SE: Pindah pilihan
                } else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {
                    pauseSelectedIndex = (pauseSelectedIndex + 1) % PAUSE_MENU_SIZE;
                    playSoundEffect(Sound.LOADING); // SE: Pindah pilihan
                } else if (keyCode == KeyEvent.VK_ENTER) {
                    playSoundEffect(Sound.LEVEL); // SE: Konfirmasi pilihan
                    if (showPauseControls) {
                        showPauseControls = false;
                    } else {
                        handlePauseMenuSelection();
                    }
                } else if (keyCode == KeyEvent.VK_ESCAPE) {
                    if (showPauseControls) {
                        showPauseControls = false;
                        playSoundEffect(Sound.LEVEL); // SE: Kembali
                    }
                }
            } else {
                keyHandler.handleKey(keyCode, getActiveChef());
            }
        }
    }

    private void handlePauseMenuSelection() {
        switch (pauseSelectedIndex) {
            case 0: // Resume
                isPaused = false;
                System.out.println("[GAME] Resumed");
                playMusicByState(); // Lanjutkan musik game
                break;
            case 1: // Controls
                showPauseControls = !showPauseControls;
                break;
            case 2: // Quit -> go back to stage select
                System.out.println("[GAME] Quit level - returning to stage selection");
                // Reset game state for next level selection
                resetGame();
                gameState = GameState.STAGE_SELECT;
                selectedMenuIndex = 0;
                maxMenuIndex = 1; // Stage Select: Select Stage / Back
                isPaused = false;
                showPauseControls = false;
                playMusicByState(); // Ganti musik ke menu/stage select
                repaint();
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}