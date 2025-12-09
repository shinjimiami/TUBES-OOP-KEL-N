package nimonscooked.main;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        // Print controls to console
        System.out.println("=== NIMONSCOOKED - CONTROLS ===");
        System.out.println("Movement: W/A/S/D");
        System.out.println("Interact with station: V");
        System.out.println("Pick up/Drop item: C");
        System.out.println("Switch chef: B");
        System.out.println("Start/Stop cooking: F");
        System.out.println("Manual cut (optional): X");
        System.out.println("================================\n");

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Nimonscooked - Burger Map");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.startGameThread();
    }
}
