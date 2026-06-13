package monster;

import entity.Entity;
import main.GamePanel;
import object.OBJ_HealingPotion;
import object.OBJ_ManaPotion;
import projectile.PT_Fireball;
import projectile.Projectile;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

// The final boss enemy — a large slime that chases the player, periodically jumps,
// and fires fireballs. Only becomes active after the player opens the boss door (boss1 == true).
public class BossSlime extends Entity {

    private final GamePanel gp;
    private final Projectile projectile; // the fireball the boss shoots at the player
    private int shotCounter = 0;         // counts frames between each shot
    private final int jumpStrength = 25; // upward velocity applied when the boss jumps
    private int jumpPower;               // accumulates each frame; triggers a jump when it reaches 300

    public BossSlime(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = TYPE_MONSTER;
        name = "Boss Slime";
        speed = 2;
        width = (int)(gp.tileSize * 1.5);  // slightly larger than a normal tile
        height = (int)(gp.tileSize * 1.5);
        setImageAndSolidAreaFromBlackPixels("/monsters/bossslime1"); // derives hitbox from sprite black pixels
        direction = 'L';
        directionBeforeKnockBack = 'L';

        maxLife = 30;
        life = maxLife;
        projectile = new PT_Fireball(gp);
    }

    // Allows GamePanel to check if the boss projectile clashes with the player's projectile
    public Projectile getProjectile() {
        return projectile;
    }

    // Turns the boss to face the player each frame
    public void setAction() {
        if(gp.player.x < this.x){
            this.direction = 'L';
        } else if(gp.player.x > this.x){
            this.direction = 'R';
        }
    }

    // Alternates between two sprite frames to create a simple walking animation
    public void setWalking() {
        walkingCounter++;

        if (walkingCounter >= 20) {
            image = setup("/monsters/bossslime");
            if (walkingCounter >= 40) {
                image = setup("/monsters/bossslime1");
                walkingCounter = 0;
            }
        }
    }

    @Override
    public void update() {
        // Boss logic is completely disabled until the player triggers the boss fight
        if (gp.player.boss1) {
            // Accumulate jump power every frame; used to trigger periodic jumps
            jumpPower++;

            // Count down invincibility frames after being hit
            if (invincible) {
                invincibleCounter++;
                if (invincibleCounter > 20) {
                    invincible = false;
                    invincibleCounter = 0;
                }
            }

            // Trigger a jump when enough power has built up and the boss is on the ground
            if (jumpPower >= 300 && onGround) {
                gp.movementSystem.startJump(this, jumpStrength);
                jumpPower = 0;
            }

            // Handle projectile movement and collision with player / player's projectile
            updateProjectileInteractions();

            // During knockback the boss moves in the hit direction; skip normal AI
            if (knockBack) {
                boolean stillKnockedBack = gp.movementSystem.updateMonsterKnockBack(this);
                if (!stillKnockedBack) {
                    // Reduce shot counter so the boss doesn't immediately fire after recovering
                    shotCounter = shotCounter - 5;
                }
                return;
            }

            // Fire a projectile toward the player every 150 frames
            shotCounter++;
            if (shotCounter > 150 && !projectile.alive) {
                int projectileX = x + (width - projectile.width) / 2;
                int projectileY = y + (height - projectile.height) / 2;
                projectile.set(projectileX, projectileY, direction, true);
                shotCounter = 0;
            }

            // Freeze frames pause all movement briefly after being hit
            if (freezeFrames > 0) {
                freezeFrames--;
                return;
            }

            setAction();
            setWalking();
            gp.movementSystem.updateWalkingMonster(this);
        }
    }

    // Moves the active projectile and checks whether it hits the player or is cancelled by the player's fireball
    private void updateProjectileInteractions() {
            if (!projectile.alive) {
                return;
            }

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

            // Deal damage to the player if the projectile reaches them
            if (projectileBox.intersects(playerBox)) {
                if (!gp.player.invincible) {
                    gp.player.life -= projectile.damage;
                    gp.player.invincible = true;
                }
                projectile.alive = false;
            }
        }

    // Drops both a healing potion and a mana potion when the boss is defeated
    @Override
    public void checkDrop(){
        dropItem(new OBJ_HealingPotion());
        dropItem(new OBJ_ManaPotion());
    }

    @Override
    public void draw(Graphics2D g2) {
        int screenX = x - gp.camera.x;
        int screenY = y - gp.camera.y;

        // Still draw the projectile even when the boss itself is off-screen
        if (x + width < gp.camera.x ||
                x > gp.camera.x + gp.screenWidth ||
                y + height < gp.camera.y ||
                y > gp.camera.y + gp.screenHeight) {
            projectile.draw(g2);
            return;
        }

        // Flash semi-transparent while invincible to give hit feedback
        if (invincible) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        }

        g2.drawImage(image, screenX, screenY, width, height, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // Draw a health bar above the boss once it has taken any damage
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
