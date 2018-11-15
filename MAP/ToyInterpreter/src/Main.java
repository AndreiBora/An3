import controller.Controller;
import javafx.util.Pair;
import model.*;
import repository.IRepository;
import repository.Repository;
import view.ExitCommand;
import view.RunExample;
import view.TextMenu;

import java.io.BufferedReader;

public class Main {

    public static void main(String[] args) {
        MyIDictionary<String,Integer> symTable = new MyDictionary<>();
        MyIList<Integer> out = new MyList<>();
        MyIStack<IStmt> exeStack1 = new MyStack<>();
        IFileTable fileTable = new FileTable();

        // v=2;Print(v)

        IStmt st1 = new CompStmt(new AssignStmt("v",new ConstExp(2)),new PrintStmt(new VarExp("v")));
        exeStack1.push(st1);

        PrgState prgState = new PrgState(exeStack1,symTable,out,fileTable);
        IRepository repo = new Repository(prgState,"log.txt");
        Controller ctrl = new Controller(repo);

        //-----------------------------------------------------------------
        //a=2+3*5;b=a+1;Print(b)

        IStmt st2 = new CompStmt(
                new AssignStmt("a", new ArithExp('+',new ConstExp(2),new ArithExp('*',new ConstExp(3), new ConstExp(5)))),
                new CompStmt(
                        new AssignStmt("b",new ArithExp('+',new VarExp("a"), new ConstExp(1))),
                        new PrintStmt(new VarExp("b"))
                )
        );
        MyIDictionary<String,Integer> symTable2 = new MyDictionary<>();
        MyIList<Integer> out2 = new MyList<>();
        MyIStack<IStmt> exeStack2 = new MyStack<>();
        IFileTable fileTable2 = new FileTable();

        exeStack2.push(st2);
        PrgState prgState2 = new PrgState(exeStack2,symTable2,out2,fileTable2);
        IRepository repo2 = new Repository(prgState2,"log.txt");
        Controller ctrl2 = new Controller(repo2);

        //-----------------------------------------------------------------
        //a=2-2;(If a Then v=2 Else v=3); Print(v)
        IStmt st3 = new CompStmt(
                new AssignStmt("a", new ArithExp('-',new ConstExp(2), new ConstExp(2))),
                new CompStmt(
                        new IfStmt(new VarExp("a"),
                                new AssignStmt("v",new ConstExp(2)),
                                new AssignStmt("v", new ConstExp(3))),
                        new PrintStmt(new VarExp("v"))
                )
        );
        MyIDictionary<String,Integer> symTable3 = new MyDictionary<>();
        MyIList<Integer> out3 = new MyList<>();
        MyIStack<IStmt> exeStack3 = new MyStack<>();
        IFileTable fileTable3 = new FileTable();

        exeStack3.push(st3);
        PrgState prgState3 = new PrgState(exeStack3,symTable3,out3,fileTable3);
        IRepository repo3 = new Repository(prgState3,"log.txt");
        Controller ctrl3 = new Controller(repo3);

        //-----------------------------------------------------------------
        /*
        *   Lab5Ex1
        *   openRFile (var_f, "test.in");
        *   readFile (var_f, var_c); print (var_c);
        *   If var_c then readFile (var_f, var_c); print (var_c) else print (0);
        *   closeRFile (var_f)
        */

        IStmt st4 = new CompStmt(
                new OpenRFileStmt("var_f", "test.in"),
                new CompStmt(
                        new ReadFileStmt(new VarExp("var_f"), "var_c"),
                        new CompStmt(
                                new PrintStmt(new VarExp("var_c")),
                                new CompStmt(
                                        new IfStmt(
                                                new VarExp("var_c"),
                                                new CompStmt(
                                                    new ReadFileStmt(new VarExp("var_f"), "var_c"),
                                                    new PrintStmt(new VarExp("var_c"))
                                                ),
                                                new PrintStmt(new ConstExp(0))
                                        ),
                                        new CloseRFileStmt(new VarExp("var_f"))
                                )
                        )
                )
        );
        MyIDictionary<String,Integer> symTable4 = new MyDictionary<>();
        MyIList<Integer> out4 = new MyList<>();
        MyIStack<IStmt> exeStack4 = new MyStack<>();
        IFileTable fileTable4 = new FileTable();
        exeStack4.push(st4);
        PrgState prgState4 = new PrgState(exeStack4,symTable4,out4,fileTable4);
        IRepository repo4 = new Repository(prgState4,"log.txt");
        Controller ctrl4 = new Controller(repo4);


        /*
        *   Lab5Ex2
        *   openRFile (var_f, "test.in");
        *   readFile (var_f + 2, var_c); print (var_c);
        *   If var_c then readFile (var_f, var_c); print (var_c) else print (0);
        *   closeRFile (var_f)
        */

        IStmt st5 = new CompStmt(
                new OpenRFileStmt("var_f", "test.in"),
                new CompStmt(
                        new ReadFileStmt(new ArithExp('+', new VarExp("var_f"), new ConstExp(2)), "var_c"),
                        new CompStmt(
                                new PrintStmt(new VarExp("var_c")),
                                new CompStmt(
                                        new IfStmt(
                                                new VarExp("var_c"),
                                                new CompStmt(
                                                    new ReadFileStmt(new VarExp("var_f"), "var_c"),
                                                    new PrintStmt(new VarExp("var_c"))
                                                ),
                                                new PrintStmt(new ConstExp(0))
                                        ),
                                        new CloseRFileStmt(new VarExp("var_f"))
                                )
                        )
                )
        );

        MyIDictionary<String,Integer> symTable5 = new MyDictionary<>();
        MyIList<Integer> out5 = new MyList<>();
        MyIStack<IStmt> exeStack5 = new MyStack<>();
        IFileTable fileTable5 = new FileTable();
        exeStack5.push(st5);
        PrgState prgState5 = new PrgState(exeStack5,symTable5,out5,fileTable5);
        IRepository repo5 = new Repository(prgState5,"log.txt");
        Controller ctrl5 = new Controller(repo5);

        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0","exit"));
        menu.addCommand(new RunExample("1",st1.toString(),ctrl));
        menu.addCommand(new RunExample("2",st2.toString(),ctrl2));
        menu.addCommand(new RunExample("3",st3.toString(),ctrl3));
        menu.addCommand(new RunExample("4",st4.toString(),ctrl4));
        menu.addCommand(new RunExample("5",st5.toString(),ctrl5));
        menu.show();
    }

}
