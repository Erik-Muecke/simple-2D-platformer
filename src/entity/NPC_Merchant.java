package entity;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class NPC_Merchant extends Entity {

    GamePanel gp; // reference to GamePanel for camera + world access

    public NPC_Merchant(GamePanel gp) {

        super(gp); // initialize base Entity with GamePanel
        this.gp = gp;

        name = "merchant"; // internal npc identifier

        type = TYPE_NPC; // defines this entity as NPC
        direction = 'D'; // default facing direction
        speed = 0; // merchant does not move

        width = 32 * GamePanel.scale; // sprite width
        height = 32 * GamePanel.scale; // sprite height

        getNPCImage(); // loads merchant sprite
        setDialogue(); // sets merchant dialogue lines
        setItems(); // fills merchant shop inventory

        solidArea = new java.awt.Rectangle(8, 8, 48, 48); // collision box for interaction
        solidAreaDefaultX = solidArea.x; // default hitbox x offset
        solidAreaDefaultY = solidArea.y; // default hitbox y offset
    }

    public void getNPCImage() {

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/npc/merchant.png"));
            // loads merchant sprite from resources
        } catch (IOException e) {
            e.printStackTrace(); // prints error if image loading fails
        }
    }

    public void setDialogue() {

        dialogues[0] = "Hello, traveler! I have fine wares for sale. Please have a look at them."; // greeting line
        dialogues[1] = "If you want to sell something, just tell it me."; // trading explanation
        dialogues[2] = "Press ENTER to open my shop."; // interaction hint
    }

    public void setItems() {

        shopInventory[0] = new object.OBJ_HealingPotion(); // healing potion in shop
        shopInventory[1] = new object.OBJ_ManaPotion(); // mana potion in shop
    }

    @Override
    public void draw(Graphics2D g2) {

        if (image != null) {

            int screenX = x - gp.camera.x; // world x → screen x conversion
            int screenY = y - gp.camera.y; // world y → screen y conversion

            g2.drawImage(image, screenX, screenY, width, height, null); // draw merchant
        }
    }

    @Override
    public void update() {
        // static NPC (no movementlogic needed)
    }
}