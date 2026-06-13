package data;

import main.GamePanel;

import java.io.*;

public class SaveLoad {

    // reference to the main gamepanel
    // needed so save/load can access the current game state
    GamePanel gp;

    public SaveLoad(GamePanel gp) {
        this.gp = gp;
    }

    public void save() {

        // try-with-resources automatically closes the stream after saving
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream("save.dat"))) {

            // create a new save container object
            DataStorage ds = new DataStorage();

            // copy all current game data into the save object
            ds.saveData(gp);

            // convert the whole DataStorage object into bytes
            // and write it into save.dat
            oos.writeObject(ds);

            System.out.println("Game Saved");

        } catch (Exception e) {

            // prints detailed error information if saving fails
            e.printStackTrace();
        }
    }

    public void load() {

        // opens the save file and reads serialized objects from it
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream("save.dat"))) {

            // readObject() loads the saved bytes from the file
            // and converts them back into a java object
            DataStorage ds = (DataStorage) ois.readObject();

            // restore all saved values into the current game
            ds.loadData(gp);

            System.out.println("Game Loaded");

        } catch (Exception e) {

            // prints detailed error information if loading fails
            e.printStackTrace();
        }
    }
}