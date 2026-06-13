package main; // core game package (startup / window creation)

import javax.swing.*;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class Game {
    public Game() {

        // ensures all Swing operations run on the Event Dispatch Thread (important for UI safety)
        SwingUtilities.invokeLater(() -> {

            GamePanel gamepanel = new GamePanel(); // main game canvas + logic loop
            JFrame frame = new JFrame(); // application window

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // closes app on X click
            frame.setResizable(false); // fixed window size (no scaling issues)
            frame.setTitle("2D Platformer"); // window title

            frame.add(gamepanel); // attach game panel to window
            frame.addKeyListener(gamepanel.keyHandler); // input handling for player controls

            gamepanel.gameWindow = frame; // back-reference so game can access window if needed
            gamepanel.addKeyListener(gamepanel.keyHandler); // also attach listener directly to panel
            gamepanel.setFocusable(true); // allows panel to receive keyboard input

            // keeps focus on game when user clicks outside and comes back
            frame.addWindowFocusListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowGainedFocus(java.awt.event.WindowEvent e) {
                    gamepanel.requestFocusInWindow();
                }
            });

            gamepanel.requestFocusInWindow(); // try to immediately capture keyboard focus
            frame.pack(); // sizes window based on preferred GamePanel size

            // frame.setFocusable(true);
            // frame.requestFocus();

            frame.setLocationRelativeTo(null); // centers window on screen
            frame.setVisible(true); // shows window

            gamepanel.requestFocusInWindow(); // final focus fix (ensures input works)

            gamepanel.startGameThread(); // starts game loop (render + update cycle)
        });
    }
}