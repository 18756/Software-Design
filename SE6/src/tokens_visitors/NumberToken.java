package tokens_visitors;

public class NumberToken implements Token{
    public int num;

    public NumberToken(int num) {
        this.num = num;
    }

    @Override
    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }
}
