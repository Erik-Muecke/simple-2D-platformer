package monster;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Coin;
import object.OBJ_HealingPotion;
import object.OBJ_Heart;
import object.OBJ_ManaPotion;
import projectile.PT_Fireball;
import projectile.Projectile;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

// A flying enemy that hovers and fires two simultaneous downward fireballs toward the ground.
// Tracks the player horizontally and shoots in bursts.
public class FireFlyer extends Entity {

    private final GamePanel gp;
    private final Random random = new Random();
    private final Projectile projectile;  // left fireball
    private final Projectile projectile2; // right fireball, fired alongside the first
    private int shotCounter = 0;          // counts frames between shots

    public FireFlyer(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = TYPE_MONSTER;
        name = "Fire Flyer";
        speed = 2;
        width = gp.tileSize;
        height = gp.tileSize;
        setImageAndSolidAreaFromBlackPixels("/monsters/fireflyer");
        direction = 'L';
        directionBeforeKnockBack = 'L';

        maxLife = 5;
        life = maxLife;
        projectile  = new PT_Fireball(gp);
        projectile2 = new PT_Fireball(gp);
    }

    // Exposes the primary projectile so GamePanel can check for mid-air clashes
    public Projectile getProjectile() {
        return projectile;
    }

    // Turns to face the player each frame
    public void setAction() {
        if(gp.player.x < this.x){
            this.direction = 'L';
        } else if(gp.player.x > this.x){
            this.direction = 'R';
        }
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

        // Move and check collisions for both projectiles
        updateProjectileInteractions();

        // During knockback move in the hit direction and skip normal AI
        if (knockBack) {
            boolean stillKnockedBack = gp.movementSystem.updateMonsterKnockBack(this);
            if (!stillKnockedBack) {
                // Pull back shot counter so recovery doesn't immediately trigger another shot
                shotCounter = shotCounter-10;
            }
            return;
        }

        // Fire two projectiles side-by-side every 180 frames when both are inactive
        shotCounter++;
        if (shotCounter > 180 && !projectile.alive) {
            shotCounter++;
            if (shotCounter > 180 && !projectile.alive && !projectile2.alive) {
                int centerX = x + (width - projectile.width) / 2;
                int centerY = y + (height - projectile.height) / 2;

                projectile.set(centerX - 18, centerY, 'D', true);  // left fireball offset
                projectile2.set(centerX + 18, centerY, 'D', true); // right fireball offset
                shotCounter = 0;
            }
        }

        // Freeze frames pause movement briefly after being hit
        if (freezeFrames > 0) {
            freezeFrames--;
            return;
        }

        setAction();
        gp.movementSystem.updateFlyingMonster(this);
    }

    // Delegates projectile update and hit detection to a shared helper
    private void updateProjectileInteractions() {
        handleProjectile(projectile);
        handleProjectile(projectile2);
    }

    // Moves the given projectile and checks if it hits the player or the player's fireball
    private void handleProjectile(Projectile p) {
        if (!p.alive) return;

        p.update();

        Rectangle projectileBox = p.getCollisionBox();

        // Cancel both projectiles if they collide mid-air
        if (gp.player.projectile != null
                && gp.player.projectile.alive
                && projectileBox.intersects(gp.player.projectile.getCollisionBox())) {
            p.alive = false;
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
                gp.player.life -= p.damage;
                gp.player.invincible = true;
            }
            p.alive = false;
        }
    }

    // Random loot drop weighted toward coins and hearts
    @Override
    public void checkDrop() {
        int random = new Random().nextInt(100);
        if(random < 25) {
            dropItem(new OBJ_Coin());
        }
        else if(random >= 25 && random < 55) {
            dropItem(new OBJ_Heart());
        }
        else if(random >= 55 && random < 80) {
            dropItem(new OBJ_ManaPotion());
        }
        else{
            dropItem(new OBJ_HealingPotion());
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        int screenX = x - gp.camera.x;
        int screenY = y - gp.camera.y;

        // Still draw projectiles even when the enemy is off-screen
        if (x + width < gp.camera.x ||
                x > gp.camera.x + gp.screenWidth ||
                y + height < gp.camera.y ||
                y > gp.camera.y + gp.screenHeight) {
            projectile.draw(g2);
            projectile2.draw(g2);
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
        projectile2.draw(g2);
    }
}
