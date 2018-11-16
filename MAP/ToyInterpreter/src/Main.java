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
        IHeap<Integer> heap = new Heap<>();

        // v=2;Print(v)

        IStmt st1 = new CompStmt(new AssignStmt("v",new ConstExp(2)),new PrintStmt(new VarExp("v")));
        exeStack1.push(st1);

        PrgState prgState = new PrgState(exeStack1,symTable,out,fileTable,heap);
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
        IHeap<Integer> heap2 = new Heap<>();

        exeStack2.push(st2);
        PrgState prgState2 = new PrgState(exeStack2,symTable2,out2,fileTable2,heap2);
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
        IHeap<Integer> heap3 = new Heap<>();

        exeStack3.push(st3);
        PrgState prgState3 = new PrgState(exeStack3,symTable3,out3,fileTable3,heap3);
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
        IHeap<Integer> heap4 = new Heap<>();

        exeStack4.push(st4);
        PrgState prgState4 = new PrgState(exeStack4,symTable4,out4,fileTable4,heap4);
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
        IHeap<Integer> heap5 = new Heap<>();

        exeStack5.push(st5);
        PrgState prgState5 = new PrgState(exeStack5,symTable5,out5,fileTable5,heap5);
        IRepository repo5 = new Repository(prgState5,"log.txt");
        Controller ctrl5 = new Controller(repo5);
        //---------------------------------------------------------------------------------

        //v=10;new(v,20);new(a,22);print(v)

        IStmt st6 = new CompStmt(
                new AssignStmt("v",new ConstExp(10)),
                new CompStmt(
                        new NewStmt("v",new ConstExp(20)),
                        new CompStmt(
                                new NewStmt("a",new ConstExp(22)),
                                new PrintStmt(new VarExp("v"))
                        )
                )
        );

        MyIDictionary<String,Integer> symTable6 = new MyDictionary<>();
        MyIList<Integer> out6 = new MyList<>();
        MyIStack<IStmt> exeStack6 = new MyStack<>();
        IFileTable fileTable6 = new FileTable();
        IHeap<Integer> heap6 = new Heap<>();

        exeStack6.push(st6);
        PrgState prgState6 = new PrgState(exeStack6,symTable6,out6,fileTable6,heap6);
        IRepository repo6 = new Repository(prgState6,"log.txt");
        Controller ctrl6 = new Controller(repo6);

        //---------------------------------------------------------------------------------

        //v=10;new(v,20);new(a,22);print(100+rH(v));print(100+rH(a))

        IStmt st7 = new CompStmt(
                new AssignStmt("v",new ConstExp(10)),
                new CompStmt(
                        new NewStmt("v",new ConstExp(20)),
                        new CompStmt(
                                new NewStmt("a",new ConstExp(22)),
                                new CompStmt(
                                        new PrintStmt(new ArithExp('+',new ConstExp(100),new ReadHeapExp("v"))),
                                        new PrintStmt(new ArithExp('+',new ConstExp(100),new ReadHeapExp("a")))
                                )
                        )
                )

        );

        MyIDictionary<String,Integer> symTable7 = new MyDictionary<>();
        MyIList<Integer> out7 = new MyList<>();
        MyIStack<IStmt> exeStack7 = new MyStack<>();
        IFileTable fileTable7 = new FileTable();
        IHeap<Integer> heap7 = new Heap<>();

        exeStack7.push(st7);
        PrgState prgState7 = new PrgState(exeStack7,symTable7,out7,fileTable7,heap7);
        IRepository repo7 = new Repository(prgState7,"log.txt");
        Controller ctrl7 = new Controller(repo7);

        //---------------------------------------------------------------------------------

        //v=10;new(v,20);new(a,22);wH(a,30);print(a);print(rH(a))

        IStmt st8 = new CompStmt(
                new AssignStmt("v",new ConstExp(10)),
                new CompStmt(
                        new NewStmt("v",new ConstExp(20)),
                        new CompStmt(
                                new NewStmt("a",new ConstExp(22)),
                                new CompStmt(
                                        new WriteHeapStmt("a",new ConstExp(30)),
                                        new CompStmt(
                                                new PrintStmt(new VarExp("a")),
                                                new PrintStmt(new ReadHeapExp("a"))
                                        )
                                )
                        )
                )
        );

        MyIDictionary<String,Integer> symTable8 = new MyDictionary<>();
        MyIList<Integer> out8 = new MyList<>();
        MyIStack<IStmt> exeStack8 = new MyStack<>();
        IFileTable fileTable8 = new FileTable();
        IHeap<Integer> heap8 = new Heap<>();

        exeStack8.push(st8);
        PrgState prgState8 = new PrgState(exeStack8,symTable8,out8,fileTable8,heap8);
        IRepository repo8 = new Repository(prgState8,"log.txt");
        Controller ctrl8 = new Controller(repo8);

        //---------------------------------------------------------------------------------

        // v=10;new(v,20);new(a,22);wH(a,30);print(a);print(rH(a));a=0
        IStmt st9 = new CompStmt(
                new AssignStmt("v",new ConstExp(10)),
                new CompStmt(
                        new NewStmt("v",new ConstExp(20)),
                        new CompStmt(
                                new NewStmt("a",new ConstExp(22)),
                                new CompStmt(
                                        new WriteHeapStmt("a",new ConstExp(30)),
                                        new CompStmt(
                                                new PrintStmt(new VarExp("a")),
                                                new CompStmt(
                                                        new PrintStmt(
                                                                new ReadHeapExp("a")
                                                        ),
                                                        new AssignStmt("a",new ConstExp(0))
                                                )
                                        )

                                )
                        )
                )
        );

        MyIDictionary<String,Integer> symTable9 = new MyDictionary<>();
        MyIList<Integer> out9 = new MyList<>();
        MyIStack<IStmt> exeStack9 = new MyStack<>();
        IFileTable fileTable9 = new FileTable();
        IHeap<Integer> heap9 = new Heap<>();

        exeStack9.push(st9);
        PrgState prgState9 = new PrgState(exeStack9,symTable9,out9,fileTable9,heap9);
        IRepository repo9 = new Repository(prgState9,"log.txt");
        Controller ctrl9 = new Controller(repo9);

        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0","exit"));
        menu.addCommand(new RunExample("1",st1.toString(),ctrl));
        menu.addCommand(new RunExample("2",st2.toString(),ctrl2));
        menu.addCommand(new RunExample("3",st3.toString(),ctrl3));
        menu.addCommand(new RunExample("4",st4.toString(),ctrl4));
        menu.addCommand(new RunExample("5",st5.toString(),ctrl5));
        menu.addCommand(new RunExample("6",st6.toString(),ctrl6));
        menu.addCommand(new RunExample("7",st7.toString(),ctrl7));
        menu.addCommand(new RunExample("8",st8.toString(),ctrl8));
        menu.addCommand(new RunExample("9",st9.toString(),ctrl9));
        menu.show();
    }

}
