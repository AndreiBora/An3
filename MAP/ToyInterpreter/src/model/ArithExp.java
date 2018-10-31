package model;

import exception.DivisionByZeroException;

public class ArithExp extends Exp{
    private Exp e1;
    private Exp e2;
    char op;

    public ArithExp( char op,Exp exp1, Exp exp2) {
        this.e1 = exp1;
        this.e2 = exp2;
        this.op = op;
    }


    @Override
    int eval(MyIDictionary<String, Integer> tbl) {
        int res = 0;
        switch (op){
            case '+':
                res = (this.e1.eval(tbl) + this.e2.eval(tbl));
                break;
            case '-':
                res =  (this.e1.eval(tbl) - this.e2.eval(tbl));
                break;
            case '*':
                res = (this.e1.eval(tbl) * this.e2.eval(tbl));
                break;
            case '/':
                if(e2.eval(tbl) != 0) {
                    res = (this.e1.eval(tbl) / this.e2.eval(tbl));
                }else{
                    throw new DivisionByZeroException();
                }
        }
        return res;
    }

    @Override
    public String toString() {
        return this.e1 + " " + op + " " + this.e2;
    }
}
