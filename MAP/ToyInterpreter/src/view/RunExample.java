package view;

import controller.Controller;
import exception.*;

public class RunExample extends Command {
    private Controller ctrl;

    public RunExample(String key, String description, Controller ctrl) {
        super(key, description);
        this.ctrl = ctrl;
    }

    @Override
    public void execute() {
        try{
            ctrl.allStep(true);
        }catch (DivisionByZeroException e){
            System.out.println(e.getMessage());
        }catch (FileCloseException| FileOpenException e){
            System.out.println(e.getMessage());
        }catch (MyStmtExecException e) {
            System.out.println(e.getMessage());
        }catch (FileDescriptorNotFoundException|ReadFromFileException e){
            System.out.println(e.getMessage());
        }catch (UndefinedVariableException e){
            System.out.println(e.getMessage());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
