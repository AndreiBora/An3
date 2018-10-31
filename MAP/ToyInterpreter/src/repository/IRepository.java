package repository;

import model.PrgState;

import java.util.List;

public interface IRepository {

    public void add(PrgState state);
    public List<PrgState> getProgStates();
    public void setProgStates(List<PrgState> progStates);
    public PrgState getCurrentState();
}
