package system;

import entity.Entity; // base class for all moving objects (player, NPCs, monsters)
import main.GamePanel; // central game container (map, player, systems, camera)
import object.SuperObject; // interactable world objects (doors, coins, items)

import java.awt.*; // Graphics2D, Rectangle, Color, Stroke

public class CollisionSystem {

    GamePanel gp; // access to game state (tiles, player, camera, objects)

    public CollisionSystem(GamePanel gp) {
        this.gp = gp; // store GamePanel reference
    }

    public int[] getCurrentAABB(Entity entity) {
        // converts local hitbox (solidArea) into world-space rectangle
        int left = entity.x + entity.solidArea.x; // left edge in world coords
        int right = left + entity.solidArea.width; // right edge
        int top = entity.y + entity.solidArea.y; // top edge
        int bottom = top + entity.solidArea.height; // bottom edge

        return new int[]{left, right, top, bottom}; // AABB = axis aligned bounding box
    }

    public void drawDebugBoxes(Graphics2D g2, Entity entity) {
        if (entity == null || entity.solidArea == null) return; // safety check

        int[] current = getCurrentAABB(entity); // get world-space hitbox

        int screenCurrentX = current[0] - gp.camera.x; // convert world → screen X
        int screenCurrentY = current[2] - gp.camera.y; // convert world → screen Y

        int currentWidth = current[1] - current[0]; // width = right - left
        int currentHeight = current[3] - current[2]; // height = bottom - top

        g2.setStroke(new BasicStroke(2)); // line thickness for debug box
        g2.setColor(new Color(0, 255, 0, 180)); // semi-transparent green
        g2.drawRect(screenCurrentX, screenCurrentY, currentWidth, currentHeight); // draw hitbox
    }

    private boolean overlaps(int aLeft, int aRight, int aTop, int aBottom,
                             int bLeft, int bRight, int bTop, int bBottom) {

        // AABB collision check:
        // overlap exists if boxes are not separated on any axis
        return aLeft < bRight && aRight > bLeft &&
                aTop < bBottom && aBottom > bTop;
    }

    public void collidesT(Entity entity) {

        int[] box = getCurrentAABB(entity); // entity hitbox in world coords
        int left = box[0], right = box[1], top = box[2], bottom = box[3];

        boolean solidCollision = false; // flag for blocking movement

        // convert world position into tile grid indices
        int colLeft = Math.floorDiv(left, gp.tileSize);
        int colRight = Math.floorDiv(right - 1, gp.tileSize);
        int rowTop = Math.floorDiv(top, gp.tileSize);
        int rowBottom = Math.floorDiv(bottom - 1, gp.tileSize);

        for (int col = colLeft; col <= colRight; col++) {
            for (int row = rowTop; row <= rowBottom; row++) {

                if (col < 0 || row < 0 ||
                        col >= gp.tileM.mapTileNum.length ||
                        row >= gp.tileM.mapTileNum[0].length) {

                    solidCollision = true; // outside map = blocked
                    continue;
                }

                int tileNum = gp.tileM.mapTileNum[col][row]; // tile ID at position

                if (!isValidCollisionTile(tileNum)) {
                    solidCollision = true; // invalid tile data = block movement
                    continue;
                }

                if (gp.tileM.tile[tileNum].name.equals("spike")) {
                    continue; // spikes handled separately (damage system)
                }

                if (gp.tileM.tile[tileNum].name.equals("coin")) {

                    if (entity != gp.player) continue; // only player collects coins

                    // coin logic depends on map
                    switch (gp.mapIndicator) {
                        case 0, 1, 3, 4:
                            gp.player.hasCoin++;
                            gp.tileM.mapTileNum[col][row] = 0;
                            break;

                        case 2:
                            gp.player.hasCoin++;
                            gp.tileM.mapTileNum[col][row] = 10; // special floor tile
                            break;
                    }

                    System.out.println("You got a coin! Total: " + gp.player.hasCoin);
                    gp.ui.addMessage("You got a coin! Total: " + gp.player.hasCoin);

                    continue;
                }

                if (!gp.tileM.tile[tileNum].collision) continue; // non-solid tile

                int tLeft = col * gp.tileSize; // tile left edge
                int tRight = tLeft + gp.tileSize; // tile right edge
                int tTop = row * gp.tileSize; // tile top edge
                int tBottom = tTop + gp.tileSize; // tile bottom edge

                if (overlaps(left, right, top, bottom, tLeft, tRight, tTop, tBottom)) {
                    solidCollision = true; // entity hits solid tile
                }
            }
        }

        if (solidCollision) entity.collisionOn = true; // block movement
    }

    public int collidesWithNPC(Entity entity) {

        int entityLeft = entity.x + entity.solidArea.x;
        int entityRight = entityLeft + entity.solidArea.width;
        int entityTop = entity.y + entity.solidArea.y;
        int entityBottom = entityTop + entity.solidArea.height;

        // predict next movement step (so collision happens before movement)
        switch (entity.direction) {
            case 'L': entityLeft -= entity.speed; break;
            case 'R': entityRight += entity.speed; break;
            case 'U': entityTop -= entity.speed; break;
            case 'D': entityBottom += entity.speed; break;
        }

        for (int i = 0; i < gp.npc.length; i++) {
            Entity npc = gp.npc[i];
            if (npc == null) continue;

            int npcLeft = npc.x + npc.solidArea.x;
            int npcRight = npcLeft + npc.solidArea.width;
            int npcTop = npc.y + npc.solidArea.y;
            int npcBottom = npcTop + npc.solidArea.height;

            if (overlaps(entityLeft, entityRight, entityTop, entityBottom,
                    npcLeft, npcRight, npcTop, npcBottom)) {

                entity.collisionOn = true; // block movement into NPC
                return i; // return NPC index
            }
        }
        return 999; // no NPC collision
    }

    public void checkSpikeDamage(Entity entity) {

        int[] box = getCurrentAABB(entity); // entity hitbox
        int left = box[0], right = box[1], top = box[2], bottom = box[3];

        int colLeft = Math.floorDiv(left, gp.tileSize);
        int colRight = Math.floorDiv(right - 1, gp.tileSize);
        int rowTop = Math.floorDiv(top, gp.tileSize);
        int rowBottom = Math.floorDiv(bottom - 1, gp.tileSize);

        for (int col = colLeft; col <= colRight; col++) {
            for (int row = rowTop; row <= rowBottom; row++) {

                if (col < 0 || row < 0 ||
                        col >= gp.tileM.mapTileNum.length ||
                        row >= gp.tileM.mapTileNum[0].length) continue;

                int tileNum = gp.tileM.mapTileNum[col][row];

                if (!isValidCollisionTile(tileNum) ||
                        !gp.tileM.tile[tileNum].name.equals("spike")) continue;

                int spikeBorder = gp.tileSize / 3; // reduces hitbox of spikes (fairness)

                int tLeft = col * gp.tileSize + spikeBorder;
                int tRight = (col + 1) * gp.tileSize - spikeBorder;
                int tTop = row * gp.tileSize + 5;
                int tBottom = tTop + gp.tileSize;

                if (overlaps(left, right, top, bottom, tLeft, tRight, tTop, tBottom)) {
                    damageSpikeTarget(entity); // apply damage
                    return;
                }
            }
        }
    }

    private void damageSpikeTarget(Entity entity) {

        if (entity == gp.player) {
            gp.player.damagePlayer(); // player damage system
            return;
        }

        // monsters take damage with invincibility frames
        if (entity.type == Entity.TYPE_MONSTER && !entity.invincible) {
            entity.life--; // reduce HP
            entity.invincible = true; // prevent rapid multi-hit

            if (entity.life <= 0) {
                entity.isDead = true; // remove monster
            }
        }
    }

    private boolean isValidCollisionTile(int tileNum) {
        return tileNum >= 0 &&
                tileNum < gp.tileM.tile.length &&
                gp.tileM.tile[tileNum] != null &&
                gp.tileM.tile[tileNum].name != null; // prevents null crashes
    }

    public boolean collidesWithPlayer(Entity entity) {

        // check entity vs player AABB collision
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

        if (contactPlayer) entity.collisionOn = true; // stop movement

        return contactPlayer; // used for damage logic
    }

    public void collidesWithObject(Entity entity) {

        int entityLeft = entity.x + entity.solidArea.x;
        int entityRight = entityLeft + entity.solidArea.width;
        int entityTop = entity.y + entity.solidArea.y;
        int entityBottom = entityTop + entity.solidArea.height;

        for (SuperObject object : gp.obj) {

            if (object == null || !object.collision) continue;

            int objectLeft = object.x + object.solidArea.x;
            int objectRight = objectLeft + object.solidArea.width;
            int objectTop = object.y + object.solidArea.y;
            int objectBottom = objectTop + object.solidArea.height;

            if (overlaps(entityLeft, entityRight, entityTop, entityBottom,
                    objectLeft, objectRight, objectTop, objectBottom)) {

                entity.collisionOn = true; // block movement
                return;
            }
        }
    }

    public int collisionObject(Entity entity, boolean player) {

        int entityLeft = entity.x + entity.solidArea.x;
        int entityRight = entityLeft + entity.solidArea.width;
        int entityTop = entity.y + entity.solidArea.y;
        int entityBottom = entityTop + entity.solidArea.height;

        // predict movement step for early detection
        switch (entity.direction) {
            case 'U': entityTop -= entity.speed; break;
            case 'D': entityBottom += entity.speed; break;
            case 'L': entityLeft -= entity.speed; break;
            case 'R': entityRight += entity.speed; break;
        }

        int index = findObjectIndex(entity, player,
                entityLeft, entityRight, entityTop, entityBottom, false);

        if (index != 999 || !player) return index;

        // fallback check (useful for falling interaction like doors)
        int[] box = getCurrentAABB(entity);
        return findObjectIndex(entity, true,
                box[0], box[1], box[2], box[3] + entity.speed, true);
    }

    private int findObjectIndex(Entity entity, boolean player,
                                int entityLeft, int entityRight,
                                int entityTop, int entityBottom,
                                boolean doorsOnly) {

        for (int i = 0; i < gp.obj.length; i++) {

            SuperObject object = gp.obj[i];
            if (object == null) continue;
            if (doorsOnly && !isDoor(object)) continue;

            int objectLeft = object.x + object.solidArea.x;
            int objectRight = objectLeft + object.solidArea.width;
            int objectTop = object.y + object.solidArea.y;
            int objectBottom = objectTop + object.solidArea.height;

            if (overlaps(entityLeft, entityRight, entityTop, entityBottom,
                    objectLeft, objectRight, objectTop, objectBottom)) {

                if (object.collision) entity.collisionOn = true;

                if (player) return i; // return object index for pickup/interaction
            }
        }
        return 999; // no collision
    }

    private boolean isDoor(SuperObject object) {
        return object.name.equals("Door") || object.name.equals("Special Door"); // door filter
    }
}