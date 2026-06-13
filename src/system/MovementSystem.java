package system;

import entity.Entity; // base class for player, NPCs, monsters
import entity.Player;
import main.GamePanel; // global game state (world, camera, tilemap, player)
import projectile.Projectile; // projectile objects like fireballs

public class MovementSystem {

    private final GamePanel gp; // access to global game systems
    private final CollisionSystem collisionSystem; // handles all collision checks

    public MovementSystem(GamePanel gp) {
        this.gp = gp; // store GamePanel reference
        this.collisionSystem = gp.collisionsystem; // reuse central collision system
    }

    public void updatePlayer(Entity player) {

        // gravity: increases downward velocity every frame
        player.velocityY += 2; // constant gravity force (simple physics)
        if (player.velocityY > 31) player.velocityY = 31; // terminal velocity cap
        player.onGround = false; // assume airborne until collision proves otherwise

        // horizontal movement first (prevents corner sticking)
        player.collisionOn = false;
        player.x += player.velocityX;
        collisionSystem.collidesT(player); // tile collision
        collisionSystem.collidesWithObject(player); // object collision

        if (player.collisionOn) {
            player.x -= player.velocityX; // undo movement if blocked
            player.velocityX = 0; // stop horizontal motion
        }

        // vertical movement second
        player.collisionOn = false;
        player.y += player.velocityY;
        collisionSystem.collidesT(player);
        collisionSystem.collidesWithObject(player);

        if (player.collisionOn) {
            player.y -= player.velocityY; // undo vertical movement
            if (player.velocityY > 0) player.onGround = true; // landing detection
            player.velocityY = 0; // stop falling/jumping
        }

        // clamp to world floor (prevents falling below map)
        int floorY = gp.worldHeight - player.height;
        if (player.y >= floorY) {
            player.y = floorY;
            player.velocityY = 0;
            player.onGround = true;
        }

        if (player instanceof Player currentPlayer
                && player.onGround
                && !collisionSystem.isTouchingSpike(player)) {
            currentPlayer.setPreviousSafePosition();
        }

        collisionSystem.checkSpikeDamage(player); // hazard damage check
    }

    public void updateProjectile(Projectile projectile) {

        projectile.collisionOn = false;

        // projectile movement depends on direction
        if (projectile.direction == 'L') projectile.x -= projectile.speed;
        else if (projectile.direction == 'R') projectile.x += projectile.speed;
        else if (projectile.direction == 'D') projectile.y += projectile.speed;

        collisionSystem.collidesT(projectile);
        collisionSystem.collidesWithObject(projectile);

        // projectile dies immediately on collision
        if (projectile.collisionOn) {
            projectile.alive = false;
            return;
        }

        projectile.life--; // lifespan countdown
        if (projectile.life <= 0) projectile.alive = false;
    }

    public boolean updateMonsterKnockBack(Entity monster) {

        // knockback = short forced movement away from player after hit
        int knockVelocity = monster.speed * 4; // stronger than normal movement

        if (monster.direction == 'L') monster.velocityX = -knockVelocity;
        else if (monster.direction == 'R') monster.velocityX = knockVelocity;

        monster.x += monster.velocityX;

        monster.collisionOn = false;
        collisionSystem.collidesT(monster);
        collisionSystem.collidesWithObject(monster);

        // revert if hit wall or world boundary
        if (monster.collisionOn ||
                monster.x <= 0 ||
                monster.x + monster.width >= gp.worldWidth) {

            monster.x -= monster.velocityX;
        }

        monster.velocityX = 0; // stop horizontal movement
        monster.knockBackCounter++; // duration timer

        if (monster.knockBackCounter > 10) {
            monster.knockBackCounter = 0;
            monster.knockBack = false;
            monster.direction = monster.directionBeforeKnockBack; // restore direction
            return false; // knockback finished
        }

        return true; // still in knockback state
    }

    public void updateWalkingMonster(Entity monster) {

        // horizontal walking AI
        char horizontalDirection = monster.direction;

        if (horizontalDirection == 'L') monster.velocityX = -monster.speed;
        else monster.velocityX = monster.speed;

        monster.x += monster.velocityX;

        monster.direction = horizontalDirection;

        monster.collisionOn = false;
        collisionSystem.collidesT(monster);
        collisionSystem.collidesWithObject(monster);

        // turn around if blocked
        if (monster.collisionOn ||
                monster.x <= 0 ||
                monster.x + monster.width >= gp.worldWidth) {

            monster.x -= monster.velocityX;

            if (horizontalDirection == 'L') monster.direction = 'R';
            else monster.direction = 'L';

            monster.velocityX = 0;
            monster.freezeFrames = 15; // short pause before moving again
        }

        // gravity (same concept as player physics)
        monster.velocityY += 2;
        if (monster.velocityY > 31) monster.velocityY = 31;
        monster.onGround = false;

        monster.y += monster.velocityY;

        // temporarily force downward direction for collision check
        char savedDirection = monster.direction;
        monster.direction = 'D';

        monster.collisionOn = false;
        collisionSystem.collidesT(monster);
        collisionSystem.collidesWithObject(monster);

        monster.direction = savedDirection;

        if (monster.collisionOn) {
            monster.y -= monster.velocityY;
            monster.velocityY = 0;
            monster.onGround = true;
        } else {
            monster.onGround = false;
        }

        if (collisionSystem.collidesWithPlayer(monster)) {
            gp.player.damagePlayer(); // contact damage
        }
    }

    public void updateFlyingMonster(Entity monster) {

        // same horizontal AI as walking monster (no gravity)
        char horizontalDirection = monster.direction;

        if (horizontalDirection == 'L') monster.velocityX = -monster.speed;
        else monster.velocityX = monster.speed;

        monster.x += monster.velocityX;
        monster.direction = horizontalDirection;

        monster.collisionOn = false;
        collisionSystem.collidesT(monster);
        collisionSystem.collidesWithObject(monster);

        // turn around if blocked
        if (monster.collisionOn ||
                monster.x <= 0 ||
                monster.x + monster.width >= gp.worldWidth) {

            monster.x -= monster.velocityX;

            if (horizontalDirection == 'L') monster.direction = 'R';
            else monster.direction = 'L';

            monster.velocityX = 0;
            monster.freezeFrames = 15;
        }

        if (collisionSystem.collidesWithPlayer(monster)) {
            gp.player.damagePlayer(); // contact damage
        }
    }

    public void startJump(Entity entity, int jumpStrength) {
        // upward movement = negative Y velocity (screen coordinate system)
        entity.velocityY = -jumpStrength;
        entity.onGround = false;
    }
}
