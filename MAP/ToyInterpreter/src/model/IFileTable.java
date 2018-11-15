package model;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.util.Collection;

public interface IFileTable {
    void add(String filename, BufferedReader br);
    Collection<Pair<String,BufferedReader>> values();
    Pair<String,BufferedReader> get(Integer key);
    Pair<String,BufferedReader> remove(Integer key);
}
