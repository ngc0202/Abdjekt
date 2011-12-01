package abdjekt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class abdjektReader {

    public static void process(Item subject, Item object, String verb) {
        String curLine;
        URL url = null;
        URLConnection urlConn = null;
        InputStreamReader inStream = null;
        BufferedReader reader = null;
        String outputLine = null;
        int found;
        World world = Main.world;

        try {
            if (verb.equals("spawn")) {
                if (world.canSpawn(subject.getURLName())) {
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
                //}
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
                                        world.remove(new Item(reqs[1][i]));
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
            //start joe code
            String[][] outputArray = subject.getOutputs();
            found = -1;
            for (int i = 0; i < outputArray.length; i++) {
                if (verb.equals(outputArray[i][0]) && (object.getName().equals(outputArray[i][1]))) {
                    found = i;
                    break;
                }
            }
            for (int i = 2; i < 32; i++) {
                if (outputArray[found][i] != null) {
                    if (outputArray[found][i].equals("<object>")) {
                        System.out.print(subject.getName() + " ");
                    }
                    if (outputArray[found][i].equals("<object>.")) {
                        System.out.print(subject.getName() + ". ");
                    }
                } else {
                    break;
                }
            }
            //end joe code
            //start of action reader
            //TODO: use actions/outputs in the Item object
            reader.reset();
            found = false;
            while (true) {
                curLine = reader.readLine();
//                System.out.println(curLine);
//                System.out.println("If this is being read and this is not a valid interaction it means actions are always accesed");//
                if (curLine != null && !curLine.equals("</actions>")) {
//                    System.out.println(curLine+" is the current line and this is the first if");
                    if (curLine.equals("<actions>") && outputLine != null) {
//                        System.out.println(outputLine);
                        while (!outputLine.equals("</actions>")) {
                            outputLine = reader.readLine();
//                            System.out.println(outputLine);
                            Scanner linereader = new Scanner(outputLine);

                            for (int i = 0; i < 22; i++) {
                                try {
                                    outputArray[i] = linereader.next();

                                } catch (java.util.NoSuchElementException eff) {
                                    break;
                                }
                            }
                            if (outputArray[0].equals(verb) && outputArray[1].equals(object.getName())) {
                                found = true;
                                for (int i = 2; i < outputArray.length; i = i + 1) {
                                    if (outputArray[i] != null) {
                                        if (outputArray[i + 1] != null) {
                                            if (outputArray[i + 1].equals("<object>")) {
                                                outputArray[i + 1] = subject.getName();
                                            }
                                            if (outputArray[i].equals("spawn")) {
                                                world.add(new Item(Item.cleanWord(outputArray[i + 1])));
                                            }
                                            if (outputArray[i].equals("remove")) {
                                                world.remove(new Item(outputArray[i + 1]));
                                            }
                                        }
                                        if (outputArray[i].equals("clean")) {
                                            world.clear();
                                        }
                                    }
                                }
                            } else {
                                for (int i = 0; i < outputArray.length; i++) {
                                    outputArray[i] = null;
                                }
                                continue;
                            }
                        }
                    } //?
                } else {
                    break;
                }
            }
            //end of action reader
        } catch (MalformedURLException eo) {
//            System.out.println(eo);
        } catch (IOException et) {
//            System.out.println(et);
        }

        try {
            reader.close();
        } catch (IOException ett) {
        }
    }
}
