package abdjektcompiler;

import abdjekt.Item;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        final Logger log = Logger.getLogger("AbdjektCompiler");
        try {
            Scanner input = new Scanner(System.in);
            System.out.print("Absolute path to folder: ");
            File folder = new File(input.findWithinHorizon("([a-zA-Z]:)?(\\\\[a-zA-Z0-9_-]+)+\\\\?", 0));
            File[] files = folder.listFiles(new AbdjektFileFilter());
            if (files == null) {
                log.severe("Invalid folder.");
                return;
            }
            Item[] items = new Item[files.length];
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                items[i] = new Item(file);
            }
            File itemFile = new File(folder, "#items.dat");
            itemFile.createNewFile();
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(itemFile));
            output.writeObject(items);
            System.out.println("Successfully compiled " + items.length + " items to " + itemFile.getAbsolutePath());
        } catch (Exception ex) {
            log.log(Level.SEVERE, null, ex);
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
