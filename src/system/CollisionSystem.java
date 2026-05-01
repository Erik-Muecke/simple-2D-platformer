package system;

import entity.Entity;
import main.GamePanel;

public class CollisionSystem {

    GamePanel gp;

    public CollisionSystem(GamePanel gp) {
        this.gp = gp;
    }

    private int[] getProjectedAABB(Entity entity) {
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
}