package game.system;

import game.entity.Entity;

import java.util.HashSet;

/**
 * CollisionSystem handles collision detection for game entities.
 * Uses Axis-Aligned Bounding Box (AABB) collision detection algorithm.
 */
public class CollisionSystem {

    // Checks if two entities are colliding using AABB collision detection
    public boolean collides(Entity a, Entity b) {
        // AABB collision: check if boxes overlap on both axes
        return a.x < b.x + b.width &&
               a.x + a.width > b.x &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;

    }

    // Checks if a given entity collides with any wall in the provided set
    public Entity getCollidingWall(Entity entity, HashSet<Entity> walls) {
        // Iterate through all walls and return the first collision found
        for (Entity wall : walls) {
            if (collides(entity, wall)) {
                return wall;
            }
        }
        return null;
    }
}
