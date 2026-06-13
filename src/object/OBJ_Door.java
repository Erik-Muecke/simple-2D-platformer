package object;

public class OBJ_Door extends SuperObject {

    public OBJ_Door() {
        name = "Door";
        collision = true;
        image = loadImage("/objects/door.png");
    }
}
