package nimonscooked.main;

import nimonscooked.entity.Chef;
import nimonscooked.entity.station.*;
import nimonscooked.entity.item.kitchenutensil.FryingPan;
import nimonscooked.enums.Direction;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    // Screen Settings
    final int originalTileSize = 32;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale; // 96px
    public final int maxScreenCol = 14;
    public final int maxScreenRow = 10;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    // System
    Thread gameThread;
    public OrderManager orderManager = new OrderManager();

    // Entities
    public ArrayList<Chef> chefs = new ArrayList<>();
    public int activeChefIndex = 0; // 0 = Chef 1, 1 = Chef 2

    // Map (Grid Station)
    // stationMap[col][row] menyimpan objek Station di koordinat tersebut
    public Station[][] stationMap = new Station[maxScreenCol][maxScreenRow];

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(this);
        this.setFocusable(true);

        setupGame();
    }

    public void setupGame() {
        // 1. Inisialisasi Chefs
        // Posisi spawn sesuai Map C (V) -> (5,3) dan (9,6) kira-kira dari gambar PDF
        chefs.add(new Chef("C1", "Kebin", 6, 3, Color.BLUE));
        chefs.add(new Chef("C2", "Stewart", 8, 6, Color.RED)); // Index 1

        // 2. Inisialisasi Map C (Burger Map) secara Hardcode
        initMapC();
    }

    // layout berdasarkan PDF Page 21 (Map Type C)
    private void initMapC() {
        // Legend:
        // C=Cutting, R=Cooking, A=Assembly, S=Serving, W=Washing, I=Storage, P=Plate,
        // T=Trash, X=Wall

        // Baris 1
        createWall(0, 0);
        createWall(1, 0);
        createWall(2, 0);
        createWall(3, 0);
        createWall(4, 0);
        createStation(new AssemblyStation("A1", 5, 0));
        createStation(new AssemblyStation("A2", 6, 0));
        createStation(new IngredientStorage("I1", 7, 0)); // Storage
        // ... lanjutkan sesuai gambar jika perlu detail persis

        // Contoh penempatan station penting sesuai grid Map C (Simplified untuk demo):

        // R (Cooking) - Ada 4 Frying Pan
        createCookingStation(5, 2);
        createCookingStation(9, 2);
        createCookingStation(5, 5);
        createCookingStation(8, 5);

        // C (Cutting)
        createStation(new CuttingStation("C1", 0, 1));
        createStation(new CuttingStation("C2", 0, 3));

        // I (Ingredient Storage) - Burger ingredients
        // Asumsi: I di (0,2) = Meat, (0,4) = Bun, dll. Perlu diset spesifik
        IngredientStorage meatStore = new IngredientStorage("MeatStore", 0, 2);
        // meatStore.setIngredientType(IngredientType.MEAT); // Perlu method ini di
        // IngredientStorage
        stationMap[0][2] = meatStore;

        // W (Washing)
        createStation(new WashingStation("W1", 2, 7));
        createStation(new WashingStation("W2", 3, 7));

        // S (Serving)
        ServingCounter sc = new ServingCounter("S1", 13, 3);
        sc.setOrderManager(orderManager);
        stationMap[13][3] = sc;

        // T (Trash)
        createStation(new TrashStation("T1", 13, 8));

        // P (Plate)
        createStation(new PlateStorage("P1", 13, 2));

        // Tembok Keliling (X)
        for (int i = 0; i < maxScreenCol; i++) {
            createWall(i, 9); // Bawah
        }
        // ... tambahkan tembok lain sesuai kebutuhan collision
    }

    private void createStation(Station s) {
        stationMap[(int) s.getX()][(int) s.getY()] = s;
    }

    private void createCookingStation(int x, int y) {
        CookingStation cs = new CookingStation("R" + x + y, x, y);
        // Map C mulai dengan Frying Pan di atas kompor
        cs.placeItem(new FryingPan("FP" + x + y, x, y));
        stationMap[x][y] = cs;
    }

    private void createWall(int x, int y) {
        // Kita gunakan Station dummy atau kelas Wall khusus.
        // Disini pakai null check di move() Chef, jadi kita butuh objek penanda
        // Untuk simpel, anggap Station tanpa interaksi = Tembok
        stationMap[x][y] = new Station("Wall", "Wall", x, y) {
            @Override
            public void interact(Chef p) {
            } // Tembok diem aja
        };
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / 60;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            update();
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;
                if (remainingTime < 0)
                    remainingTime = 0;
                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        // Update Orders
        // Kirim deltaTime (approx 0.016s untuk 60FPS)
        orderManager.update(0.016f);

        // Generate new order logic (randomly)
        if (Math.random() < 0.005) { // Kecil chance per frame
            orderManager.generateOrder();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // 1. Draw Map (Grid & Stations)
        for (int x = 0; x < maxScreenCol; x++) {
            for (int y = 0; y < maxScreenRow; y++) {
                // Gambar lantai dasar
                g2.setColor(Color.LIGHT_GRAY);
                g2.drawRect(x * tileSize, y * tileSize, tileSize, tileSize);

                // Gambar Station jika ada
                Station s = stationMap[x][y];
                if (s != null) {
                    if (s.getName().equals("Wall"))
                        g2.setColor(Color.DARK_GRAY);
                    else if (s instanceof CookingStation)
                        g2.setColor(Color.ORANGE);
                    else if (s instanceof CuttingStation)
                        g2.setColor(Color.GREEN);
                    else if (s instanceof ServingCounter)
                        g2.setColor(Color.MAGENTA);
                    else
                        g2.setColor(Color.CYAN);

                    g2.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
                    g2.setColor(Color.BLACK);
                    g2.drawString(s.getName(), x * tileSize + 5, y * tileSize + 20);

                    // Gambar Item di atas Station
                    if (s.getContainedItem() != null) {
                        g2.setColor(Color.YELLOW);
                        g2.fillOval(x * tileSize + 10, y * tileSize + 10, tileSize - 20, tileSize - 20);
                        g2.setColor(Color.BLACK);
                        g2.drawString(s.getContainedItem().getName(), x * tileSize + 15, y * tileSize + 40);
                    }
                }
            }
        }

        // 2. Draw Chefs
        for (Chef c : chefs) {
            c.draw(g2, tileSize);
        }

        // Marker Chef Aktif
        Chef active = chefs.get(activeChefIndex);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(active.getX() * tileSize, active.getY() * tileSize, tileSize, tileSize);

        // 3. Draw UI (Orders)
        g2.setColor(Color.WHITE);
        g2.drawString("Active Orders: " + orderManager.getActiveOrders().size(), 10, 20);
        int yOrd = 40;
        for (var ord : orderManager.getActiveOrders()) {
            g2.drawString("- " + ord.getRecipe().getName(), 10, yOrd);
            yOrd += 20;
        }

        g2.dispose();
    }

    // --- INPUT HANDLING ---
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        Chef activeChef = chefs.get(activeChefIndex);

        // Movement (WASD)
        if (code == KeyEvent.VK_W) {
            activeChef.setDirection(Direction.UP);
            activeChef.move(0, -1, stationMap, maxScreenCol, maxScreenRow);
        }
        if (code == KeyEvent.VK_S) {
            activeChef.setDirection(Direction.DOWN);
            activeChef.move(0, 1, stationMap, maxScreenCol, maxScreenRow);
        }
        if (code == KeyEvent.VK_A) {
            activeChef.setDirection(Direction.LEFT);
            activeChef.move(-1, 0, stationMap, maxScreenCol, maxScreenRow);
        }
        if (code == KeyEvent.VK_D) {
            activeChef.setDirection(Direction.RIGHT);
            activeChef.move(1, 0, stationMap, maxScreenCol, maxScreenRow);
        }

        // Switch Chef (B)
        if (code == KeyEvent.VK_B) {
            activeChefIndex = (activeChefIndex + 1) % chefs.size();
        }

        // Interact / Pick / Drop (V atau Space)
        if (code == KeyEvent.VK_V || code == KeyEvent.VK_SPACE) {
            // Ambil koordinat di depan Chef
            int targetX = activeChef.getFacingPosition().getX();
            int targetY = activeChef.getFacingPosition().getY();

            // Validasi bounds
            if (targetX >= 0 && targetX < maxScreenCol && targetY >= 0 && targetY < maxScreenRow) {
                Station targetStation = stationMap[targetX][targetY];
                if (targetStation != null) {
                    targetStation.interact(activeChef);
                } else {
                    // Drop item di lantai (jika diperbolehkan)
                    // Implementasi drop di lantai bisa disini
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
