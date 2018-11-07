package model;

import exception.FileDescriptorNotFoundException;
import exception.ReadFromFileException;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFileStmt implements IStmt {
    private Exp expFileId;
    private String varName;

    public ReadFileStmt(Exp expFileId, String varName) {
        this.expFileId = expFileId;
        this.varName = varName;
    }

    @Override
    public PrgState execute(PrgState state) {
        Integer fd = expFileId.eval(state.getSymTable());
        //check if the file descriptor exists in the file table
        Pair<String, BufferedReader> val = state.getFileTable().get(fd);
        if (val == null) {
            throw new FileDescriptorNotFoundException(fd.toString() + " is not present in the file table");
        }
        BufferedReader br = val.getValue();
        try {
            Integer value = Integer.parseInt(br.readLine());
            if (value == null) {
                state.getSymTable().put(this.varName, 0);
            } else {
                state.getSymTable().put(this.varName, value);
            }
        } catch (IOException e) {
            throw new ReadFromFileException("Could not read from file with fd = " + fd);
        }

        return state;
    }

    @Override
    public String toString() {
        return "ReadFileStmt(" + this.expFileId.toString() + ", " + this.varName + ")";
    }
}
