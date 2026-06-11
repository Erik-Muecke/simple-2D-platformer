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

    public void checkEvent() {
        boolean damage = hit(9, 11);


        if (!eventReady) {
            return;
        }

        if (damage) {
            gp.player.damagePlayer();
            eventReady = false;
            return;
        }
    }

    public boolean hit(int eventCol, int eventRow) {

        int eventWorldX = eventCol * gp.tileSize;
        int eventWorldY = eventRow * gp.tileSize;

        eventRect.x = eventWorldX + eventRectDefaultX;
        eventRect.y = eventWorldY + eventRectDefaultY;

        Rectangle playerWorldRect = new Rectangle(
                gp.player.x + gp.player.solidAreaDefaultX,
                gp.player.y + gp.player.solidAreaDefaultY,
                gp.player.solidArea.width,
                gp.player.solidArea.height + 1
        );

        boolean result = playerWorldRect.intersects(eventRect);

        eventRect.x = eventRectDefaultX;
        eventRect.y = eventRectDefaultY;

        return result;
    }

    public boolean isHealingPoolHit() {
        return hit(4, 10);
    }

    public void healingPool() {
        if (gp.keyHandler.enterPressed) {
            gp.player.life = gp.player.maxLife;
            gp.keyHandler.enterPressed = false;
        }
    }

    public void teleport(int col, int row) {
        gp.player.x = col * gp.tileSize;
        gp.player.y = row * gp.tileSize;
    }
}
