package projectile;

import main.GamePanel;

public class PT_AimedFireball extends Projectile {

    private final GamePanel gp;

    private double dirX = 1;
    private double dirY = 0;
    //movement direction

    private double subX = 0;
    private double subY = 0;
    //exact position with decimals for smooth movement

    public PT_AimedFireball(GamePanel gp) {

        super(gp);
        this.gp = gp;

        speed = 5;
        maxLife = 80;
        damage = 2;
        useCost = 1;

        width = gp.tileSize * 2 / 3;
        height = gp.tileSize * 2 / 3;
        //projectile size = 2/3 of a tile

        int hitboxWidth = width / 2;
        int hitboxHeight = height / 2;
        //hitbox is smaller than the sprite

        solidArea.setBounds(
                (width - hitboxWidth) / 2,
                //(full size - hitbox size) / 2 centers the hitbox

                (height - hitboxHeight) / 2,
                hitboxWidth,
                hitboxHeight
        );

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {//loads projectile sprites

        img1 = setup("/projectile/fireball_right");
        img2 = setup("/projectile/fireball_left");
    }

    public void setDirection(double dirX, double dirY) {
        //sets the movement direction

        this.dirX = dirX;
        this.dirY = dirY;
    }

    @Override
    public void update() {

        if (!alive) return;

        subX += dirX * speed;
        subY += dirY * speed;
        //direction × speed = movement every frame

        x = (int) Math.round(subX);
        y = (int) Math.round(subY);
        //converts decimal positions into pixels

        collisionOn = false;
        gp.collisionsystem.collidesT(this);
        //checks tile collision

        if (collisionOn) {
            alive = false;
            return;
        }

        life--;
        //projectile lifetime goes down every frame

        if (life <= 0) {
            alive = false;
        }
    }

    @Override
    public void set(int x, int y, char direction, boolean alive) {

        super.set(x, y, direction, alive);
        //sets normal projectile values

        subX = x;
        subY = y;
        //starts decimal position at spawn point

        life = maxLife;
        //resets projectile lifetime
    }
}