package nimonscooked.main;

import javax.swing.JFrame;

public class Main {
<<<<<<< HEAD
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Nimonscooked - Burger Map");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

=======

    public static void main(String[] args) {

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Nimonscooked");

        GamePanel gamePanel = new GamePanel();
        // gamePanel.setTime(time);
        // gamePanel.setWeather(weather);
        // gamePanel.setGameClock(gameClock);

        window.add(gamePanel);
>>>>>>> try#1
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

<<<<<<< HEAD
        gamePanel.startGameThread();
    }
}
=======
        // gamePanel.setupGame();
        gamePanel.startGameThread();

    }
}
>>>>>>> try#1
