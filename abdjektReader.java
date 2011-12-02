package abdjekt;

public class abdjektReader {

    public static void process(Item subject, Item object, String verb) {
        int found;
        World world = Main.world;

        if (verb.equals("spawn")) {
            if (world.canSpawn(subject.getURLName()))  {
                    if (subject.canSpawn()) {
                        world.add(subject);
                        if (!Main.world.checkMax(1) && (subject.getSpawnText() == null || subject.getSpawnText().equals(""))) {
                            System.out.println("You spawn " + subject.getArticle() + subject.getName() + ".");
                        } else {
                            System.out.println(subject.getSpawnText());
                        }
                    } else {
                        System.out.println("You are unable to spawn " + subject.getArticle() + subject.getName() + ".");
                    }
            } else {
                System.out.println("What is a " + subject.getName() + "?");
            }
            return;
        }
        if (verb.equals("remove")) {
            if (world.canSpawn(subject.getURLName())) {
                if (world.remove(subject)) {
                    if (subject.getRemoveText().equals("")) {
                        System.out.println("You remove " + subject.getArticle() + subject.getName() + ".");
                    } else {
                        System.out.println(subject.getRemoveText());
                    }

                } else {
                    System.out.println("There is no " + subject.getName() + " here.");
                }
            } else {
                System.out.println("There is no " + subject.getName() + " here.");
            }
            return;
        }
        if (verb.equals("look")) {
            world.show();
            return;
        }
        if (verb.equals("make")) {
            if (world.canMake(subject.getURLName())) {
                if (subject.canMake()) {
                    if (world.hasReqs(subject.getMakeReqs())) {
                        world.add(subject);
                        String[][] reqs = subject.getMakeReqs();
                        for (int i = 0; i < reqs[0].length; i++) {
                            if (reqs[0][i] != null && reqs[1][i] != null) {
                                for (int k = 0; k < Integer.parseInt(reqs[0][i]); k++) {
                                    world.remove(Game.newItem(reqs[1][i]));
                                }
                            }
                        }
                        System.out.println("You successfully create " + subject.getArticle() + subject.getName() + ".");
                    } else {
                        System.out.print("To create " + subject.getArticle() + subject.getName() + ", you need ");
                        String[][] reqs = subject.getMakeReqs();
                        for (int i = 0; i < reqs[1].length - 1; i++) {
                            if (reqs[0][i] != null && reqs[1][i] != null) {
                                System.out.print(reqs[0][i] + " ");
                                if (reqs[0][i + 1] != null && reqs[1][i + 1] != null) {
                                    System.out.print(reqs[1][i] + ", ");
                                } else {
                                    System.out.println(reqs[1][i] + ".");
                                }
                            }
                        }
                    }
                } else {
                    System.out.println("You cannot craft " + subject.getArticle() + subject.getName() + ".");
                }
            } else {
                System.out.println("What is a " + subject.getName() + "?");
            }
            return;
        }
        if (!world.isSpawned(subject)) {
            System.out.println("There is no " + subject.getName() + " here.");
            return;
        }
        if (!world.isSpawned(object)) {
            System.out.println("There is no " + object.getName() + " here.");
            return;
        }
        if (subject.equals(object)) {
            if (world.getCount(object) <= 1) {
                System.out.println("You do not have two " + subject.getName() + "s.");
                return;
            }
        }
        String[][] outputArray = subject.getOutputs();
        found = -1;
        for (int i = 0; i < outputArray.length; i++) {
            if (verb.equals(outputArray[i][0]) && (object.getName().equals(outputArray[i][1]))) {
                found = i;
                break;
            }
        }

        if (found == -1){
            System.out.println("You can't do that.");
            return;
        }
        for (int i = 2; i < outputArray[found].length; i++) {
            if (outputArray[found][i] != null) {
                if (outputArray[found][i].equals("<object>")) {
                    System.out.print(subject.getName() + " ");
                } else if (outputArray[found][i].equals("<object>.")) {
                    System.out.print(subject.getName() + ". ");
                } else {
                    if (i == outputArray[found].length - 1) {
                        System.out.print(outputArray[found][i]);
                        if (outputArray[found][i].endsWith(".")) {
                            System.out.println();
                        } else {
                            System.out.println(".");
                        }
                    } else {
                        System.out.print(outputArray[found][i] + " ");
                    }
                }
            } else {
                break;
            }
        }

        found = -1;
        String[][] actionArray = subject.getActions();
        for (int i = 0; i < actionArray.length; i++) {
            if (verb.equals(actionArray[i][0]) && (object.getName().equals(actionArray[i][1]))) {
                found = i;
                break;
            }
        }
        for (int i = 2; i < actionArray[found].length; i++) {
            if (actionArray[found][i].equals("spawn")) {
                world.add(Game.newItem(Item.cleanWord(actionArray[found][i + 1])));
            }
            if (actionArray[found][i].equals("remove")) {
                world.remove(Game.newItem(actionArray[found][i + 1]));
            }
            if (actionArray[found][i].equals("clean")) {
                world.clear();
            }
        }
    }
}
