package object;

public class OBJ_Coin extends SuperObject {

    public OBJ_Coin() {
        name = "Coin";
        collision = false;
        image = loadImage("/objects/coin.png");
    }
}
