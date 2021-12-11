package tokens_visitors;

public abstract class Operation implements Token {

    @Override
    public void accept(TokenVisitor visitor) throws Exception {
        visitor.visit(this);
    }

    public abstract int getPriority();

    public abstract String getOperationString();

    public abstract double calc(double x, double y);
}
