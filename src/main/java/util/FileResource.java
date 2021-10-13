package util;

import core.Resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class FileResource extends Resource {
    File file;
    public FileResource(String s) {
        file =new File(s);
    }
    public FileResource(File f) {
        file=f;
    }
    public File getFile() {
        return file;
    }
    public FileReader getReader() throws FileNotFoundException {
        return new FileReader(file);
    }

}
