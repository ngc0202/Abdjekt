package abdjekt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Item implements Serializable {

    private String name;
    private String urlname;
    private String spawnText;
    private String removeText;
    private String masterAlias;
    private boolean canSpawnNotFreely;
    private boolean canMake;
    private int limit;
    private String article;
    private String[][] actions;
    private String[][] outputs;
    private String[][] makeReqs;

    public Item(File file) {
        name = file.getName().substring(0, file.getName().length() - 4);
        urlname = cleanWord(name);
        masterAlias = findMaster(file);
        if (!masterAlias.equals(name)) {
            urlname = cleanWord(masterAlias);
        }
        spawnText = findSpawnText(file);
        removeText = findRemoveText(file);
        canSpawnNotFreely = findCanSpawnNotFree(file);
        article = findArticle(file);
        actions = findActions(file);
        outputs = findOutputs(file);
        canMake = findCanMake(file);
        makeReqs = findMakeReqs(file);
        limit = findLimit(file);

    }

    public final String getName() {
        return name;
    }

    public final String getURLName() {
        return urlname;
    }

    public final String getSpawnText() {
        return spawnText;
    }

    public final String getRemoveText() {
        return removeText;
    }

    public final boolean canSpawnNotFree() {
        return canSpawnNotFreely;
    }

    public final String getArticle() {
        return article;
    }

    public final String[][] getActions() {
        return actions;
    }

    public final String[][] getOutputs() {
        return outputs;
    }

    public final boolean canMake() {
        return canMake;
    }

    public final String[][] getMakeReqs() {
        return makeReqs;
    }

    public final int getLimit() {
        return limit;
    }

    private String findSpawnText(File itemfile) {
        String curLine = "foo";
        String text = "";
        Scanner rfile = null;
        try {
            rfile = new Scanner(itemfile);
        } catch (FileNotFoundException ex) {
        }
        while (curLine != null && !curLine.equals("</flags>") && rfile.hasNext()) {
            curLine = rfile.nextLine();
            if (curLine != null) {
                if (curLine.startsWith("spawntext")) {
                    Scanner line = new Scanner(curLine);
                    line.next();
                    text += line.nextLine();
                    return text;
                }
            } else {
                break;
            }
        }
        return "";
    }

    private String findRemoveText(File itemfile) {
        String curLine = "foo";
        String parameters = "";
        Scanner rfile = null;
        try {
            rfile = new Scanner(itemfile);
        } catch (FileNotFoundException ex) {
        }

        while (curLine != null && !curLine.equals("</flags>") && rfile.hasNext()) {
            curLine = rfile.nextLine();
            if (curLine != null) {
                if (curLine.startsWith("removetext")) {
                    Scanner line = new Scanner(curLine);
                    line.next();
                    parameters += line.nextLine();
                    return parameters;
                }
            } else {
                break;
            }
        }
        return "";
    }

    private boolean findCanSpawnNotFree(File itemfile) {
        Scanner rfile = null;
        try {
            rfile = new Scanner(itemfile);
        } catch (FileNotFoundException ex) {
        }
        String curLine = "foo";
        boolean check = true;
        while (curLine != null && !curLine.equals("</flags>") && rfile.hasNext()) {
            if (curLine.startsWith("nospawn")) {
                check = false;
            }
            curLine = rfile.nextLine();
        }
        return check;
    }

    private String findArticle(File itemfile) {
        Scanner rfile = null;
        try {
            rfile = new Scanner(itemfile);
        } catch (FileNotFoundException ex) {
        }
        String curLine = "foo";
        String[] parameters = new String[1];

        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = "foo";
        }

        while (curLine != null && !curLine.equals("</flags>") && rfile.hasNext()) {
            curLine = rfile.nextLine();
            if (curLine.startsWith("article")) {
                Scanner line = new Scanner(curLine);
                line.next();
                for (int i = 0; i < parameters.length; i++) {
                    try {
                        parameters[i] = line.next();
                    } catch (NoSuchElementException err) {
                        parameters[i] = "";
                    }
                }
                if (!parameters[0].equals("")) {
                    return parameters[0] + " ";
                } else {
                    return "";
                }
            }
        }
        return "a ";
    }

    private String[][] findActions(File itemfile) {
        String curLine = "foo";
        String[] dactions = new String[20];
        int count = 0;
        Scanner rfile = null;
        try {
            rfile = new Scanner(itemfile);
        } catch (FileNotFoundException ex) {
        }
        while (curLine != null && !curLine.equals("</abdjekt>") && rfile.hasNext()) {
            curLine = rfile.nextLine();
            if (curLine.equals("<actions>")) {
                while (curLine != null && !curLine.equals("</actions>")) {
                    curLine = rfile.nextLine();
                    if (!curLine.equals("</actions>")) {
                        dactions[count] = curLine;
                        count++;
                    }
                }
            }
        }
        String ractions[] = new String[count];
        System.arraycopy(dactions, 0, ractions, 0, count);
        String[][] sractions = new String[count][32];
        for (int i = 0; i < count; i++) {
            sractions[i] = ractions[i].split(" ");
        }
        return sractions;
    }

    private String[][] findOutputs(File itemfile) {
        String curLine = "foo";
        String[] doutputs = new String[20];
        int count = 0;
        Scanner rfile = null;
        try {
            rfile = new Scanner(itemfile);
        } catch (FileNotFoundException ex) {
        }
        while (curLine != null && !curLine.equals("</abdjekt>") && rfile.hasNext()) {
            curLine = rfile.nextLine();
            if (curLine.equals("<outputs>")) {
                while (curLine != null && !curLine.equals("</outputs>")) {
                    curLine = rfile.nextLine();
                    if (!curLine.equals("</outputs>")) {
                        doutputs[count] = curLine;
                        count++;
                    }
                }
            }
        }
        String routputs[] = new String[count];
        System.arraycopy(doutputs, 0, routputs, 0, count);
        String[][] sroutputs = new String[count][32];
        for (int i = 0; i < count; i++) {
            sroutputs[i] = routputs[i].split(" ");
        }
        return sroutputs;
    }

    private String[][] findMakeReqs(File itemfile) {
        String[][] reqs = new String[2][5];
        Scanner abjread = null;
        try {
            abjread = new Scanner(itemfile);
        } catch (FileNotFoundException ex) {
        }
        String curLine = "foo";
        String reqsline = null;
        while (curLine != null && !curLine.equals("</flags>")) {
            if (curLine.startsWith("make")) {
                reqsline = curLine;
                break;
            }
            curLine = abjread.nextLine();
        }
        if (reqsline == null) {
            return reqs;
        }
        Scanner lr = new Scanner(reqsline);
        String[] plainreqs = new String[reqs.length * 2];
        lr.next();
        for (int i = 1; i < plainreqs.length + 1; i++) {
            plainreqs[i - 1] = lr.next();
        }

        for (int i = 0; i < plainreqs.length / 2; i++) {
            if (plainreqs[2 * i] != null) {
                reqs[0][i] = plainreqs[2 * i]; //reqs[0][x] is nums
                reqs[1][i] = plainreqs[2 * i + 1]; //reqs[1][x] is objects
            }
        }
        return reqs;
    }

    private boolean findCanMake(File itemfile) {
        boolean check = false;
        Scanner rfile = null;
        try {
            rfile = new Scanner(itemfile);
        } catch (FileNotFoundException ex) {
        }
        String curline = "foo";
        while (curline != null && !curline.equals("</flags>") && rfile.hasNext()) {
            curline = rfile.nextLine();
            if (curline.startsWith("make")) {
                check = true;
                break;
            }
        }
        return check;
    }

    private int findLimit(File itemfile) {
        try {
            Scanner file = new Scanner(itemfile);
            String[] acur;
            String cur = "";
            while (file.hasNext() && cur.equals("</flags>")) {
                cur = file.nextLine();
                acur = cur.split(" ");
                if (acur[0].equalsIgnoreCase("limit")) {
                    return Integer.parseInt(acur[1]);
                }
            }
        } catch (FileNotFoundException ex) {
        }
        return -1;
    }

    private String findMaster(File itemfile) {
        String curLine = "foo";
        String text = "";
        Scanner rfile = null;
        try {
            rfile = new Scanner(itemfile);
        } catch (FileNotFoundException ex) {
        }
        while (curLine != null && !curLine.equals("</aliases>") && rfile.hasNext()) {
            curLine = rfile.nextLine();
            if (curLine != null) {
                if (curLine.startsWith("<aliases>")) {
                    curLine = rfile.nextLine();
                    if (curLine.equals("</aliases>")) {
                        return name;
                    } else {
                        return curLine;
                    }
                }
            } else {
                break;
            }
        }
        return "";
    }

    public static String cleanWord(String word) {
        String cleanword = "";
        if (word != null) {
            if (word.contains(" ")) {
                char[] subArray = word.toCharArray();
                cleanword = "";
                for (int i = 0; i < subArray.length; i++) {
                    if (subArray[i] != ' ') {
                        cleanword += subArray[i];
                    } else {
                        cleanword += "_";
                    }
                }
            } else {
                cleanword = word;
            }
        }
        return cleanword;
    }
}
