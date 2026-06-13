package main;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;


public class SaveHandler {
    GamePanel gp;
    File saveFile = new File("savefile.properties");
    Properties save = new Properties();

    public SaveHandler(GamePanel gp) {
        this.gp = gp;

        createSaveFile();
    }

    public void createSaveFile() {
        if (!saveFile.exists()) {
            Properties save = new Properties();
            save.setProperty("level", "0");

            try (FileWriter writer = new FileWriter(saveFile)) {
                save.store(writer, "Game Save");
            } catch (Exception e) {
                System.out.println("Fehler beim Schreiben der Datei, nach Erstellung" + e.getMessage());
            }
        }
    }

    //Getter zum laden des levels
    public int loadLevel() {

        try (FileReader reader = new FileReader(saveFile)) {
            save.load(reader);
            return Integer.parseInt(save.getProperty("level"));
        } catch (IOException e) {
            System.out.println("Fehler beim Einlesen der Datei" + e.getMessage());
        }

        return 1;
    }

    public int loadLives() {

        try (FileReader reader = new FileReader(saveFile)) {
            save.load(reader);
            return Integer.parseInt(save.getProperty("lives"));
        } catch (IOException e) {
            System.out.println("Fehler beim Einlesen der Datei" + e.getMessage());
        }

        return 6;
    }

    public void savelives(int lives) {

        save.setProperty("lives", String.valueOf(lives));

        try (FileWriter writer = new FileWriter(saveFile)) {
            save.store(writer, "Game Save");
        } catch (IOException e) {
            System.out.println("Fehler beim Schreiben der Datei, zum Leben Speichern" + e.getMessage());
        }
    }

    //Setter zum ändern des levels
    public void saveLevel(int level) {

        save.setProperty("level", String.valueOf(level));

        try (FileWriter writer = new FileWriter(saveFile)) {
            save.store(writer, "Game Save");
        } catch (IOException e) {
            System.out.println("Fehler beim Schreiben der Datei, zum Level Speichern" + e.getMessage());
        }
    }
}