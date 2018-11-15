package model;

public class ConstExp extends Exp{
    private Integer number;

    public ConstExp(int number) {
        this.number = number;
    }

    @Override
    int eval(MyIDictionary<String, Integer> tbl,IHeap<Integer> heap) {
        return number;
    }

    @Override
    public String toString() {
        return this.number.toString();
    }
}
