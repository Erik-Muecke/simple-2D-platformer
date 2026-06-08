package system;

import entity.Entity;
import main.GamePanel;
import object.SuperObject;

import java.awt.*;

public class CollisionSystem {
    GamePanel gp;

    public CollisionSystem(GamePanel gp) {
        this.gp = gp;
    }

    public int[] getCurrentAABB(Entity entity) {
        // Convert an entity's local solidArea into world-space collision bounds.
        int left   = entity.x + entity.solidArea.x;
        int right  = left + entity.solidArea.width;
        int top    = entity.y + entity.solidArea.y;
        int bottom = top + entity.solidArea.height;

        return new int[]{ left, right, top, bottom };
    }

    public void drawDebugBoxes(Graphics2D g2, Entity entity) {
        if (entity == null || entity.solidArea == null) return;

        int[] current = getCurrentAABB(entity);
        int screenCurrentX = current[0] - gp.camera.x;
        int screenCurrentY = current[2] - gp.camera.y;
        int currentWidth = current[1] - current[0];
        int currentHeight = current[3] - current[2];

        g2.setStroke(new BasicStroke(2));
        g2.setColor(new Color(0, 255, 0, 180));
        g2.drawRect(screenCurrentX, screenCurrentY, currentWidth, currentHeight);
    }

    private boolean overlaps(int aLeft, int aRight, int aTop, int aBottom,
                             int bLeft, int bRight, int bTop, int bBottom) {
        return aLeft < bRight && aRight > bLeft &&
                aTop  < bBottom && aBottom > bTop;
    }

    public void collidesT(Entity entity) {
        int[] box = getCurrentAABB(entity);
        int left = box[0], right = box[1], top = box[2], bottom = box[3];
        boolean solidCollision = false;

        // Check only the tiles overlapped by the entity hitbox instead of scanning the whole map.
        int colLeft   = Math.floorDiv(left, gp.tileSize);
        int colRight  = Math.floorDiv(right - 1, gp.tileSize);
        int rowTop    = Math.floorDiv(top, gp.tileSize);
        int rowBottom = Math.floorDiv(bottom - 1, gp.tileSize);

        for (int col = colLeft; col <= colRight; col++) {
            for (int row = rowTop; row <= rowBottom; row++) {

                if (col < 0 || row < 0 ||
                        col >= gp.tileM.mapTileNum.length ||
                        row >= gp.tileM.mapTileNum[0].length) {
                    // Treat outside-world movement as blocked.
                    solidCollision = true;
                    continue;
                }

                int tileNum = gp.tileM.mapTileNum[col][row];
                if (!isValidCollisionTile(tileNum)) {
                    // Invalid map data should block movement instead of crashing the game loop.
                    solidCollision = true;
                    continue;
                }

                if (gp.tileM.tile[tileNum].name.equals("spike")) {
                    // Spikes are hazards, not solid walls.
                    // Damage is checked after movement is resolved, not during collision probing.
                    continue;
                }

                if (gp.tileM.tile[tileNum].name.equals("coin")) {
                    if (entity != gp.player) { // Gegner ignorieren Coins
                        continue;
                    }
                    switch (gp.mapIndicator) {
                        case 0:
                            gp.player.hasCoin++;
                            gp.tileM.mapTileNum[col][row] = 0;

                            System.out.println("You got a coin! Total: " + gp.player.hasCoin);
                            gp.ui.addMessage("You got a coin! Total: " + gp.player.hasCoin);
                            break;
                        case 1:
                            gp.player.hasCoin++;
                            gp.tileM.mapTileNum[col][row] = 0;
                            System.out.println("You got a coin! Total: " + gp.player.hasCoin);
                            gp.ui.addMessage("You got a coin! Total: " + gp.player.hasCoin);
                            break;
                        case 2:
                            gp.player.hasCoin++;
                            // Map 2 uses dungeon floor under collected coins.
                            gp.tileM.mapTileNum[col][row] = 10;
                            System.out.println("You got a coin! Total: " + gp.player.hasCoin);
                            gp.ui.addMessage("You got a coin! Total: " + gp.player.hasCoin);
                            break;
                        case 3:
                            gp.player.hasCoin++;
                            gp.tileM.mapTileNum[col][row] = 0;
                            System.out.println("You got a coin! Total: " + gp.player.hasCoin);
                            gp.ui.addMessage("You got a coin! Total: " + gp.player.hasCoin);
                            break;
                        case 4:
                            gp.player.hasCoin++;
                            gp.tileM.mapTileNum[col][row] = 0;
                            System.out.println("You got a coin! Total: " + gp.player.hasCoin);
                            gp.ui.addMessage("You got a coin! Total: " + gp.player.hasCoin);
                            break;
                    }
                    continue;
                }
                if (!gp.tileM.tile[tileNum].collision) continue;

                int tLeft   = col * gp.tileSize;
                int tRight  = tLeft + gp.tileSize;
                int tTop    = row  * gp.tileSize;
                int tBottom = tTop + gp.tileSize;

                if (overlaps(left, right, top, bottom, tLeft, tRight, tTop, tBottom)) {
                    solidCollision = true;
                }
            }
        }

        if (solidCollision) {
            entity.collisionOn = true;
        }
    }

    public void checkSpikeDamage(Entity entity) {
        int[] box = getCurrentAABB(entity);
        int left = box[0], right = box[1], top = box[2], bottom = box[3];

        int colLeft   = Math.floorDiv(left, gp.tileSize);
        int colRight  = Math.floorDiv(right - 1, gp.tileSize);
        int rowTop    = Math.floorDiv(top, gp.tileSize);
        int rowBottom = Math.floorDiv(bottom - 1, gp.tileSize);

        for (int col = colLeft; col <= colRight; col++) {
            for (int row = rowTop; row <= rowBottom; row++) {
                if (col < 0 || row < 0 ||
                        col >= gp.tileM.mapTileNum.length ||
                        row >= gp.tileM.mapTileNum[0].length) {
                    continue;
                }

                int tileNum = gp.tileM.mapTileNum[col][row];
                if (!isValidCollisionTile(tileNum) || !gp.tileM.tile[tileNum].name.equals("spike")) {
                    continue;
                }

                int spikeBorder = gp.tileSize / 3;
                int tLeft   = col * gp.tileSize + spikeBorder;
                int tRight  = (col + 1) * gp.tileSize - spikeBorder;
                int tTop    = row * gp.tileSize + 5;
                int tBottom = tTop + gp.tileSize;

                if (overlaps(left, right, top, bottom, tLeft, tRight, tTop, tBottom)) {
                    damageSpikeTarget(entity);
                    return;
                }
            }
        }
    }

    private void damageSpikeTarget(Entity entity) {
        if (entity == gp.player) {
            gp.player.damagePlayer();
            return;
        }

        // Monster invincibility prevents one spike contact from draining all life in one frame.
        if (entity.type == Entity.TYPE_MONSTER && !entity.invincible) {
            entity.life--;
            entity.invincible = true;

            if (entity.life <= 0) {
                entity.isDead = true;
            }
        }
    }

    private boolean isValidCollisionTile(int tileNum) {
        return tileNum >= 0
                && tileNum < gp.tileM.tile.length
                && gp.tileM.tile[tileNum] != null
                && gp.tileM.tile[tileNum].name != null;
    }

    public boolean collidesWithPlayer(Entity entity) {
        // Used by monsters to detect contact damage against the player.
        int entityLeft = entity.x + entity.solidArea.x;
        int entityRight = entityLeft + entity.solidArea.width;
        int entityTop = entity.y + entity.solidArea.y;
        int entityBottom = entityTop + entity.solidArea.height;

        int playerLeft = gp.player.x + gp.player.solidArea.x;
        int playerRight = playerLeft + gp.player.solidArea.width;
        int playerTop = gp.player.y + gp.player.solidArea.y;
        int playerBottom = playerTop + gp.player.solidArea.height;

        boolean contactPlayer = overlaps(
                entityLeft, entityRight, entityTop, entityBottom,
                playerLeft, playerRight, playerTop, playerBottom
        );

        if (contactPlayer) {
            entity.collisionOn = true;
        }

        return contactPlayer;
    }

    public void collidesWithObject(Entity entity) {
        // Solid objects such as doors block movement using the same AABB overlap test as tiles.
        int entityLeft = entity.x + entity.solidArea.x;
        int entityRight = entityLeft + entity.solidArea.width;
        int entityTop = entity.y + entity.solidArea.y;
        int entityBottom = entityTop + entity.solidArea.height;

        for (SuperObject object : gp.obj) {
            if (object == null || !object.collision) {
                continue;
            }

            int objectLeft = object.x + object.solidArea.x;
            int objectRight = objectLeft + object.solidArea.width;
            int objectTop = object.y + object.solidArea.y;
            int objectBottom = objectTop + object.solidArea.height;

            if (overlaps(entityLeft, entityRight, entityTop, entityBottom,
                    objectLeft, objectRight, objectTop, objectBottom)) {
                entity.collisionOn = true;
                return;
            }
        }
    }



    public int collisionObject(Entity entity, boolean player) {
        int entityLeft   = entity.x + entity.solidArea.x;
        int entityRight  = entityLeft + entity.solidArea.width;
        int entityTop    = entity.y + entity.solidArea.y;
        int entityBottom = entityTop + entity.solidArea.height;

        // Project one step ahead so doors/items are detected before movement is blocked.
        switch (entity.direction) {
            case 'U': entityTop    -= entity.speed; break;
            case 'D': entityBottom += entity.speed; break;
            case 'L': entityLeft   -= entity.speed; break;
            case 'R': entityRight  += entity.speed; break;
        }
        int index = findObjectIndex(entity, player, entityLeft, entityRight, entityTop, entityBottom, false);
        if (index != 999 || !player) {
            return index;
        }

        // If the player stands directly on a door while falling, still allow door interaction.
        int[] box = getCurrentAABB(entity);
        return findObjectIndex(entity, true, box[0], box[1], box[2], box[3] + entity.speed, true);
    }

    private int findObjectIndex(Entity entity, boolean player, int entityLeft, int entityRight,
                                int entityTop, int entityBottom, boolean doorsOnly) {
        for (int i = 0; i < gp.obj.length; i++) {
            SuperObject object = gp.obj[i];
            if (object == null) continue;
            if (doorsOnly && !isDoor(object)) continue;

            int objectLeft   = object.x + object.solidArea.x;
            int objectRight  = objectLeft + object.solidArea.width;
            int objectTop    = object.y + object.solidArea.y;
            int objectBottom = objectTop + object.solidArea.height;

            if (overlaps(entityLeft, entityRight, entityTop, entityBottom,
                    objectLeft, objectRight, objectTop, objectBottom)) {
                if (object.collision) {
                    entity.collisionOn = true;
                }
                if (player) {
                    return i;
                }
            }
        }
        return 999;
    }

    private boolean isDoor(SuperObject object) {
        return object.name.equals("Door") || object.name.equals("Special Door");
    }
}
