package abdjektcompiler;

import java.io.File;
import java.io.FileFilter;

public class AbdjektFileFilter implements FileFilter {
    public boolean accept(File file){
        return file.getName().endsWith(".abj");
    }
}
