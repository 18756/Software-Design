package tokens_visitors;

public class Plus extends Operation {
    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public String getOperationString() {
        return "+";
    }

    @Override
    public double calc(double x, double y) {
        return x + y;
    }
}
