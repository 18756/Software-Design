package tokens_visitors;

public interface TokenVisitor {
    void visit(NumberToken token);
    void visit(Brace token) throws Exception;
    void visit(Operation token) throws Exception;
}
