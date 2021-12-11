package tokens_visitors;

public class Brace implements Token {
    public Type type;

    public Brace(Type type) {
        this.type = type;
    }

    @Override
    public void accept(TokenVisitor visitor) throws Exception {
        visitor.visit(this);
    }

    public enum Type {
        LEFT, RIGHT
    }
}
