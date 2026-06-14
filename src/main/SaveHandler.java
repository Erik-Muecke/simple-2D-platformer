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

        createSaveFile(); // Erstellt die Datei, wenn sie nicht existiert
    }

    public void createSaveFile() {
        if (!saveFile.exists()) { //Ueberpruefen ob die Datei existiert, wenn nicht wird sie erstellt
            Properties save = new Properties();
            save.setProperty("level", "0");

            try (FileWriter writer = new FileWriter(saveFile)) { // Datei wird mit einem FileWriter erstellt und die Properties werden gespeichert
                save.store(writer, "Game Save");
            } catch (Exception e) {
                System.out.println("Fehler beim Schreiben der Datei, nach Erstellung" + e.getMessage()); // Fehlerbehandlung, falls die Datei nicht erstellt oder beschrieben werden kann
            }
        }
    }

    //Getter zum laden des levels
    public int loadLevel() {

        try (FileReader reader = new FileReader(saveFile)) {
            save.load(reader);
            return Integer.parseInt(save.getProperty("level")); // Rückgabe des gespeicherten Levels als Integer, wenn die Datei erfolgreich gelesen und die Property gefunden wurde
        } catch (IOException e) {
            System.out.println("Fehler beim Einlesen der Datei" + e.getMessage()); // Fehlerbehandlung, falls die Datei nicht gelesen werden kann oder die Property nicht gefunden wird, Rückgabe von 1 als Standardlevel
        }

        return 1;
    }

    //Getter zum laden der Leben
    public int loadLives() {
        try (FileReader reader = new FileReader(saveFile)) {
            save.load(reader);
            return Integer.parseInt(save.getProperty("lives"));
        } catch (IOException e) {
            System.out.println("Fehler beim Einlesen der Datei" + e.getMessage());
        }

        return 6;
    }

    //Setter zum ändern der Leben
    public void savelives(int lives) {
        save.setProperty("lives", String.valueOf(lives)); // Setzen der "lives" Property auf den übergebenen Wert, konvertiert zu String

        try (FileWriter writer = new FileWriter(saveFile)) {
            save.store(writer, "Game Save"); // Speichern der Properties in der Datei, überschreibt die vorherigen Werte
        } catch (IOException e) {
            System.out.println("Fehler beim Schreiben der Datei, zum Leben Speichern" + e.getMessage()); // Fehlerbehandlung, falls die Datei nicht beschrieben werden kann
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