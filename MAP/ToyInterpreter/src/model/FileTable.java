package model;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FileTable implements IFileTable {
    private static Integer fileDescriptor = 1;
    private Map<Integer,Pair<String,BufferedReader>> map;

    public FileTable() {
        map = new HashMap<>();
    }

    @Override
    public void add(String filename, BufferedReader br) {
        this.map.put(fileDescriptor++,new Pair<>(filename,br));
    }

    @Override
    public Collection<Pair<String, BufferedReader>> values() {
        return this.map.values();
    }

    @Override
    public Pair<String, BufferedReader> get(Integer key) {
        return this.map.get(key);
    }

    @Override
    public Pair<String, BufferedReader> remove(Integer key) {
        return this.map.remove(key);
    }

    @Override
    public Collection<Integer> getKeys() {
        return map.keySet();
    }

    public static Integer getFileDescriptor() {
        return fileDescriptor;
    }

    @Override
    public String toString() {
        String res = "\n";
        for(HashMap.Entry<Integer,Pair<String,BufferedReader>> entry: this.map.entrySet()){
            res +=  entry.getKey().toString() + " -> " + entry.getValue().toString() + "\n";
        }
        return res;
    }

}
