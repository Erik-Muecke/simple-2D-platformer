package monster;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Coin;
import object.OBJ_HealingPotion;
import object.OBJ_Heart;
import object.OBJ_ManaPotion;
import projectile.PT_AimedFireball;
import projectile.Projectile;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

// A stationary turret-style enemy that fires an aimed fireball directly at the player
// every 180 frames. Does not move or respond to knockback.
public class FireShooter extends Entity {

    private final GamePanel gp;
    private final Projectile projectile; // the aimed fireball this enemy fires
    private int shotCounter = 0;         // counts frames between shots
    private double dirX = 0;             // normalized X component of the aim direction
    private double dirY = 0;             // normalized Y component of the aim direction

    public FireShooter(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = TYPE_MONSTER;
        name = "Fire Shooter";
        speed = 0; // stationary — never moves
        width = gp.tileSize;
        height = gp.tileSize;
        image = setup("/monsters/fireshooter"); // load sprite directly, no black-pixel hitbox scan needed
        int hitboxWidth = 42;
        int hitboxHeight = 42;
        solidArea = new Rectangle(
                6,
                6,
                hitboxWidth,
                hitboxHeight
        );
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        direction = 'L';
        directionBeforeKnockBack = 'L';

        maxLife = 8;
        life = maxLife;
        projectile = new PT_AimedFireball(gp);
    }

    // Exposes the projectile so GamePanel can check for mid-air clashes with the player's fireball
    public Projectile getProjectile() {
        return projectile;
    }

    @Override
    public void update() {
        // Count down invincibility frames after being hit
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        // FireShooter never moves, so knockback is always discarded
        knockBack = false;

        // Move the active projectile and check if it hits the player or player's fireball
        updateProjectileInteractions();

        // Freeze frames pause shooting briefly after being hit
        if (freezeFrames > 0) {
            freezeFrames--;
            return;
        }

        // Deal contact damage if the player walks into the turret
        if (gp.collisionsystem.collidesWithPlayer(this)) {
            gp.player.damagePlayer();
        }
        collisionOn = false; // reset the flag set as a side-effect by collidesWithPlayer

        // Fire a new aimed projectile every 180 frames
        shotCounter++;
        if (shotCounter > 180 && !projectile.alive) {
            aimAndShoot();
            shotCounter = 0;
        }
    }

    // Calculates the direction vector from the turret center to the player center,
    // passes it to the projectile, then launches it
    private void aimAndShoot() {
        int monsterCenterX = x + width / 2;
        int monsterCenterY = y + height / 2;
        int playerCenterX = gp.player.x + gp.player.solidArea.x + gp.player.solidArea.width / 2;
        int playerCenterY = gp.player.y + gp.player.solidArea.y + gp.player.solidArea.height / 2;

        double dx = playerCenterX - monsterCenterX;
        double dy = playerCenterY - monsterCenterY;
        double length = Math.sqrt(dx * dx + dy * dy);

        if (length == 0) return; // player is exactly on top of turret — skip

        // Normalize to a unit vector so speed is consistent regardless of distance
        dirX = dx / length;
        dirY = dy / length;

        if (projectile instanceof PT_AimedFireball aimed) {
            aimed.setDirection(dirX, dirY);
        }

        int projectileX = x + (width - projectile.width) / 2;
        int projectileY = y + (height - projectile.height) / 2;

        // Face the turret sprite toward the player for visual feedback
        char facing = (dirX >= 0) ? 'R' : 'L';
        direction = facing;
        projectile.set(projectileX, projectileY, facing, true);
    }

    // Moves the active projectile and checks if it hits the player or the player's fireball
    private void updateProjectileInteractions() {
        if (!projectile.alive) return;

        projectile.update();

        Rectangle projectileBox = projectile.getCollisionBox();

        // Cancel both projectiles if they collide mid-air
        if (gp.player.projectile != null
                && gp.player.projectile.alive
                && projectileBox.intersects(gp.player.projectile.getCollisionBox())) {
            projectile.alive = false;
            gp.player.projectile.alive = false;
            return;
        }

        Rectangle playerBox = new Rectangle(
                gp.player.x + gp.player.solidArea.x,
                gp.player.y + gp.player.solidArea.y,
                gp.player.solidArea.width,
                gp.player.solidArea.height
        );

        // Deal damage if the projectile reaches the player
        if (projectileBox.intersects(playerBox)) {
            if (!gp.player.invincible) {
                gp.player.life -= projectile.damage;
                gp.player.invincible = true;
            }
            projectile.alive = false;
        }
    }

    // Random loot drop weighted toward coins and hearts
    @Override
    public void checkDrop() {
        int random = new Random().nextInt(100);
        if (random < 25) {
            dropItem(new OBJ_Coin());
        } else if (random < 55) {
            dropItem(new OBJ_Heart());
        } else if (random < 80) {
            dropItem(new OBJ_ManaPotion());
        } else {
            dropItem(new OBJ_HealingPotion());
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        int screenX = x - gp.camera.x;
        int screenY = y - gp.camera.y;

        // Still draw the projectile even when the turret itself is off-screen
        if (x + width < gp.camera.x ||
                x > gp.camera.x + gp.screenWidth ||
                y + height < gp.camera.y ||
                y > gp.camera.y + gp.screenHeight) {
            projectile.draw(g2);
            return;
        }

        // Flash semi-transparent while invincible
        if (invincible) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        }

        g2.drawImage(image, screenX, screenY, width, height, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // Health bar shown once the enemy has taken damage
        if (life < maxLife) {
            int barWidth = width - 12;
            int currentLifeWidth = barWidth * life / maxLife;

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(screenX + 6, screenY + 6, barWidth, 6);
            g2.setColor(Color.RED);
            g2.fillRect(screenX + 6, screenY + 6, currentLifeWidth, 6);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }

        projectile.draw(g2);
    }
}
