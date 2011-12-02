package abdjekt;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class World {

//    private Item[] world;
    private ArrayList<Item> world;
    private int limit;

    public World(int ilimit) {
        world = new ArrayList<Item>();
        limit = ilimit;
    }

    public boolean checkMax(int added) {
        if (world.size() + added > limit) {
            return true;
        }
        return false;
    }
    /*
    public int length(){
    if(world.isEmpty()){
    return 0;
    } else {
    return world.size();
    }
    }
     */

    public String show() {
        String nouns = "";
        Item object = null;
        for (int i = 0; i < world.size(); i++) {
            object = world.get(i);
            if (i == 0 && world.get(i) != null) {
                String article = world.get(i).getArticle();
                nouns = nouns + article + world.get(i).getName();
            }
            if (i > 0 && world.get(i) != null) {
                if (i + 1 < world.size()) {
                    nouns = nouns + ", " + object.getArticle() + object.getName();
                } else {
                    nouns = nouns + " and " + object.getArticle() + object.getName();
                }

            }
        }
        return nouns;
    }

    public void add(Item object) {
        world.add(object);
        if (checkMax(1)) {
            System.out.println("The world is full, please remove or destroy an object.");
        }
    }

    public boolean remove(Item object) {
        for(int i=0;i<world.size();i++){
            if(object.getName().equals(world.get(i).getName())){
                world.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean contains(Item object) {
        boolean check = false;
        for (int i = 0; i < world.size(); i++) {
            Item current = world.get(i);
            if (current != null) {
                if (current.getName().equals(object.getName())) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }

    public int getCount(Item object) {
        int count = 0;
        for (int i = 0; i < world.size(); i++) {
            if (world.get(i) != null) {
                if (world.get(i).getName().equals(object.getName())) {
                    count++;
                }
            }
        }
        return count;
    }

    public Item getAt(int index) {
        return world.get(index);
    }

    public void clear() {
        world.clear();
    }

    public boolean hasReqs(String[][] reqs) {
        boolean check = true;
        for (int i = 0; i < reqs[0].length; i++) {
            if (reqs[0][i] != null && reqs[1][i] != null) {
                if (getCount(Game.newItem(reqs[1][i])) != Integer.parseInt(reqs[0][i])) {
                    check = false;
                }
            }
        }
        return check;
    }

    public boolean canMake(String iobject) {
        boolean check = false;
        if (canSpawn(iobject)) {
            Item object = Game.newItem(iobject);
            Scanner rfile = null;
            try {
                rfile = new Scanner(object.getItemFile());
            } catch (FileNotFoundException ex) {
            }
            String curline = "foo";
            while (curline != null && !curline.equals("</flags>")) {
                curline = rfile.nextLine();
                if (curline.startsWith("make")) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }

    public final int getLength() {
        return world.size();
    }

    public boolean canSpawn(String name) {
        for (int i = 0; i < world.size(); i++) {
            if (world.get(i).getName().equals(name)) {
                return true;
            }
        }
            if (Item.exists(name)) {
                return true;
            }
        return false;
    }

    public boolean isSpawned(Item item) {
        for (int i = 0; i < world.size(); i++) {
            if (world.get(i).getName().equals(item.getName())) {
                return true;
            }
        }
        return false;
    }
}
