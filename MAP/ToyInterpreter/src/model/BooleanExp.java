package model;

public class BooleanExp extends Exp {
    private Exp e1;
    private Exp e2;
    private String op;

    public BooleanExp(Exp e1, Exp e2, String op) {
        this.e1 = e1;
        this.e2 = e2;
        this.op = op;
    }

    @Override
    int eval(MyIDictionary<String, Integer> tbl, IHeap<Integer> heap) {
        Boolean res;
        switch (op) {
            case ">":
                res = e1.eval(tbl, heap) > e2.eval(tbl, heap);
                return res ? 1 : 0;
            case ">=":
                res = e1.eval(tbl, heap) >= e2.eval(tbl, heap);
                return res ? 1 : 0;
            case "==":
                res = e1.eval(tbl, heap) == e2.eval(tbl, heap);
                return res ? 1 : 0;
            case "!=":
                res = e1.eval(tbl, heap) != e2.eval(tbl, heap);
                return res ? 1 : 0;
            case "<":
                res = e1.eval(tbl, heap) < e2.eval(tbl, heap);
                return res ? 1 : 0;
            case "<=":
                res = e1.eval(tbl, heap) <= e2.eval(tbl, heap);
                return res ? 1 : 0;
        }
        throw new RuntimeException("Operator not defined");
    }

    @Override
    public String toString() {
        return this.e1.toString() + " " + op + " " + this.e2.toString();
    }
}
