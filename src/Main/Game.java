package Main;

import javax.swing.*;

public class Game {
    public static KeyHandler keyHandler = new KeyHandler(); //erstellen eines statischen KeyHandler Objektes, sonst können die variablen daruas nicht im statischen kontext verwendet werden.

    public Game() {
        SwingUtilities.invokeLater(() ->{
            GamePanel.GamePanel gamepanel = new GamePanel.GamePanel();
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setTitle("2D Platformer");
            frame.add(gamepanel);
            frame.addKeyListener(keyHandler);
            frame.pack();

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            gamepanel.startGameThread();
        });


    }
}
