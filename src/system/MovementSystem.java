package system;

import entity.Entity;
import main.GamePanel;
import projectile.Projectile;

public class MovementSystem {
    private final GamePanel gp;
    private final CollisionSystem collisionSystem;

    public MovementSystem(GamePanel gp) {
        this.gp = gp;
        this.collisionSystem = gp.collisionsystem;
    }

    public void updatePlayer(Entity player) {
        // Gravity is applied before collision so the player naturally falls until a floor is hit.
        player.velocityY += 2;
        if (player.velocityY > 31) {
            player.velocityY = 31;
        }
        player.onGround = false;

        // Resolve horizontal and vertical movement separately to prevent corner-sticking.
        player.collisionOn = false;
        player.x += player.velocityX;
        collisionSystem.collidesT(player);
        collisionSystem.collidesWithObject(player);

        if (player.collisionOn) {
            player.x -= player.velocityX;
            player.velocityX = 0;
        }

        player.collisionOn = false;
        player.y += player.velocityY;
        collisionSystem.collidesT(player);
        collisionSystem.collidesWithObject(player);

        if (player.collisionOn) {
            player.y -= player.velocityY;
            if (player.velocityY > 0) {
                player.onGround = true;
            }
            player.velocityY = 0;
        }

        int floorY = gp.worldHeight - player.height;
        if (player.y >= floorY) {
            player.y = floorY;
            player.velocityY = 0;
        }

        collisionSystem.checkSpikeDamage(player);
    }

    public void updateProjectile(Projectile projectile) {
        projectile.collisionOn = false;

        // Projectiles are currently horizontal only; direction controls which way they travel.
        if (projectile.direction == 'L') {
            projectile.x -= projectile.speed;
        } else if (projectile.direction == 'R') {
            projectile.x += projectile.speed;
        } else if (projectile.direction == 'D') {
            projectile.y += projectile.speed;
        }

        collisionSystem.collidesT(projectile);
        collisionSystem.collidesWithObject(projectile);

        // A projectile disappears as soon as it hits a solid tile/object.
        if (projectile.collisionOn) {
            projectile.alive = false;
            return;
        }

        projectile.life--;
        if (projectile.life <= 0) {
            projectile.alive = false;
        }
    }

    public boolean updateMonsterKnockBack(Entity monster) {
        // Knockback is short-lived movement away from the player after taking melee damage.
        int knockVelocity = monster.speed * 4;

        if (monster.direction == 'L') {
            monster.velocityX = -knockVelocity;
        } else if (monster.direction == 'R') {
            monster.velocityX = knockVelocity;
        }

        monster.x += monster.velocityX;
        monster.collisionOn = false;
        collisionSystem.collidesT(monster);
        collisionSystem.collidesWithObject(monster);

        if (monster.collisionOn || monster.x <= 0 || monster.x + monster.width >= gp.worldWidth) {
            monster.x -= monster.velocityX;
        }

        monster.velocityX = 0;
        monster.knockBackCounter++;

        if (monster.knockBackCounter > 10) {
            monster.knockBackCounter = 0;
            monster.knockBack = false;
            monster.direction = monster.directionBeforeKnockBack;
            return false;
        }

        return true;
    }

    public void updateWalkingMonster(Entity monster) {
        // Walking monsters turn around when their next horizontal step would hit a wall.
        char horizontalDirection = monster.direction;
        if (horizontalDirection == 'L') {
            monster.velocityX = -monster.speed;
        } else {
            monster.velocityX = monster.speed;
        }
        monster.x += monster.velocityX;
        monster.direction = horizontalDirection;
        monster.collisionOn = false;
        collisionSystem.collidesT(monster);
        collisionSystem.collidesWithObject(monster);

        if (monster.collisionOn || monster.x <= 0 || monster.x + monster.width >= gp.worldWidth) {
            monster.x -= monster.velocityX;
            if (horizontalDirection == 'L') {
                monster.direction = 'R';
            } else {
                monster.direction = 'L';
            }
            monster.velocityX = 0;
            monster.freezeFrames = 15;
        }

        // Monsters use the same gravity style as the player but do not react to jump input.
        monster.velocityY += 2;
        if (monster.velocityY > 31) {
            monster.velocityY = 31;
        }
        monster.onGround = false;

        monster.y += monster.velocityY;
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
            gp.player.damagePlayer();
        }
    }

    public void updateFlyingMonster(Entity monster) {
        // Flying monsters turn around when their next horizontal step would hit a wall.
        char horizontalDirection = monster.direction;
        if (horizontalDirection == 'L') {
            monster.velocityX = -monster.speed;
        } else {
            monster.velocityX = monster.speed;
        }
        monster.x += monster.velocityX;
        monster.direction = horizontalDirection;
        monster.collisionOn = false;
        collisionSystem.collidesT(monster);
        collisionSystem.collidesWithObject(monster);

        if (monster.collisionOn || monster.x <= 0 || monster.x + monster.width >= gp.worldWidth) {
            monster.x -= monster.velocityX;
            if (horizontalDirection == 'L') {
                monster.direction = 'R';
            } else {
                monster.direction = 'L';
            }
            monster.velocityX = 0;
            monster.freezeFrames = 15;
        }

        if (collisionSystem.collidesWithPlayer(monster)) {
            gp.player.damagePlayer();
        }
    }

    public void startJump(Entity entity, int jumpStrength) {
        // Negative Y velocity moves an entity upward in screen coordinates.
        entity.velocityY = -jumpStrength;
        entity.onGround = false;
    }
}
