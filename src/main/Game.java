package main;

import javax.swing.*;



public class Game {

    public Game() {
        SwingUtilities.invokeLater(() ->{
            GamePanel gamepanel = new GamePanel();
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setTitle("2D Platformer");
            frame.add(gamepanel);
            gamepanel.addKeyListener(GamePanel.keyHandler);
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
