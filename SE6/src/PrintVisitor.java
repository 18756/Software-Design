import tokens_visitors.Brace;
import tokens_visitors.NumberToken;
import tokens_visitors.Operation;
import tokens_visitors.TokenVisitor;

public class PrintVisitor implements TokenVisitor {
    @Override
    public void visit(NumberToken token) {
        System.out.print(token.num + " ");
    }

    @Override
    public void visit(Brace token) throws Exception {
        if (token.type == Brace.Type.LEFT) {
            System.out.print("( ");
        } else {
            System.out.print(") ");
        }
    }

    @Override
    public void visit(Operation token) {
        System.out.print(token.getOperationString() + " ");
    }
}
