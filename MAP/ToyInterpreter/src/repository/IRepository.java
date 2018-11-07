package repository;

import model.PrgState;

import java.util.List;

public interface IRepository {

    void add(PrgState state);
    List<PrgState> getProgStates();
    void setProgStates(List<PrgState> progStates);
    PrgState getCurrentState();
    void logPrgStateExec();
}
