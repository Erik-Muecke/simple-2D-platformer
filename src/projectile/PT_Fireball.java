package projectile;

import main.GamePanel;

public class PT_Fireball extends Projectile {

    public PT_Fireball(GamePanel gp) {

        super(gp);

        speed = 5;
        maxLife = 80;
        damage = 2;
        useCost = 1;

        width = gp.tileSize * 2 / 3;
        height = gp.tileSize * 2 / 3;
        //projectile size

        int hitboxWidth = width / 2;
        int hitboxHeight = height / 2;
        //smaller hitbox for better collision feeling

        solidArea.setBounds(
                (width - hitboxWidth) / 2,
                //centers hitbox horizontally

                (height - hitboxHeight) / 2,
                //centers hitbox vertically

                hitboxWidth,
                hitboxHeight
        );

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {//loads fireball sprites

        img1 = setup("/projectile/fireball_right");
        img2 = setup("/projectile/fireball_left");
        img3 = setup("/projectile/fireball_down");
    }
}