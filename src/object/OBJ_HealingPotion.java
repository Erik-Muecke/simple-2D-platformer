package object;

public class OBJ_HealingPotion extends SuperObject {

    public OBJ_HealingPotion() {
        name = "Healing Potion";
        collision = false;
        image = loadImage("/objects/healingpotion.png");
        stackable = true;
    }
}
