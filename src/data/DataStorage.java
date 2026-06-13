package data;

import entity.Entity;
import main.GamePanel;
import object.SuperObject;

import java.io.Serializable;

/**
 * stores the complete game state so the world can be restored exactly as it was
 */
public class DataStorage implements Serializable {

    // serialVersionUID is used by java serialization to check compatibility
    private static final long serialVersionUID = 1L;

    // player position in the world
    public int playerX;
    public int playerY;
    public int previousX;
    public int previousY;

    // current loaded map
    public int mapIndicator;

    // player stats
    public int life;
    public int mana;
    public int hasKey;
    public int hasSpKey;
    public int hasCoin;

    // speed boost data
    public boolean speedBoostActive;
    public int speedBoostCounter;
    public int speed;

    // jump boost data
    public boolean jumpBoostActive;
    public int jumpBoostCounter;
    public int jumpStrength;

    // invincibility timer data
    public boolean invincible;
    public int invincibleCounter;

    // inventory item names and amounts
    // both arrays use the same index
    // example:
    // inventoryNames[0] = "Potion"
    // inventoryAmounts[0] = 3
    public String[] inventoryNames;
    public int[] inventoryAmounts;

    // saved world objects
    // every object keeps:
    // - its name
    // - its array slot
    // - its x and y position
    public String[] objectNames;
    public int[] objectSlots;
    public int[] objectX;
    public int[] objectY;

    // saved monster data
    public String[] monsterNames;
    public int[] monsterSlots;
    public int[] monsterX;
    public int[] monsterY;
    public int[] monsterLife;
    public boolean[] monsterDead;

    // saved npc data
    public String[] npcNames;
    public int[] npcSlots;
    public int[] npcX;
    public int[] npcY;
    public int[] npcDialogueIndex;
    public String[][] npcDialogues;

    /**
     * copies all current game data from the gamepanel into this save object
     */
    public void saveData(GamePanel gp) {

        // save current player position
        playerX = gp.player.x;
        playerY = gp.player.y;
        previousX = gp.player.previousX;
        previousY = gp.player.previousY;

        // save current map
        mapIndicator = gp.mapIndicator;

        // save player stats
        life = gp.player.life;
        mana = gp.player.mana;
        hasKey = gp.player.hasKey;
        hasSpKey = gp.player.hasSpKey;
        hasCoin = gp.player.hasCoin;

        // save speed boost values
        speedBoostActive = gp.player.speedBoostActive;
        speedBoostCounter = gp.player.speedBoostCounter;
        speed = gp.player.speed;

        // save jump boost values
        jumpBoostActive = gp.player.jumpStrengthBoostActive;
        jumpBoostCounter = gp.player.jumpStrengthBoostCounter;
        jumpStrength = gp.player.jumpStrength;

        // save invincibility timer
        invincible = gp.player.invincible;
        invincibleCounter = gp.player.invincibleCounter;

        // create arrays with the exact inventory size
        inventoryNames = new String[gp.player.inventory.size()];
        inventoryAmounts = new int[gp.player.inventory.size()];

        // copy every inventory item into the save arrays
        for(int i = 0; i < gp.player.inventory.size(); i++) {

            // get the item from the inventory list
            SuperObject item = gp.player.inventory.get(i);

            // save item name
            inventoryNames[i] = item.name;

            // save amount
            inventoryAmounts[i] = item.amount;
        }

        // count how many objects currently exist
        int objectCount = 0;

        for(SuperObject object : gp.obj) {
            if(object != null) {
                objectCount++;
            }
        }

        // create arrays with the correct size
        objectNames = new String[objectCount];
        objectSlots = new int[objectCount];
        objectX = new int[objectCount];
        objectY = new int[objectCount];

        int objectIndex = 0;

        // loop through all object slots
        for(int i = 0; i < gp.obj.length; i++) {

            SuperObject object = gp.obj[i];

            // skip empty slots
            if(object != null) {

                // save object name
                objectNames[objectIndex] = object.name;

                // save the original slot
                // this makes sure the object returns to the same array index later
                objectSlots[objectIndex] = i;

                // save object position
                objectX[objectIndex] = object.x;
                objectY[objectIndex] = object.y;

                objectIndex++;
            }
        }

        // count monsters
        int monsterCount = 0;

        for(Entity monster : gp.monster) {
            if(monster != null) {
                monsterCount++;
            }
        }

        // create monster save arrays
        monsterNames = new String[monsterCount];
        monsterSlots = new int[monsterCount];
        monsterX = new int[monsterCount];
        monsterY = new int[monsterCount];
        monsterLife = new int[monsterCount];
        monsterDead = new boolean[monsterCount];

        int monsterIndex = 0;

        // save all monster data
        for(int i = 0; i < gp.monster.length; i++) {

            Entity monster = gp.monster[i];

            if(monster != null) {

                // save monster type/name
                monsterNames[monsterIndex] = monster.name;

                // save monster slot
                monsterSlots[monsterIndex] = i;

                // save position
                monsterX[monsterIndex] = monster.x;
                monsterY[monsterIndex] = monster.y;

                // save health and death state
                monsterLife[monsterIndex] = monster.life;
                monsterDead[monsterIndex] = monster.isDead;

                monsterIndex++;
            }
        }

        // count npcs
        int npcCount = 0;

        for(Entity npc : gp.npc) {
            if(npc != null) {
                npcCount++;
            }
        }

        // create npc save arrays
        npcNames = new String[npcCount];
        npcSlots = new int[npcCount];
        npcX = new int[npcCount];
        npcY = new int[npcCount];
        npcDialogueIndex = new int[npcCount];
        npcDialogues = new String[npcCount][];

        int npcIndex = 0;

        // save npc data
        for(int i = 0; i < gp.npc.length; i++) {

            Entity npc = gp.npc[i];

            if(npc != null) {

                // save npc name
                npcNames[npcIndex] = npc.name;

                // save original npc slot
                npcSlots[npcIndex] = i;

                // save npc position
                npcX[npcIndex] = npc.x;
                npcY[npcIndex] = npc.y;

                // save which dialogue line the npc currently uses
                npcDialogueIndex[npcIndex] = npc.dialogueIndex;

                // clone creates a copy of the dialogue array
                // without clone, both arrays would point to the same memory
                npcDialogues[npcIndex] = npc.dialogues.clone();

                npcIndex++;
            }
        }
    }

    /**
     * restores all saved data back into the game
     */
    public void loadData(GamePanel gp) {

        // load map first
        // otherwise objects and monsters would load into the wrong level
        gp.mapIndicator = mapIndicator;

        // rebuild the world for the saved map
        gp.loadMapForSave();

        gp.gameOver = false;

        // restore player position
        gp.player.x = playerX;
        gp.player.y = playerY;
        if(previousX == 0 && previousY == 0) {
            gp.player.setPreviousSafePosition();
        } else {
            gp.player.previousX = previousX;
            gp.player.previousY = previousY;
        }

        // restore player stats
        gp.player.life = life;
        gp.player.mana = mana;
        gp.player.hasKey = hasKey;
        gp.player.hasSpKey = hasSpKey;
        gp.player.hasCoin = hasCoin;

        // restore speed boost values
        gp.player.speedBoostActive = speedBoostActive;
        gp.player.speedBoostCounter = speedBoostCounter;
        gp.player.speed = speed;

        // restore jump boost values
        gp.player.jumpStrengthBoostActive = jumpBoostActive;
        gp.player.jumpStrengthBoostCounter = jumpBoostCounter;
        gp.player.jumpStrength = jumpStrength;

        // restore invincibility
        gp.player.invincible = invincible;
        gp.player.invincibleCounter = invincibleCounter;

        // remove current inventory before loading saved inventory
        gp.player.inventory.clear();

        // rebuild inventory from saved arrays
        for(int i = 0; i < inventoryNames.length; i++) {

            // create a new object using the saved name
            SuperObject item = gp.entityHandler.getObject(inventoryNames[i]);

            if(item != null) {

                // restore amount
                item.amount = inventoryAmounts[i];

                // add item back into inventory
                gp.player.inventory.add(item);
            }
        }

        // completely replace the current object array
        gp.obj = new SuperObject[gp.obj.length];

        // recreate all saved objects
        for(int i = 0; i < objectNames.length; i++) {

            // create object from saved name
            SuperObject object = gp.entityHandler.getObject(objectNames[i]);

            if(object != null) {

                // restore object position
                object.x = objectX[i];
                object.y = objectY[i];

                // get original slot
                int slot = getSavedSlot(objectSlots, i, gp.obj.length);

                // place object back into the array
                gp.obj[slot] = object;
            }
        }

        // replace monster array
        gp.monster = new Entity[gp.monster.length];

        // recreate monsters
        for(int i = 0; i < monsterNames.length; i++) {

            // create monster from saved name
            Entity monster = gp.entityHandler.getMonster(monsterNames[i]);

            if(monster != null) {

                // restore position
                monster.x = monsterX[i];
                monster.y = monsterY[i];

                // restore health
                monster.life = monsterLife[i];

                // restore death state
                monster.isDead = monsterDead[i];

                int slot = getSavedSlot(monsterSlots, i, gp.monster.length);

                gp.monster[slot] = monster;
            }
        }

        // save reference to current npc array
        // this allows reuse of already existing npc instances
        Entity[] defaultNPCs = gp.npc;

        // create new npc array
        gp.npc = new Entity[gp.npc.length];

        // restore npcs
        for(int i = 0; i < npcNames.length; i++) {

            int slot = getSavedSlot(npcSlots, i, gp.npc.length);

            // try to reuse an already existing npc
            Entity npc = getDefaultNPC(defaultNPCs, slot, npcNames[i], npcX[i], npcY[i]);

            // if no existing npc matches, create a new one
            if(npc == null) {
                npc = gp.entityHandler.getNPC(npcNames[i]);
            }

            if(npc != null) {

                // restore position
                npc.x = npcX[i];
                npc.y = npcY[i];

                // restore current dialogue line
                npc.dialogueIndex = npcDialogueIndex[i];

                // restore dialogue text
                if(npcDialogues != null && i < npcDialogues.length && npcDialogues[i] != null) {
                    npc.dialogues = npcDialogues[i].clone();
                }

                gp.npc[slot] = npc;
            }
        }

        // move camera to player position after loading
        gp.camera.update(gp.player);

        // reset active dialogue
        gp.ui.activeNPCIndex = 999;
    }

    private int getSavedSlot(int[] slots, int index, int maxLength) {

        // checks if the saved slot exists and is valid
        if(slots != null && index < slots.length && slots[index] >= 0 && slots[index] < maxLength) {
            return slots[index];
        }

        // fallback if slot data is invalid
        return index;
    }

    private Entity getDefaultNPC(Entity[] defaultNPCs, int slot, String name, int x, int y) {

        // no npc array exists
        if(defaultNPCs == null) {
            return null;
        }

        // first check the saved slot directly
        if(slot >= 0 && slot < defaultNPCs.length && matchesNPC(defaultNPCs[slot], name)) {
            return defaultNPCs[slot];
        }

        // otherwise search through all npcs
        for(Entity npc : defaultNPCs) {

            // same npc name and same position
            if(matchesNPC(npc, name) && npc.x == x && npc.y == y) {
                return npc;
            }
        }

        return null;
    }

    private boolean matchesNPC(Entity npc, String name) {

        // checks if the npc exists and has the same name
        return npc != null && npc.name != null && npc.name.equals(name);
    }
}
