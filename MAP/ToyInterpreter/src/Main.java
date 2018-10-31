import controller.Controller;
import model.*;
import repository.IRepository;
import repository.Repository;
import view.View;

public class Main {

    public static void main(String[] args) {
//        MyIDictionary<String,Integer> symTable = new MyDictionary<>();
//        MyIList<Integer> out = new MyList<>();
//        MyIStack<IStmt> exeStack1 = new MyStack<>();


        // v=2;Print(v)
        /*
        IStmt st1 = new CompStmt(new AssignStmt("v",new ConstExp(2)),new PrintStmt(new VarExp("v")));
        exeStack1.push(st1);

        PrgState prgState = new PrgState(exeStack1,symTable,out);
        IRepository repo = new Repository(prgState);
        Controller ctrl = new Controller(repo);
        ctrl.allStep(true);
        */
        //a=2+3*5;b=a+1;Print(b)
        /*
        IStmt ex2 = new CompStmt(
                new AssignStmt("a", new ArithExp('+',new ConstExp(2),new ArithExp('*',new ConstExp(3), new ConstExp(5)))),
                new CompStmt(
                        new AssignStmt("b",new ArithExp('+',new VarExp("a"), new ConstExp(1))),
                        new PrintStmt(new VarExp("b"))
                )
        );

        MyIStack<IStmt> exeStack2 = new MyStack<>();
        exeStack2.push(ex2);
        PrgState prgState2 = new PrgState(exeStack2,symTable,out);
        IRepository repo2 = new Repository(prgState2);
        Controller ctrl2 = new Controller(repo2);
        ctrl2.allStep(true);
        */

        //a=2-2;(If a Then v=2 Else v=3); Print(v)
//        IStmt ex3 = new CompStmt(
//                new AssignStmt("a", new ArithExp('-',new ConstExp(2), new ConstExp(2))),
//                new CompStmt(
//                        new IfStmt(new VarExp("a"),
//                                new AssignStmt("v",new ConstExp(2)),
//                                new AssignStmt("v", new ConstExp(3))),
//                        new PrintStmt(new VarExp("v"))
//                )
//        );
//
//        MyIStack<IStmt> exeStack3 = new MyStack<>();
//        exeStack3.push(ex3);
//        PrgState prgState3 = new PrgState(exeStack3,symTable,out);
//        IRepository repo3 = new Repository(prgState3);
//        Controller ctrl3 = new Controller(repo3);
//        ctrl3.allStep(true);

        View view = new View();
        view.run();
    }

}
