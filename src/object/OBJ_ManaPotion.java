package object;

public class OBJ_ManaPotion extends SuperObject {

    public OBJ_ManaPotion() {
        name = "Mana Potion";
        collision = false;
        image = loadImage("/objects/manapotion.png");
        stackable = true;
        amount = 3;
        price = 3;
    }
}
