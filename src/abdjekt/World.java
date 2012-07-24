package abdjekt;

import java.util.ArrayList;

public class World {

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
        for (int i = 0; i < world.size(); i++) {
            if (object.getName().equals(world.get(i).getName())) {
                world.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean remove(String object) {
        for (int i = 0; i < world.size(); i++) {
            if (world.get(i).getName().equals(object)) {
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
                if (getCount(Game.getItem(reqs[1][i])) != Integer.parseInt(reqs[0][i])) {
                    check = false;
                }
            }
        }
        return check;
    }

    public final int getLength() {
        return world.size();
    }

    public boolean canSpawn(Item item) {
        if (item.getLimit() > this.getCount(item)) {
            return false;
        }
        if (!item.canSpawnNotFree()) {
            return false;
        }
        return true;
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
