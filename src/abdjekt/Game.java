package abdjekt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.URL;
import java.net.URLConnection;

public class Game {

    private static int mode = 2;

    public static void printNews() {
        String nextLine;
        URL url = null;
        URLConnection urlConn = null;
        InputStreamReader inStream = null;
        BufferedReader news = null;

        try {
            url = new URL("http://kicneoj.webs.com/abdjekt/other/news.txt");
            urlConn = url.openConnection();
            inStream = new InputStreamReader(urlConn.getInputStream());
            news = new BufferedReader(inStream);

            nextLine = news.readLine();
            System.out.println("--------------NEWS--------------");
            if (nextLine != null) {
                System.out.println(nextLine);
            } else {
                System.out.println("There is no news to display.");
            }
            System.out.println("--------------------------------");
        } catch (IOException ioe) {
        }
    }

    public static String getClientVersion() {
        return ".9";
    }

    public static String getCurrentVersion() {
        URL url = null;
        URLConnection urlConn = null;
        InputStreamReader inStream = null;
        BufferedReader rversion = null;
        String version;
        try {
            url = new URL("http://kicneoj.webs.com/abdjekt/other/version.txt");
            urlConn = url.openConnection();
            inStream = new InputStreamReader(urlConn.getInputStream());
            rversion = new BufferedReader(inStream);
            version = rversion.readLine();

            if (version != null) {
                return version;
            } else {
                return getClientVersion();
            }
        } catch (IOException ioe) {
            return getClientVersion();
        }
    }

    public static void setFree() {
        mode = 1;
        Main.world.clear();
    }

    public static void setNonFree() {
        mode = 2;
        Main.world.clear();
    }

    public static int getMode() {
        return mode;
    }

    public static Item getItem(String name) {
        for (Item item : Main.items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    public static boolean exists(String name) {
        for (Item item : Main.items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public static boolean itemUpdate() {
        try {
            Main.items = (Item[]) (new ObjectInputStream(new URL("https://dl.dropbox.com/u/20248237/%23items.dat").openStream())).readObject();
            return true;
        } catch (ClassNotFoundException ex) {
        } catch (IOException ex) {
        }
        return false;
    }
}
