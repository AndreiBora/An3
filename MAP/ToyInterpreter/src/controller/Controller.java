package controller;

import javafx.util.Pair;
import model.*;
import repository.IRepository;

import javax.sound.midi.Soundbank;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Controller {
    private IRepository repository;
    private ExecutorService executor;

    public Controller(IRepository repository) {
        this.repository = repository;
    }

    public void allStep(Boolean flag) throws InterruptedException {
        executor = Executors.newFixedThreadPool(2);
        List<PrgState> prgList = repository.getProgStates();
        IHeap<Integer> heap = prgList.get(0).getHeap();
        IFileTable fileTable = prgList.get(0).getFileTable();
        //remove completed programs
        try {
            while (prgList.size() > 0) {
                oneStepForAllPrg(prgList);
                //remove completed prgrams
                prgList = removeCompletedPrg(prgList);
                this.repository.setProgStates(prgList);
                heap.setContent(
                        conservativeGarbageCollector(mergeTables(prgList), heap.getContent())
                );
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            fileTable.values().stream().forEach(
                    f-> {
                        try {
                            f.getValue().close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            );
        }

        executor.shutdown();
        repository.setProgStates(prgList);

    }

    private Set<Integer> mergeTables(List<PrgState> prgStates){
        Set<Integer> result = new HashSet<>();
        prgStates.stream().forEach(
                p->result.addAll(p.getSymTable().values())
        );
        return result;
    }

    public void oneStepForAllPrg(List<PrgState> prgList) throws InterruptedException {
        //prepare the list of callables
        List<Callable<PrgState>> callList = prgList.stream()
                .map((PrgState p) -> (Callable<PrgState>) (() -> {
                    return p.oneStep();
                }))
                .collect(Collectors.toList());

        //start execution

        List<PrgState> newPrgList = executor.invokeAll(callList)
                .stream().map(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException e) {
                        System.out.println(e.getCause().getMessage());
                    } catch (ExecutionException e) {
                        System.out.println(e.getMessage());
                    }
                    return null;
                }).filter(p -> p != null)
                .collect(Collectors.toList());
        //add the new create threads to the list of existing threads
        prgList.addAll(newPrgList);

        //log after execution
        prgList.forEach(prg -> repository.logPrgStateExec(prg));

        //save programs in repo
        //repository.setProgStates(prgList);
    }

    public List<PrgState> removeCompletedPrg(List<PrgState> inPrgList) {
        return inPrgList.stream()
                .filter(p -> p.isNotCompleted())
                .collect(Collectors.toList());
    }

    private static void displayState(PrgState prgState) {
        System.out.println(prgState.toString());
    }

    Map<Integer, Integer> conservativeGarbageCollector(Collection<Integer> symTableValues, Map<Integer, Integer> heap) {
        return heap.entrySet().stream()
                .filter(e -> symTableValues.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
