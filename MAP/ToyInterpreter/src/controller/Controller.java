package controller;

import exception.MyStmtExecException;
import model.IStmt;
import model.PrgState;
import repository.IRepository;

public class Controller {
    private IRepository repository;

    public Controller(IRepository repository) {
        this.repository = repository;
    }

    public PrgState oneStep(){
        PrgState state = this.repository.getCurrentState().oneStep();
        displayState(state);
        return state;
    }



    public void allStep(Boolean flag){
        PrgState prgState = this.repository.getCurrentState();
        try{
            while (true){
                if(flag){
                    displayState(prgState);
                }
                prgState.oneStep();
            }
        }catch (MyStmtExecException e){
            //Program finish execution
        }
    }

    public static void displayState(PrgState prgState){
        System.out.println(prgState.toString());
    }
}
