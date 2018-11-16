package controller;

import model.MyIDictionary;
import model.PrgState;
import repository.IRepository;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

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
            prgState.getHeap().setContent(conservativeGarbageCollector(
                    prgState.getSymTable().values(),prgState.getHeap().getContent()
            ));
            if (flag) {
                repository.logPrgStateExec();
            }
        }
    }

    private static void displayState(PrgState prgState) {
        System.out.println(prgState.toString());
    }

    Map<Integer,Integer> conservativeGarbageCollector(Collection<Integer> symTableValues, Map<Integer,Integer> heap){
        return heap.entrySet().stream()
                .filter(e -> symTableValues.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
    }
}
