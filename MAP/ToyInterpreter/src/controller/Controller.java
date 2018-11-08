package controller;

import exception.MyStmtExecException;
import model.PrgState;
import repository.IRepository;

public class Controller {
    private IRepository repository;

    public Controller(IRepository repository) {
        this.repository = repository;
    }

    public PrgState oneStep() {
        PrgState state = this.repository.getCurrentState().oneStep();
        displayState(state);
        return state;
    }


    public void allStep(Boolean flag) {
        PrgState prgState = this.repository.getCurrentState();

        while (!prgState.getExeStack().isEmpty()) {
            prgState.oneStep();
            if (flag) {
                repository.logPrgStateExec();
            }
        }
    }

    private static void displayState(PrgState prgState) {
        System.out.println(prgState.toString());
    }
}
