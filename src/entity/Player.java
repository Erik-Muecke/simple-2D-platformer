package entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.util.ArrayList;

import projectile.PT_Fireball;
import projectile.Projectile;
import tile.TileManager;
import object.SuperObject;

import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {

    private final GamePanel gp;
    private final KeyHandler keyH;
    private TileManager tileManager;
    private int jumpStrength;
//  private int gravity = 2;
//  private int maxFallSpeed = 12;
    public int hasKey = 0;
    public int hasSpKey = 0;
    public int hasCoin = 0;

    public int maxLife;
    public int life;
    public boolean invincible = false;
    public int invincibleCounter = 0;
    // Player additions
    public Projectile projectile;
    private int doorMessageCooldown = 0;
    private int boostMessageCooldown = 0;
    private BufferedImage frontImage;
    private BufferedImage frontStepImage;
    private BufferedImage leftImage;
    private BufferedImage leftStepImage;
    private BufferedImage rightImage;
    private BufferedImage rightStepImage;


    public int mana;
    public int maxMana;
    public int manaCounter = 0;

    public int normalSpeed = 4;
    private int speedBoostCounter = 0;
    public boolean speedBoostActive = false;

    public int normalJumpStrength = 30;
    private int jumpStrengthBoostCounter = 0;
    public boolean jumpStrengthBoostActive = false;
    public boolean attacking = false;
    public boolean boss1 = false;

    public ArrayList<SuperObject> inventory = new ArrayList<>();

    public Player(GamePanel gp, KeyHandler keyH) {
        super();
        speed = normalSpeed; //Geschwindigkeit des Spielers, wie viele Pixel er sich pro Update bewegen soll
        jumpStrength = normalJumpStrength;
        width = 32 * GamePanel.scale; //Breite des Spielers in Pixeln
        height = 32 * GamePanel.scale; //Höhe des Spielers in Pixeln
        direction = 'D';
        this.gp = gp;
        this.keyH = keyH;

        // The hitbox is smaller than the sprite so collisions feel fair near transparent pixels.
        int hitboxWidth = 48;
        int hitboxHeight = 48;
        solidArea = new Rectangle(
                (width - hitboxWidth) / 2,  //inkrement um das die x position verschoben wird, damit die box mittig ist
                (height - hitboxHeight) / 2,  //inkrement um das die y position verschoben wird, damit die box mittig ist
                hitboxWidth,
                hitboxHeight
        );

        projectile = new PT_Fireball(gp);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;//declaring the solid parts of the player
        maxLife = 20;
        life = maxLife;
        maxMana = 5;
        mana = maxMana;
        attackWidth = gp.tileSize-16;
        attackHeight = gp.tileSize-16;
        loadPlayerImage();
    }

    public void loadPlayerImage() {
        try {
            frontImage = loadImage("/player/playerfront.png");
            frontStepImage = loadImage("/player/playerfront1.png");
            leftImage = loadImage("/player/playerleft.png");
            leftStepImage = loadImage("/player/playerleft1.png");
            rightImage = loadImage("/player/playerright.png");
            rightStepImage = loadImage("/player/playerright1.png");
            image = frontImage;
        } catch (IOException e) {
            System.err.println("Fehler beim Laden der Player-Sprites: " + e.getMessage());
        }//laden der verschiedenen Player Bilder
    }

    private BufferedImage loadImage(String path) throws IOException {
        InputStream stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            throw new IOException("Ressource nicht gefunden: " + path);
        }
        return ImageIO.read(stream);
    }

    public void setWalking() {

        walkingCounter++;

        // LEFT
        if (direction == 'L') {

            if (velocityX != 0) {

                // default image immediately
                image = leftImage;

                // walking animation
                if (walkingCounter >= 30) {
                    image = leftImage;
                    walkingCounter = 0;

                } else if (walkingCounter >= 15) {
                    image = leftStepImage;
                }

            } else {

                // idle animation
                if (walkingCounter >= 300 && walkingCounter < 480) {
                    image = frontStepImage;

                } else if (walkingCounter >= 180) {
                    image = frontImage;

                } else {
                    image = leftImage;
                }
            }
        }

        // RIGHT
        else if (direction == 'R') {

            if (velocityX != 0) {

                // default image immediately
                image = rightImage;

                // walking animation
                if (walkingCounter >= 30) {
                    image = rightImage;
                    walkingCounter = 0;

                } else if (walkingCounter >= 15) {
                    image = rightStepImage;
                }

            } else {

                // idle animation
                if (walkingCounter >= 300 && walkingCounter < 480) {
                    image = frontStepImage;

                } else if (walkingCounter >= 180) {
                    image = frontImage;

                } else {
                    image = rightImage;
                }
            }
        }

        // reset long idle cycle
        if (walkingCounter >= 480) {
            walkingCounter = 0;
        }
    }

    public void update() {
        // Temporary boosts count down in frames because the game loop runs at a fixed FPS.
        if(speedBoostActive) {
            speedBoostCounter++;
            if(speedBoostCounter >= 300) {
                speed = normalSpeed;
                speedBoostActive = false;
            }
        }

        if(jumpStrengthBoostActive) {
            jumpStrengthBoostCounter++;
            if(jumpStrengthBoostCounter >= 300) {
                jumpStrength = normalJumpStrength;
                jumpStrengthBoostActive = false;
            }
        }

        if (doorMessageCooldown > 0){
            doorMessageCooldown--;
        }

        if (boostMessageCooldown > 0){
            boostMessageCooldown--;
        }
        // Convert pressed key flags into velocity; MovementSystem applies collision afterward.
        if (keyH.leftPressed) {
            direction = 'L';
            velocityX = -speed;
        } else if (keyH.rightPressed) {
            direction = 'R';
            velocityX = speed;
        } else {
            velocityX = 0;
        }

        setWalking();
        int npcIndex = gp.collisionsystem.collidesWithNPC(this);
        if (keyH.enterPressed && !gp.eHandler.isPlayerOnHealingPool()) {
            if (npcIndex != 999) {
                interactWithNPC(npcIndex);
            } else {
                attack();
                keyH.enterPressed = false;
            }
        }

        // Jump starts only from the ground to prevent infinite air jumps.
        if (keyH.jumpPressed && onGround) {
            velocityY = -jumpStrength;
            onGround = false;
        }

        if (invincible == true) {
            invincibleCounter++;
            if (invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }

        }

        if(keyH.shotKeyPressed && projectile.alive == false &&
                mana >= projectile.useCost) {
            // Reuse one projectile instance; it becomes active again when fired.
            int projectileX = x + (width - projectile.width) / 2;
            int projectileY = y + (height - projectile.height) / 2;
            char projectileDirection = direction;

            if (projectileDirection != 'L' && projectileDirection != 'R') {
                projectileDirection = 'R';
            }

            mana -= projectile.useCost;
            walkingCounter = 0;
            projectile.set(projectileX, projectileY, projectileDirection, true);
            keyH.shotKeyPressed = false;
        }

        int objectIndex = gp.collisionsystem.collisionObject(this, true);
        pickUpObject(objectIndex);

        // Movement is handled after input and object checks so collisions use current velocity.
        gp.movementSystem.updatePlayer(this);

        if(life <= 0) {
            life = 0;
            gp.gameOver = true;
        }

        if (mana == 5){
            return;
        }

        manaCounter++;
        if(manaCounter > 300) {
            if(mana < maxMana) {
                mana++;
            }
            manaCounter = 0;
        }
    }

    public void resetBoosts() {
        speed = normalSpeed;
        jumpStrength = normalJumpStrength;
        speedBoostActive = false;
        jumpStrengthBoostActive = false;
        speedBoostCounter = 0;
        jumpStrengthBoostCounter = 0;
    }

    public void pickUpObject(int i) {
        if (i != 999) {
            String objectName = gp.obj[i].name;
            // Object names are the gameplay identifiers used by the pickup/interaction switch.
            switch (objectName) {
                case "Key":
                    hasKey++;
                    addItemToInventory(gp.obj[i]);
                    System.out.println("You got a key! Total: " + hasKey);
                    gp.obj[i] = null;
                    gp.ui.addMessage("You got a key! Total: " + hasKey);
                    break;

                case "Door":
                    if (hasKey > 0) {
                        System.out.println("You opened the door!");
                        gp.ui.addMessage("You opened the door!");
                        hasKey--;
                        removeItemFromInventory("Key");
                        gp.obj[i] = null; // door disappears
                    } else if (doorMessageCooldown == 0){
                        System.out.println("You need a key!");
                        gp.ui.addMessage("You need a key!");
                        doorMessageCooldown = 120;
                    }
                    break;

                case "Special Key":
                    hasSpKey++;
                    addItemToInventory(gp.obj[i]);
                    System.out.println("You got a special key! Total: " + hasSpKey);
                    gp.obj[i] = null;
                    gp.ui.addMessage("You got a special key! Total: " + hasSpKey);
                    break;

                case "Special Door":
                    if (hasSpKey > 0) {
                        System.out.println("You opened the special door!");
                        gp.ui.addMessage("You opened the special door!");
                        hasSpKey--;
                        removeItemFromInventory("Special Key");
                        gp.obj[i] = null; // door disappears
                    } else if (doorMessageCooldown == 0){
                        System.out.println("You need a special key!");
                        gp.ui.addMessage("You need a special key!");
                        doorMessageCooldown = 120;
                    }
                    break;

                case "Boss Door":
                    if(boss1 == false){
                        // Opening the boss door seals the arena by spawning a second door behind the player.
                        boss1 = true;
                        System.out.println("opened the Boss door. Now comes the Boss");
                        gp.obj[i] = null;
                        gp.obj[11] = new object.OBJ_BossDoor();
                        gp.obj[11].x = 16 * gp.tileSize;
                        gp.obj[11].y = 30 * gp.tileSize;
                        gp.ui.addMessage("opened the Boss door. Now comes the Boss");
                        this.x = this.x + 2*gp.tileSize;
                    }
                    break;

                case "SpeedBoost":
                    activateSpeedBoost();
                    gp.ui.addMessage("Speed Boost!");
                    gp.obj[i] = null;
                    break;

                case "SpeedBooster":
                    activateSpeedBoost();
                    if (boostMessageCooldown == 0) {
                        System.out.println("Speed boost!");
                        gp.ui.addMessage("Speed boost!");
                        boostMessageCooldown = 120;
                    }
                    break;

                case "JumpBooster":
                    activateJumpBoost();
                    if (boostMessageCooldown == 0) {
                        System.out.println("Jump boost!");
                        gp.ui.addMessage("Jump boost!");
                        boostMessageCooldown = 120;
                    }
                    break;

                case "JumpBoost":
                    activateJumpBoost();
                    gp.ui.addMessage("Jump Boost!");
                    gp.obj[i] = null;
                    break;

                case "Healing Potion":
                    addItemToInventory(gp.obj[i].copy());
                    gp.ui.addMessage("You got a healing potion");
                    gp.obj[i] = null;
                    break;

                case "Mana Potion":
                    addItemToInventory(gp.obj[i].copy());
                    gp.ui.addMessage("You got a mana potion");
                    gp.obj[i] = null;
                    break;

                case "Heart":
                    gp.ui.addMessage("You healed yourself");
                    this.life = Math.min(this.life + 2, this.maxLife);
                    gp.obj[i] = null;
                    break;

                case "Coin":
                    gp.player.hasCoin++;
                    gp.ui.addMessage("You got a coin");
                    gp.obj[i] = null;
                    break;

                case "Flag":
                    int nextMap = gp.mapIndicator + 1;
                    if (gp.tileM.mapExists(nextMap)) {
                        gp.mapIndicator = nextMap;
                        System.out.println("Loading map " + nextMap);
                        gp.ui.addMessage("Loading map " + nextMap);
                    } else {
                        System.out.println("You win!");
                        gp.ui.addMessage("You win!");
                    }
                    break;
            }
        }//function, which is enabling the collision and interaction with the different objects
    }

    public void addItemToInventory(SuperObject item) {
        int itemIndex = searchItemInInventory(item.name);
        if(itemIndex != 999 && item.stackable) {
            inventory.get(itemIndex).amount++;
        }
        else {
            inventory.add(item);
        }
    }

    public void removeItemFromInventory(String itemName) {
        int itemIndex = searchItemInInventory(itemName);
        if(itemIndex != 999) {
            inventory.get(itemIndex).amount--;
            if(inventory.get(itemIndex).amount <= 0) {
                inventory.remove(itemIndex);
            }
        }
    }

    public int searchItemInInventory(String itemName) {
        int itemIndex = 999;

        for(int i = 0; i < inventory.size(); i++) {
            if(inventory.get(i).name.equals(itemName)) {
                itemIndex = i;
                break;
            }
        }
        return itemIndex;
    }

    public void interactWithNPC(int npcIndex) {
        if (npcIndex == 999) return;
        if (!keyH.enterPressed) return;
        if (gp.npc[npcIndex] == null) return;

        Entity npc = gp.npc[npcIndex];
        keyH.enterPressed = false;

        if (gp.gameState != gp.dialogueState) {
            gp.gameState = gp.dialogueState;
            gp.ui.activeNPCIndex = npcIndex;
            npc.dialogueIndex = 0;
        }

        String line = npc.dialogues[npc.dialogueIndex];
        if (line != null && !line.isEmpty()) {
            gp.ui.currentDialogue = line;
            npc.dialogueIndex++;
            if (npc.dialogueIndex >= npc.dialogues.length
                    || npc.dialogues[npc.dialogueIndex] == null
                    || npc.dialogues[npc.dialogueIndex].isEmpty()) {
                npc.dialogueIndex = 0;
            }
        }
    }

    public void activateSpeedBoost() {
        speed = normalSpeed + 2;
        speedBoostActive = true;
        speedBoostCounter = 0;
    }

    public void activateJumpBoost() {
        jumpStrength = normalJumpStrength + 10;
        jumpStrengthBoostActive = true;
        jumpStrengthBoostCounter = 0;
    }

    public void attack() {
        Rectangle attackBox = new Rectangle();
        attacking = true;
        walkingCounter = 0;

        // Melee attacks only extend horizontally because the player faces left/right in combat.
        switch (direction) {
            case 'L':
                attackBox.x = x + solidArea.x - attackWidth;
                attackBox.y = y + solidArea.y;
                break;
            case 'R':
                attackBox.x = x + solidArea.x + solidArea.width;
                attackBox.y = y + solidArea.y;
                break;
        }

        attackBox.width = attackWidth;
        attackBox.height = attackHeight;

        checkMonsterHit(attackBox);
    }

    public void checkMonsterHit(Rectangle attackBox) {
        // Build fresh monster rectangles from current positions to avoid stale collision data.
        for (Entity monster : gp.monster) {
            if (monster == null || monster.isDead) {
                continue;
            }

            Rectangle monsterBox = new Rectangle(
                    monster.x + monster.solidArea.x,
                    monster.y + monster.solidArea.y,
                    monster.solidArea.width,
                    monster.solidArea.height
            );

            if (attackBox.intersects(monsterBox)) {
                damageMonster(monster);
            }
        }
    }

    public void checkProjectileMonsterHit() {
        if (projectile == null || !projectile.alive) {
            return;
        }

        // Stop after the first hit so one fireball cannot damage multiple monsters.
        Rectangle projectileBox = projectile.getCollisionBox();

        for (Entity monster : gp.monster) {
            if (monster == null || monster.isDead) {
                continue;
            }

            Rectangle monsterBox = new Rectangle(
                    monster.x + monster.solidArea.x,
                    monster.y + monster.solidArea.y,
                    monster.solidArea.width,
                    monster.solidArea.height
            );

            if (projectileBox.intersects(monsterBox)) {
                monster.life -= projectile.damage;
                gp.ui.addMessage(projectile.damage + " damage");
                projectile.alive = false;

                if (monster.life <= 0) {
                    monster.checkDrop();
                    monster.isDead = true;
                    gp.ui.addMessage("You shot a " + monster.name);
                }

                return;
            }
        }
    }

    public void damageMonster(Entity monster) {
        if (!monster.invincible) {
            // Invincibility frames keep melee attacks from applying damage every update tick.
            monster.life--;
            monster.knockBack = true;
            monster.directionBeforeKnockBack = monster.direction;
            monster.direction = this.direction;
            gp.ui.addMessage("1 damage");
            monster.invincible = true;

            if (monster.life <= 0) {
                monster.checkDrop();
                monster.isDead = true;
                gp.ui.addMessage("You killed a " + monster.name);
            }
        }
    }

    public void damagePlayer() {
        if (invincible == false) {
            // Player invincibility prevents repeated damage while overlapping a hazard/enemy.
            life -= 1;
            gp.ui.addMessage("You got damaged: 1 damage");
            invincible = true;
        }
    }

    @Override
    public void draw(Graphics2D g2) {

        if (invincible == true) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        }

        if (image != null) {
            int screenX = x - gp.camera.x;
            int screenY = y - gp.camera.y;

            g2.drawImage(image, screenX, screenY, width, height, null);
        }//using the camera for the player
        if (attacking) {
            g2.setColor(Color.YELLOW);
            int swordX = (direction == 'R')
                    ? x - gp.camera.x + solidArea.x + solidArea.width
                    : x - gp.camera.x + solidArea.x - attackWidth;
            int swordY = y - gp.camera.y + solidArea.y;
            g2.fillRect(swordX, swordY, attackWidth, attackHeight);
        }

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}
