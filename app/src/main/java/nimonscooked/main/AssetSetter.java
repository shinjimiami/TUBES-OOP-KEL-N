package nimonscooked.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
        // atur
    }

    private BufferedImage load(String path) {
        try {
            java.io.InputStream is = getClass().getResourceAsStream(path);
            if (is == null) {
                System.err.println("[ASSET] Not found: " + path);
                return null;
            }
            return javax.imageio.ImageIO.read(is);
        } catch (Exception e) {
            System.err.println("[ASSET] Error loading: " + path + " -> " + e.getMessage());
            return null;
        }
    }

    private BufferedImage createPlaceholder(String text) {
        int size = Math.max(24, gp.tileSize);
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(180, 180, 180));
        g.fillRect(0, 0, size, size);
        g.setColor(new Color(80, 80, 80));
        g.setFont(new Font("daydream", Font.PLAIN, Math.max(10, size / 4)));
        int w = g.getFontMetrics().stringWidth(text);
        g.drawString(text, Math.max(2, (size - w) / 2), size / 2 + 4);
        g.dispose();
        return img;
    }

    public void setAssets() {
        try {
            gp.menuBackground = load("/menu/start.png");
            gp.stageSelectBackground = load("/menu/stage_select.png");
            gp.winScreenBackground = load("/menu/win_screen.png");
            gp.loseScreenBackground = load("/menu/lose_screen.png");
            gp.mapBackground = load("/maps/map_d.png");
            gp.pauseOverlayImage = load("/menu/pause.png");
            gp.pauseControlsOverlayImage = load("/menu/pause_controls.png");
            gp.startHowToPlay = load("/menu/start_howtoplay.png");
            gp.startCredits = load("/menu/start_credits.png");

            // Load timer background (optional - will use default if not found)
            gp.timerBackground = load("/ui/timer_background.png");
            if (gp.timerBackground != null) {
                System.out.println("[UI]  Timer background loaded successfully!");
            } else {
                System.out.println("[UI]  No custom timer background - using default box");
            }

            // Load map C thumbnail for stage select
            gp.mapCThumbnail = load("/maps/thumbnails/map_c_thumbnail.png");
            if (gp.mapCThumbnail != null) {
                System.out.println("[UI] ✅ Map C thumbnail loaded successfully!");
            } else {
                System.out.println("[UI] ⚠️ Map C thumbnail not found");
            }

            // Try to load chef sprites using actual resource filenames present in
            // resources/chef
            String name1 = gp.chefs.size() > 0 ? gp.chefs.get(0).getName().toUpperCase() : "KIRBY";
            String name2 = gp.chefs.size() > 1 ? gp.chefs.get(1).getName().toUpperCase() : "WADDLE DEE";

            String[] kirbyOrder = new String[] {
                    "BELAKANG - KIRI NAIK.png", "BELAKANG - KANAN NAIK.png",
                    "DEPAN - KIRI NAIK.png", "DEPAN - KANAN NAIK.png",
                    "KIRI - KIRI NAIK.png", "KIRI - KANAN NAIK.png",
                    "KANAN - KIRI NAIK.png", "KANAN - KANAN NAIK.png"
            };

            String[] waddleOrder = new String[] {
                    "BELAKANG - KIRI ATAS.png", "BELAKANG - KANAN ATAS.png",
                    "DEPAN - KIRI ATAS.png", "DEPAN - KANAN ATAS.png",
                    "KIRI - KIRI ATAS.png", "KIRI - KANAN ATAS.png",
                    "KANAN - KIRI ATAS.png", "KANAN - KANAN ATAS.png"
            };

            for (int i = 0; i < 8; i++) {
                gp.chef1Sprites[i] = load("/chef/" + name1 + "_" + kirbyOrder[i]);
                if (gp.chef1Sprites[i] == null) {
                    System.err.println("[ASSET] Missing chef1 asset: /chef/" + name1 + "_" + kirbyOrder[i]);
                }
                gp.chef2Sprites[i] = load("/chef/" + name2 + "_" + waddleOrder[i]);
                if (gp.chef2Sprites[i] == null) {
                    System.err.println("[ASSET] Missing chef2 asset: /chef/" + name2 + "_" + waddleOrder[i]);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
