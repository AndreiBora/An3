package model;

import exception.FileOpenException;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class OpenRFileStmt implements IStmt {
    private String varFileId;
    private String fileName;

    public OpenRFileStmt(String varFileId, String fileName) {
        this.varFileId = varFileId;
        this.fileName = fileName;
    }

    @Override
    public PrgState execute(PrgState state) {
        for (Pair<String, BufferedReader> strBr : state.getFileTable().values()) {
            if (strBr.getKey().equals(this.fileName)) {
                throw new FileOpenException(this.fileName + " is already opened");
            }
        }
        //save into file table
        try {
            BufferedReader br = new BufferedReader(new FileReader(this.fileName));
            state.getFileTable().add(this.fileName,br);
        } catch (IOException e) {
            throw new FileOpenException("Could not open the file " + this.fileName);
        }
        //save into symbol table
        state.getSymTable().put(this.varFileId, FileTable.getFileDescriptor()-1);
        return state;
    }

    @Override
    public String toString() {
        return "open(" + this.fileName + ")";
    }
}
