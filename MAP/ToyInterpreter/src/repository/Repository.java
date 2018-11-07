package repository;

import exception.MyStmtExecException;
import model.PrgState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {
    private List<PrgState> progStates;
    private String logFilePath;

    public Repository(PrgState progState,String logFilePath) {
        this.progStates = new ArrayList<>();
        this.progStates.add(progState);
        this.logFilePath = logFilePath;
    }

    @Override
    public void add(PrgState progState) {
        this.progStates.add(progState);
    }

    @Override
    public List<PrgState> getProgStates() {
        return this.progStates;
    }

    @Override
    public void setProgStates(List<PrgState> progStates) {
        this.progStates = progStates;
    }

    @Override
    public PrgState getCurrentState() {
        return this.progStates.get(0);
    }

    @Override
    public void logPrgStateExec() {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(this.logFilePath,true))){
            writer.append(getCurrentState().toString());
        }catch (IOException e){
            throw new MyStmtExecException("Error while opening the log file");
        }
    }

}
