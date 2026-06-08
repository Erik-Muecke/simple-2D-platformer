package main;

import javax.swing.*;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class Game {
    public Game() {
        SwingUtilities.invokeLater(() ->{
            GamePanel gamepanel = new GamePanel();
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setTitle("2D Platformer");
            frame.add(gamepanel);
            frame.addKeyListener(gamepanel.keyHandler);
            gamepanel.gameWindow = frame;
            gamepanel.addKeyListener(gamepanel.keyHandler);
            gamepanel.setFocusable(true);

            frame.addWindowFocusListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowGainedFocus(java.awt.event.WindowEvent e) {
                    gamepanel.requestFocusInWindow();
                }
            });

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
