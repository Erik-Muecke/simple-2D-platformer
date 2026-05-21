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

    public int[] getProjectedAABB(Entity entity) {
        int left   = entity.x + entity.solidArea.x;
        int right  = entity.x + entity.solidArea.x + entity.solidArea.width;
        int top    = entity.y + entity.solidArea.y;
        int bottom = entity.y + entity.solidArea.y + entity.solidArea.height;

        switch (entity.direction) {
            case 'U': top    -= entity.speed; break;
            case 'D': bottom += entity.speed; break;
            case 'L': left   -= entity.speed; break;
            case 'R': right  += entity.speed; break;
        }

        return new int[]{ left, right, top, bottom };
    }

    public void drawDebugBoxes(Graphics2D g2, Entity entity) {
        if (entity == null || entity.solidArea == null) return;

        int currentLeft = entity.x + entity.solidArea.x;
        int currentTop = entity.y + entity.solidArea.y;
        int currentWidth = entity.solidArea.width;
        int currentHeight = entity.solidArea.height;

        int[] projected = getProjectedAABB(entity);

        int screenCurrentX = currentLeft - gp.camera.x;
        int screenCurrentY = currentTop - gp.camera.y;

        int screenProjectedX = projected[0] - gp.camera.x;
        int screenProjectedY = projected[2] - gp.camera.y;
        int projectedWidth = projected[1] - projected[0];
        int projectedHeight = projected[3] - projected[2];

        g2.setStroke(new BasicStroke(2));

        // aktuelle Hitbox
        g2.setColor(new Color(0, 255, 0, 180));
        g2.drawRect(screenCurrentX, screenCurrentY, currentWidth, currentHeight);

        // projected AABB
        g2.setColor(new Color(255, 0, 0, 180));
        g2.drawRect(screenProjectedX, screenProjectedY, projectedWidth, projectedHeight);
    }

    private boolean overlaps(int aLeft, int aRight, int aTop, int aBottom,
                             int bLeft, int bRight, int bTop, int bBottom) {
        return aLeft < bRight && aRight > bLeft &&
                aTop  < bBottom && aBottom > bTop;
    }

    public void collidesT(Entity entity) {
        int[] box = getProjectedAABB(entity);
        int left = box[0], right = box[1], top = box[2], bottom = box[3];

        int colLeft   = left   / gp.tileSize;
        int colRight  = right  / gp.tileSize;
        int rowTop    = top    / gp.tileSize;
        int rowBottom = bottom / gp.tileSize;

        for (int col = colLeft; col <= colRight; col++) {
            for (int row = rowTop; row <= rowBottom; row++) {

                if (col < 0 || row < 0 ||
                        col >= gp.tileM.mapTileNum.length ||
                        row >= gp.tileM.mapTileNum[0].length) {
                    entity.collisionOn = true;
                    continue;
                }

                int tileNum = gp.tileM.mapTileNum[col][row];
                if (!gp.tileM.tile[tileNum].collision) continue;

                int tLeft   = col * gp.tileSize;
                int tRight  = tLeft + gp.tileSize;
                int tTop    = row  * gp.tileSize;
                int tBottom = tTop + gp.tileSize;

                if (overlaps(left, right, top, bottom, tLeft, tRight, tTop, tBottom)) {
                    entity.collisionOn = true;
                    return;
                }
            }
        }
    }

    public boolean collidesWithPlayer(Entity entity) {
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

    public void collidesWithEntity(Entity a, Entity b) {
        if (a == b) return;

        int[] boxA = getProjectedAABB(a);
        int bLeft   = b.x + b.solidArea.x;
        int bRight  = bLeft + b.solidArea.width;
        int bTop    = b.y  + b.solidArea.y;
        int bBottom = bTop + b.solidArea.height;

        if (overlaps(boxA[0], boxA[1], boxA[2], boxA[3],
                bLeft,   bRight,  bTop,    bBottom)) {
            a.collisionOn = true;
        }
    }

    public void collidesWithObject(Entity entity) {
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
        int index = 999;

        int entityLeft   = entity.x + entity.solidArea.x;
        int entityRight  = entityLeft + entity.solidArea.width;
        int entityTop    = entity.y + entity.solidArea.y;
        int entityBottom = entityTop + entity.solidArea.height;

        // Project one step ahead so doors are detected before movement is blocked, needed
        switch (entity.direction) {
            case 'U': entityTop    -= entity.speed; break;
            case 'D': entityBottom += entity.speed; break;
            case 'L': entityLeft   -= entity.speed; break;
            case 'R': entityRight  += entity.speed; break;
        }
        for (int i = 0; i < gp.obj.length; i++) {
            SuperObject object = gp.obj[i];
            if (object == null) continue;

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
                    index = i;
                }
            }
        }
        return index;
    }

}
