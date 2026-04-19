package src;

import java.util.HashSet;

public class CollisionSystem {

    public boolean collides(Entity a, Entity b) {
        return a.x < b.x + b.width &&
               a.x + a.width > b.x &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;
    }

    public Entity getCollidingWall(Entity entity, HashSet<Entity> walls) {
        for (Entity wall : walls) {
            if (collides(entity, wall)) {
                return wall;
            }
        }
        return null;
    }
}
