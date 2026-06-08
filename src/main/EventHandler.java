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

    public void checkEvent0() {
        // Map 0 has one damage event, one healing pool, and one teleport trigger.
        boolean damage = hit(9, 10);
        boolean healing = hit(4, 10);
        boolean teleport = hit(0, 4);

        if (healing) {
            healingPool();
        }

        if (!damage && !teleport) {
            // Reset the one-shot event latch only after the player fully leaves the trigger.
            eventReady = true;
            return;
        }

        if (!eventReady) {
            return;
        }

        if (damage) {
            gp.ui.addMessage("You fell into a pit");
            gp.player.damagePlayer();
            eventReady = false;
            return;
        }

        if (teleport) {
            gp.ui.addMessage("You were teleported to another place");
            teleport(19, 10);
            eventReady = false;
        }
    }

    public void checkEvent1() {
        // Map 1 currently only has a pit damage event.
        boolean damage1 = hit(8, 4);

        if (!damage1) {
            eventReady = true;
            return;
        }

        if (!eventReady) {
            return;
        }

        if (damage1) {
            gp.ui.addMessage("You fell into a pit");
            gp.player.damagePlayer();
            eventReady = false;
            return;
        }
    }

    public void checkEvent2() {
        // Map 2 uses a healing pool near the boss area.
        boolean healing = hit(15, 28);

        if (healing) {
            healingPool();
        }
    }

    public void checkEvent3() {
        boolean teleport = hit(38, 0);
        boolean teleport2 = hit(24, 9);
        boolean teleport3 = hit(45, 12);
        boolean teleport4 = hit(44, 0);
        boolean teleport5 = hit(0, 7);

        if (!teleport && !teleport2 && !teleport3 && !teleport4 && !teleport5) {
            // Reset the one-shot event latch only after the player fully leaves the trigger.
            eventReady = true;
            return;
        }

        if (teleport) {
            gp.ui.addMessage("You were teleported to another place");
            teleport(0, 0);
            eventReady = false;
        }

        if (teleport2) {
            gp.ui.addMessage("You were teleported to another place");
            teleport(23, 3);
            eventReady = false;
        }

        if (teleport3) {
            gp.ui.addMessage("You were teleported to another place");
            teleport(52, 10);
            eventReady = false;
        }

        if (teleport4) {
            gp.ui.addMessage("You were teleported to another place");
            teleport(51, 12);
            eventReady = false;
        }

        if (teleport5) {
            gp.ui.addMessage("You were teleported to another place");
            teleport(54, 7);
            eventReady = false;
        }
    }

    public boolean isPlayerOnHealingPool() {
        // Prevents ENTER from attacking while the player is standing on a healing pool.
        return switch (gp.mapIndicator) {
            case 0 -> hit(4, 10);
            case 2 -> hit(15, 28);
            default -> false;
        };
    }

    public boolean hit(int eventCol, int eventRow) {

        // Convert event tile coordinates into a world-space rectangle and compare with player hitbox.
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

    public void healingPool() {
        // Healing requires ENTER so the player can choose when to spend the action.
        if (gp.keyHandler.enterPressed) {
            gp.ui.addMessage("You healed yourself");
            gp.player.life = gp.player.maxLife;
            gp.keyHandler.enterPressed = false;
        }
    }

    public void teleport(int col, int row) {
        // Teleport destinations are expressed in tile coordinates for easier map editing.
        gp.player.x = col * gp.tileSize;
        gp.player.y = row * gp.tileSize;
    }
}
