package model;

import exception.FileCloseException;
import exception.FileDescriptorNotFoundException;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;

public class CloseRFileStmt implements IStmt {
    private Exp expFileId;

    public CloseRFileStmt(Exp expFileId) {
        this.expFileId = expFileId;
    }

    @Override
    public PrgState execute(PrgState state) {
        Integer val = expFileId.eval(state.getSymTable(),state.getHeap());
        Pair<String, BufferedReader> strBr = state.getFileTable().get(val);
        if (strBr == null) {
            throw new FileDescriptorNotFoundException("Could not close the file because fd = " + val + " does not exists");
        }
        BufferedReader br = strBr.getValue();
        try {
            br.close();
        } catch (IOException e) {
            throw new FileCloseException("Error while closing the file");
        }
        //delete entry from file table
        state.getFileTable().remove(val);

        return state;
    }

    @Override
    public String toString() {
        return "CloseRFileStmt(" + expFileId + ")";
    }
}
