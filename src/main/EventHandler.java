package main;

import java.awt.Rectangle;

public class EventHandler {

    GamePanel gp; // reference to main game state
    Rectangle eventRect; // reusable hitbox for event tiles
    int eventRectDefaultX, eventRectDefaultY; // original offset of event box inside tile
    boolean eventReady = true; // prevents repeated triggering of same event every frame

    public EventHandler(GamePanel gp) {
        this.gp = gp;

        eventRect = new Rectangle();
        eventRect.x = 0;
        eventRect.y = 0;
        eventRect.width = gp.tileSize; // event area is one tile by default
        eventRect.height = gp.tileSize;

        eventRectDefaultX = eventRect.x;
        eventRectDefaultY = eventRect.y;
    }

    public void checkEvent0() {
        // Map 0: pit damage, healing pool, teleport zone
        boolean damage = hit(9, 10);
        boolean healing = hit(4, 10);
        boolean teleport = hit(0, 4);

        if (healing) {
            healingPool(); // optional interaction (press ENTER to heal)
        }

        if (!damage && !teleport) {
            // reset trigger lock if player leaves event area completely
            eventReady = true;
            return;
        }

        if (!eventReady) {
            return; // prevents spam triggering while standing inside tile
        }

        if (damage) {
            gp.ui.addMessage("You fell into a pit");
            gp.player.damagePlayer();
            eventReady = false;
            return;
        }

        if (teleport) {
            gp.ui.addMessage("You were teleported to another place");
            teleport(19, 10); // move player to target tile
            eventReady = false;
        }
    }

    public void checkEvent1() {
        // Map 1: only a single pit damage event
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
        // Map 2: healing pool near boss area
        boolean healing = hit(15, 28);

        if (healing) {
            healingPool();
        }
    }

    public void checkEvent3() {
        // Map 3: multiple teleport points (warp system)
        boolean teleport = hit(38, 0);
        boolean teleport2 = hit(24, 9);
        boolean teleport3 = hit(45, 12);
        boolean teleport4 = hit(44, 0);
        boolean teleport5 = hit(0, 7);

        if (!teleport && !teleport2 && !teleport3 && !teleport4 && !teleport5) {
            // reset only when player fully leaves all triggers
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
        // used to disable attack / allow healing interaction
        return switch (gp.mapIndicator) {
            case 0 -> hit(4, 10);
            case 2 -> hit(15, 28);
            default -> false;
        };
    }

    public boolean hit(int eventCol, int eventRow) {

        // converts tile coordinates into world position and checks collision with player
        int eventWorldX = eventCol * gp.tileSize;
        int eventWorldY = eventRow * gp.tileSize;

        eventRect.x = eventWorldX + eventRectDefaultX;
        eventRect.y = eventWorldY + eventRectDefaultY;

        // player hitbox in world coordinates
        Rectangle playerWorldRect = new Rectangle(
                gp.player.x + gp.player.solidAreaDefaultX,
                gp.player.y + gp.player.solidAreaDefaultY,
                gp.player.solidArea.width,
                gp.player.solidArea.height + 1
        );

        boolean result = playerWorldRect.intersects(eventRect);

        // reset event rectangle back to default position
        eventRect.x = eventRectDefaultX;
        eventRect.y = eventRectDefaultY;

        return result;
    }

    public void healingPool() {
        // healing only happens on key press (player chooses timing)
        if (gp.keyHandler.enterPressed) {
            gp.ui.addMessage("You healed yourself");
            gp.player.life = gp.player.maxLife;
            gp.keyHandler.enterPressed = false; // consume input so it doesn't repeat
        }
    }

    public void teleport(int col, int row) {
        // moves player to a new tile position (tile-based world system)
        gp.player.x = col * gp.tileSize;
        gp.player.y = row * gp.tileSize;
    }
}