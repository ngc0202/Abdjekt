package abdjekt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

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

    public static BufferedReader getObjectReader(String object) {
        try {
            URL url = new URL("http://kicneoj.webs.com/abdjekt/abdjekts/" + object + ".abj");
            URLConnection urlConn = url.openConnection();
            InputStreamReader inStream = new InputStreamReader(urlConn.getInputStream());
            BufferedReader file = new BufferedReader(inStream);
            return file;
        } catch (MalformedURLException ex) {
        } catch (IOException err) {
        }
        return null;
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

    public static Item newItem(String name) {
        ArrayList<Item> spawned = Main.items;
        Item item = spawned.get(0);
        boolean bspawned = false;
        if (!Item.exists(name)) {
            return item;
        }
        for (int i = 0; i < spawned.size(); i++) {
            if (spawned.get(i).getName().equalsIgnoreCase(name)) {
                item = spawned.get(i);
                bspawned = true;
                break;
            }
        }
        if (!bspawned) {
            item = new Item(name);
            Main.items.add(item);
        }
        return item;
    }

    public static void itemUpdate() {
        try {
            File list = new File(System.getenv("APPDATA") + "\\abdjekt\\list.txt");
            InputStream inputStream = new URL("http://dl.dropbox.com/u/42082987/abdjekts/%23list.txt").openStream();
            OutputStream out = new FileOutputStream(list);
            byte buf[] = new byte[1024];
            int len;

            while ((len = inputStream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            inputStream.close();

            Scanner rlist = new Scanner(list);
            int llen = 0;
            while (rlist.hasNext()) {
                rlist.nextLine();
                llen++;
            }
            rlist.close();
            rlist = new Scanner(list);
            System.out.println("Updating file database:");
            for (int i = 0; i < llen; i++) {
                String curl = rlist.nextLine();
                Main.items.add(new Item(curl.substring(0, curl.indexOf("."))));
                int percent = (int) Math.round(((i + 1.0) / llen) * 100);
                System.out.println(percent + "%");
            }
            rlist.close();
            list.delete();
        } catch (IOException e) {
            return;
        }
    }
}
