package entity;

import main.GamePanel;

import javax.imageio.ImageIO; // used to load npc sprite image
import java.awt.*; // graphics (Graphics2D, Rectangle)
import java.io.IOException; // image loading error handling

public class NPC_Backpacker extends Entity {

    GamePanel gp; // reference to GamePanel for camera + world access

    public NPC_Backpacker(GamePanel gp) {

        super(gp); // calls Entity constructor and sets gp in parent class
        this.gp = gp;

        name = "backpacker"; // internal npc identifier

        type = TYPE_NPC; // defines this entity as NPC
        direction = 'D'; // default facing direction
        speed = 0; // npc does not move

        width = 32 * GamePanel.scale; // npc width in pixels
        height = 32 * GamePanel.scale; // npc height in pixels

        getNPCImage(); // loads npc sprite
        setDialogue(); // sets npc dialogue lines

        solidArea = new java.awt.Rectangle(8, 8, 48, 48); // collision box
        solidAreaDefaultX = solidArea.x; // saves default hitbox x offset
        solidAreaDefaultY = solidArea.y; // saves default hitbox y offset
    }

    public void getNPCImage() {

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/npc/backpacker.png"));
            // loads npc sprite from resources
        } catch (IOException e) {
            e.printStackTrace(); // prints error if image not found
        }
    }

    public void setDialogue() {

        dialogues[0] = "Hello, traveler! Ihave a hint, where the merchant could be."; // intro line
        dialogues[1] = "You want this hint?"; // question line
        dialogues[2] = "Then find first the entrance to the hidden room in this area, and then we can talk."; // hint line
    }

    @Override
    public void draw(Graphics2D g2) {

        if (image != null) {

            int screenX = x - gp.camera.x; // convert world x to screen x
            int screenY = y - gp.camera.y; // convert world y to screen y

            g2.drawImage(image, screenX, screenY, width, height, null); // draw npc
        }
    }

    @Override
    public void update() {
        // npc is static (no movementlogic needed)
    }
}