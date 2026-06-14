package main;

import java.awt.Rectangle;

public class EventHandler {

    GamePanel gp;
    Rectangle eventRect;
    int eventRectDefaultX, eventRectDefaultY;
    boolean eventReady = true;

    public EventHandler(GamePanel gp) {
        this.gp = gp;

        eventRect = new Rectangle();
        eventRect.x = 0;
        eventRect.y = 0;
        eventRect.width = gp.tileSize;
        eventRect.height = gp.tileSize;

        eventRectDefaultX = eventRect.x;
        eventRectDefaultY = eventRect.y;
    }

    //aktuell keine Events, daher Leer
    public void checkEvent() {

    }

    public boolean hit(int eventCol, int eventRow) {
        // Berechne die Weltkoordinaten des Event-Rechtecks
        int eventWorldX = eventCol * gp.tileSize;
        int eventWorldY = eventRow * gp.tileSize;

        //Erstelt das Event-Rechteck basierend auf den Weltkoordinaten
        eventRect.x = eventWorldX + eventRectDefaultX;
        eventRect.y = eventWorldY + eventRectDefaultY;

        // Berechne die Weltkoordinaten des Spieler-Rechtecks
        Rectangle playerWorldRect = new Rectangle(
                gp.player.x + gp.player.solidAreaDefaultX,
                gp.player.y + gp.player.solidAreaDefaultY,
                gp.player.solidArea.width,
                gp.player.solidArea.height + 1
        );

        //Überprüft ob sich das Spieler-Rechteck mit dem Event-Rechteck überschneidet, wenn ja ist es eine Kollision
        boolean result = playerWorldRect.intersects(eventRect);

        // Setzt die Event-Rechteck-Koordinaten zurück, damit sie für zukünftige Kollisionen korrekt sind
        eventRect.x = eventRectDefaultX;
        eventRect.y = eventRectDefaultY;

        // Gibt zurueck, ob eine Kollision stattgefunden hat
        return result;
    }

    //ueberprueft, ob der Spieler mit dem Healing Pool Event-Rechteck kollidiert,
    public boolean isHealingPoolHit() {
        return hit(4, 10);
    }

    //Logik für das Healing Pool event
    public void healingPool() {
        if (gp.keyHandler.enterPressed) {
            gp.player.life = gp.player.maxLife;
            gp.keyHandler.enterPressed = false;
        }
    }

    // Teleportiert den Spieler zu den angegebenen Spalten- und Zeilenkoordinaten, indem die Weltkoordinaten des Spielers entsprechend aktualisiert werden.
    public void teleport(int col, int row) {
        gp.player.x = col * gp.tileSize;
        gp.player.y = row * gp.tileSize;
    }
}
