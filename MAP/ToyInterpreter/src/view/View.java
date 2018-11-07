//package view;
//
//import controller.Controller;
//import exception.MyStmtExecException;
//import model.*;
//import repository.IRepository;
//import repository.Repository;
//
//import java.util.Scanner;
//
//public class View {
//
//    private static int readInteger(Scanner scanner){
//        while (!scanner.hasNextInt()){
//            scanner.next();
//            System.out.println("Enter an integer");
//        }
//        int x = scanner.nextInt();
//        scanner.nextLine();
//        return x;
//    }
//
//    private static void showMenu(){
//        System.out.println("Menu:");
//        System.out.println("Choose one statement");
//        System.out.println("1. v=2;Print(v)");
//        System.out.println("2. a=2+3*5;b=a+1;Print(b)");
//        System.out.println("3. a=2-2;(If a Then v=2 Else v=3); Print(v)");
//        System.out.println("4. Exit");
//    }
//
//    private static void printMenuRunningType(){
//        System.out.println("Menu:");
//        System.out.println("Choose type of run");
//        System.out.println("1. step by step");
//        System.out.println("2. run all");
//        System.out.println("4. Exit");
//    }
//
//    public void run() {
//        Scanner scanner = new Scanner(System.in);
//        while (true) {
//            showMenu();
//            int x = readInteger(scanner);
//            switch (x) {
//                case 1:
//                    MyIDictionary<String,Integer> symTable = new MyDictionary<>();
//                    MyIList<Integer> out = new MyList<>();
//                    MyIStack<IStmt> exeStack1 = new MyStack<>();
//                    IStmt st1 = new CompStmt(new AssignStmt("v",new ConstExp(2)),new PrintStmt(new VarExp("v")));
//                    exeStack1.push(st1);
//                    PrgState prgState = new PrgState(exeStack1,symTable,out);
//                    IRepository repo = new Repository(prgState);
//                    Controller ctrl = new Controller(repo);
//                    printMenuRunningType();
//                    runProgram(ctrl);
//                    break;
//                case 2:
//                    MyIDictionary<String,Integer> symTable2 = new MyDictionary<>();
//                    MyIList<Integer> out2 = new MyList<>();
//                    MyIStack<IStmt> exeStack2 = new MyStack<>();
//                    IStmt ex2 = new CompStmt(
//                            new AssignStmt("a", new ArithExp('+',new ConstExp(2),new ArithExp('*',new ConstExp(3), new ConstExp(5)))),
//                            new CompStmt(
//                                    new AssignStmt("b",new ArithExp('+',new VarExp("a"), new ConstExp(1))),
//                                    new PrintStmt(new VarExp("b"))
//                            )
//                    );
//                    exeStack2.push(ex2);
//                    PrgState prgState2 = new PrgState(exeStack2,symTable2,out2);
//                    IRepository repo2 = new Repository(prgState2);
//                    Controller ctrl2 = new Controller(repo2);
//                    printMenuRunningType();
//                    runProgram(ctrl2);
//                    break;
//                case 3:
//                    MyIDictionary<String,Integer> symTable3 = new MyDictionary<>();
//                    MyIList<Integer> out3 = new MyList<>();
//                    MyIStack<IStmt> exeStack3 = new MyStack<>();
//                    IStmt ex3 = new CompStmt(
//                            new AssignStmt("a", new ArithExp('-',new ConstExp(2), new ConstExp(2))),
//                            new CompStmt(
//                                    new IfStmt(new VarExp("a"),
//                                            new AssignStmt("v",new ConstExp(2)),
//                                            new AssignStmt("v", new ConstExp(3))),
//                                    new PrintStmt(new VarExp("v"))
//                            )
//                    );
//
//                    exeStack3.push(ex3);
//                    PrgState prgState3 = new PrgState(exeStack3,symTable3,out3);
//                    IRepository repo3 = new Repository(prgState3);
//                    Controller ctrl3 = new Controller(repo3);
//                    printMenuRunningType();
//                    runProgram(ctrl3);
//                    break;
//                case 4:
//                    return;
//                default:
//                    System.out.println("Wrong choise");
//            }
//        }
//    }
//
//    private static void runProgram(Controller ctrl) {
//        Scanner scanner =new Scanner(System.in);
//        int y = readInteger(scanner);
//        switch (y){
//            case 1:
//                boolean isExecuting = true;
//                try {
//                    while (isExecuting) {
//                        ctrl.oneStep();
//                        System.out.println("Press 1.next 2.exit");
//                        int res = readInteger(scanner);
//                        if (res == 2) {
//                            isExecuting = false;
//                        }
//                    }
//                }catch (MyStmtExecException e){
//                    System.out.println(e.getMessage());
//                }
//                break;
//            case 2:
//                ctrl.allStep(true);
//                break;
//            default:
//                System.out.println("Wrong choise");
//        }
//    }
//
//}
