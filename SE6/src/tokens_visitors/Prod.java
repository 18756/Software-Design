package tokens_visitors;

public class Prod extends Operation {
    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public String getOperationString() {
        return "*";
    }

    @Override
    public double calc(double x, double y) {
        return x * y;
    }
}
