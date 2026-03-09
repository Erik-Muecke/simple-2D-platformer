import javax.swing.SwingUtilities;
import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Basic JFrame");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800,600);
            frame.setResizable(true); // Fenster ist skalierbar
            frame.setLocationRelativeTo(null); // Zentriert auf dem Bildschirm
            frame.setVisible(true); // zeigt das Fenster an

        });
    }
}
