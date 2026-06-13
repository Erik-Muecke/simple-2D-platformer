package entity;

import object.SuperObject;

import java.awt.*; // graphics classes (Color, Rectangle, etc.)
import java.awt.image.BufferedImage; // image storage for sprites
import java.io.IOException; // error handling for file loading
import java.io.InputStream; // reading resources from jar/classpath
import javax.imageio.ImageIO; // loading images from streams
import main.GamePanel; // access to main game systems

public class Entity {

    private final GamePanel gp; // reference to main game system

    public static final int TYPE_PLAYER = 0;
    public static final int TYPE_NPC = 1;
    public static final int TYPE_MONSTER = 2; // entity type identifiers

    public int type = TYPE_NPC; // current entity type

    public int x;  // position on x-axis
    public int y;  // position on y-axis

    public int width; // width of entity
    public int height; // height of entity

    public Image image; // current sprite image
    public boolean isDead = false; // marks entity as dead/removed

    int startX; // spawn x position
    int startY; // spawn y position

    public char direction = 'U'; // direction entity is facing
    public char directionBeforeKnockBack; // saved direction before knockback

    public boolean onGround = true; // checks if entity is on ground

    public int velocityX = 0; // movement in x direction
    public int velocityY = 0; // movement in y direction

    public int speed; // movement speed
    public int freezeFrames = 0; // stun / delay frames

    public Rectangle solidArea; // collision box
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0); // attack hitbox

    public int attackWidth; // attack width
    public int attackHeight; // attack height

    public int solidAreaDefaultX; // default hitbox x offset
    public int solidAreaDefaultY; // default hitbox y offset

    public boolean collisionOn = false; // collision flag

    public String name; // entity name/id

    public int maxLife; // maximum HP
    public int life; // current HP

    public boolean invincible = false; // temporary damage immunity
    public int invincibleCounter = 0; // invincibility timer

    public int actionLockCounter; // AI action delay timer
    public int walkingCounter; // animation counter

    public boolean knockBack = false; // knockback active flag
    public int knockBackCounter = 0; // knockback duration

    public String[] dialogues = new String[20]; // NPC dialogue lines
    public int dialogueIndex = 0; // current dialogue line

    public SuperObject[] shopInventory = new SuperObject[20]; // shop items

    public BufferedImage img1, img2, img3, img4, img5, img6; // animation frames

    public Entity(GamePanel gp) { this.gp = gp; } // main constructor with GamePanel

    public Entity() { this.gp = null; } // empty constructor (no GamePanel)

    public void update() { } // base update (overwritten in subclasses)

    public void checkDrop() { } // called on death for item drops

    public void dropItem(SuperObject droppedItem) {
        for(int i = 0; i < gp.obj.length; i++) {
            if(gp.obj[i] == null) {
                gp.obj[i] = droppedItem; gp.obj[i].x = x; gp.obj[i].y = y; break;
                // place item in the first free slot at entity position
            }
        }
    }

    public void draw(Graphics2D g2) {
        if (image != null) {
            g2.drawImage(image, this.x, this.y, this.width, this.height, null); // draw sprite
        } else {
            g2.setColor(Color.RED); g2.fillRect(x, y, width, height); // fallback rectangle if no image
        }
    }

    public BufferedImage setup(String imagePath) {
        String path = imagePath.endsWith(".png") ? imagePath : imagePath + ".png"; // ensure .png extension

        try (InputStream stream = getClass().getResourceAsStream(path)) {
            if (stream != null) return ImageIO.read(stream); // load image if found
        } catch (IOException e) { System.err.println("Fehler beim Laden: " + path); }

        try (InputStream stream = getClass().getResourceAsStream("/res/missing/image_not_found.png")) {
            if (stream != null) return ImageIO.read(stream); // fallback image
        } catch (IOException e) { System.err.println("Fallback Fehler: " + e.getMessage()); }

        BufferedImage fallback = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = fallback.createGraphics();
        g2.setColor(Color.MAGENTA); g2.fillRect(0, 0, fallback.getWidth(), fallback.getHeight());
        g2.dispose();
        return fallback; // last-resort placeholder image
    }

    protected void setImageAndSolidAreaFromBlackPixels(String imagePath) {
        BufferedImage sprite = setup(imagePath); image = sprite;

        int minX = sprite.getWidth(), minY = sprite.getHeight(), maxX = -1, maxY = -1; // bounding box

        for (int y = 0; y < sprite.getHeight(); y++) {
            for (int x = 0; x < sprite.getWidth(); x++) {

                Color color = new Color(sprite.getRGB(x, y), true);

                if (color.getAlpha() > 0 && color.getRed() <= 40 && color.getGreen() <= 40 && color.getBlue() <= 40) {
                    minX = Math.min(minX, x); minY = Math.min(minY, y);
                    maxX = Math.max(maxX, x); maxY = Math.max(maxY, y);
                }
            }
        }

        if (maxX < minX || maxY < minY) {
            solidArea = new Rectangle(0, 0, width, height); // fallback hitbox
        } else {
            int scaledX = (int) Math.round((double) minX * width / sprite.getWidth());
            int scaledY = (int) Math.round((double) minY * height / sprite.getHeight());
            int scaledRight = (int) Math.round((double) (maxX + 1) * width / sprite.getWidth());
            int scaledBottom = (int) Math.round((double) (maxY + 1) * height / sprite.getHeight());
            // scale sprite pixels to world size

            solidArea = new Rectangle(scaledX, scaledY, scaledRight - scaledX, scaledBottom - scaledY);
        }

        solidAreaDefaultX = solidArea.x; solidAreaDefaultY = solidArea.y; // save defaults
    }

    void reset() {
        this.x = this.startX; this.y = this.startY; // reset position to spawn
    }
}