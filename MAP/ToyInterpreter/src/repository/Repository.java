package repository;

import model.PrgState;

import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {
    private List<PrgState> progStates;

    public Repository() {
    }

    public Repository(PrgState progState) {
        this.progStates = new ArrayList<PrgState>();
        this.progStates.add(progState);
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
}
