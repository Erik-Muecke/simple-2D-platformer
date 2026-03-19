package GamePanel;

import javax.swing.*;

public class GamePanel extends JPanel {
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; // 48x48 tile
    final int MaxScreenCol = 16;
    final int MaxScreenRow = 12;
    final int screenWidth = tileSize * MaxScreenCol; // 768 pixels
    final int screenHeight = tileSize * MaxScreenRow; // 576 pixels

    public GamePanel() {

        System.out.println("GamePanel created");
    }
}