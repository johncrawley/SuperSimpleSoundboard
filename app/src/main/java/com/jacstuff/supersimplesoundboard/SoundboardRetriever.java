package com.jacstuff.supersimplesoundboard;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SoundboardRetriever {

    private String path;
    private FileFilter filter;

    public SoundboardRetriever(String path){

        this.path = path;
        filter = new DirectoryFileFilter();
    }


    public List<File> retrieve(){

        File file = new File(path);
        File[] files = file.listFiles(filter);
        if(files == null){
            return new ArrayList<>();
        }
        return Arrays.asList(files);
    }
}

class DirectoryFileFilter implements FileFilter{
    public boolean accept(File file){
        return file.isDirectory();
    }
}
