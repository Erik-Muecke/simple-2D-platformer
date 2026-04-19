package src;
import java.awt.*;

public class Block{
        int x;
        int y;
        int width;
        int height;      
        Image image;
        int startX;
        int startY;
        int velocityX = 0;
        int velocityY = 0;
        
        Block (Image image, int x, int y, int width, int height){
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;
        }

        void reset(){
            this.x = this.startX;
            this.y = this.startY;
        }
}

//TODO: Block.java ist so ziemlich dasselbe wie Entity.java. Wird diese überhaupt benötigt,
// da sich so etwqas ja auch über die kommende Tilemap Regeln lässt. Wenn es nicht Weiter benötigt wird: TBD
