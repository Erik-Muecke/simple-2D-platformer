package main;

import javax.swing.*;

public class Game {
    public Game() {
        //Sicherstellen, dass die GUI im Event Dispatch Thread (EDT) erstellt wird, um Thread-Sicherheitsprobleme zu vermeiden
        SwingUtilities.invokeLater(() ->{
            GamePanel gamepanel = new GamePanel(); //neues GamePanel Objekt wird erstellt, welches die Hauptkomponente des Spiels darstellt

            //Icon für das Fenster und die Taskleiste
            ImageIcon icon = new ImageIcon(
                    Game.class.getResource("/missing/image_not_found.png")
            );//TODO: will be replaced later with final image

            //Erstellen des Hauptfensters
            JFrame frame = new JFrame();

            //Konfiguration des Hauptfensters
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //beendet die Anwendung, wenn das Fenster geschlossen wird
            frame.setResizable(false); //Fenster lässt sich nicht in der Groeße veraendern, um die skalierung der Grafiken konsistent zu halten
            frame.setTitle("2D Platformer"); //Titel des Fensters
            frame.add(gamepanel); //Hinzufuegen des GamePanels zum JFrame, damit es angezeigt wird
            frame.setIconImage(icon.getImage()); //Setzen des Icons für das Fenster und die Taskleiste
            gamepanel.addKeyListener(gamepanel.keyHandler); //Hinzufügen des KeyListeners zum GamePanel, damit es auf Tastatureingaben reagieren kann
            gamepanel.setFocusable(true); //Setzen des GamePanels als fokussierbar, damit es Tastatureingaben empfangen kann
            frame.pack(); //Passt die Größe des Fensters automatisch an die bevorzugte Größe des GamePanels an,
            // damit es genau so groß ist wie das GamePanel und keine zusätzlichen Ränder oder Scrollleisten hat


            frame.setLocationRelativeTo(null); //Positioniert das Fenster in der Mitte des Bildschirms
            frame.setVisible(true); //Macht das Fenster sichtbar, damit es auf dem Bildschirm angezeigt wird
            gamepanel.requestFocusInWindow(); //Fordert den Fokus für das GamePanel an, damit es sofort auf Tastatureingaben reagieren kann, wenn das Fenster geöffnet wird

            gamepanel.startGameThread(); //Startet den Hauptspiel-Thread, der die Spiel-Logik und das Rendering kontinuierlich aktualisiert, damit das Spiel läuft
        });


    }
}
