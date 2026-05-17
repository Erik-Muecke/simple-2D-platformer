package object;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Flag extends SuperObject {

    public OBJ_Flag() {
        name = "Flag";
        collision = false;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/flag.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}