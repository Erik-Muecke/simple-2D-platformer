package projectile;

import main.GamePanel;

/**
 * A fireball that travels in an arbitrary 2D direction (not just L/R/U/D).
 * The direction vector (dirX, dirY) must be set before calling set() via setDirection().
 */
public class PT_AimedFireball extends Projectile {

    private final GamePanel gp;
    private double dirX = 1;
    private double dirY = 0;

    // Sub-pixel accumulation to keep movement smooth at any angle
    private double subX = 0;
    private double subY = 0;

    public PT_AimedFireball(GamePanel gp) {
        super(gp);
        this.gp = gp;

        speed = 5;
        maxLife = 80;
        damage = 2;
        useCost = 1;

        width = gp.tileSize * 2 / 3;
        height = gp.tileSize * 2 / 3;
        int hitboxWidth = width / 2;
        int hitboxHeight = height / 2;
        solidArea.setBounds(
                (width - hitboxWidth) / 2,
                (height - hitboxHeight) / 2,
                hitboxWidth,
                hitboxHeight
        );
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {
        img1 = setup("/projectile/fireball_right");
        img2 = setup("/projectile/fireball_left");
    }

    /**
     * Sets the normalized direction vector toward the target.
     * Call this before set().
     */
    public void setDirection(double dirX, double dirY) {
        this.dirX = dirX;
        this.dirY = dirY;
    }

    @Override
    public void update() {
        if (!alive) return;

        // Accumulate movement in floating point, then apply integer pixels
        subX += dirX * speed;
        subY += dirY * speed;

        x = (int) Math.round(subX);
        y = (int) Math.round(subY);

        // Tile collision — kill the projectile if it hits a solid tile
        collisionOn = false;
        gp.collisionsystem.collidesT(this);
        if (collisionOn) {
            alive = false;
            return;
        }

        life--;
        if (life <= 0) {
            alive = false;
        }
    }

    @Override
    public void set(int x, int y, char direction, boolean alive) {
        super.set(x, y, direction, alive);
        // Seed the sub-pixel accumulators at spawn position
        subX = x;
        subY = y;
        life = maxLife;
    }
}