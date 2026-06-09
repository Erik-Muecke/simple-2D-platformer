package main;

import javax.swing.*;

public class Game {
    public Game() {
        SwingUtilities.invokeLater(() ->{
            GamePanel gamepanel = new GamePanel();
            ImageIcon icon = new ImageIcon(
                    Game.class.getResource("/missing/image_not_found.png")
            );//TODO: will be replaced later with final image

            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setTitle("2D Platformer");
            frame.add(gamepanel);
            frame.setIconImage(icon.getImage());
            gamepanel.addKeyListener(gamepanel.keyHandler);
            gamepanel.setFocusable(true);
            gamepanel.requestFocusInWindow();
            frame.pack();

            //frame.setFocusable(true);
            //frame.requestFocus();


            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            gamepanel.requestFocusInWindow();

            gamepanel.startGameThread();
        });


    }
}
