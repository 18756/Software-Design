import tokens_visitors.Brace;
import tokens_visitors.NumberToken;
import tokens_visitors.Operation;
import tokens_visitors.TokenVisitor;

import java.util.Stack;

public class CalcVisitor implements TokenVisitor {
    private Stack<Double> stack = new Stack<>();

    @Override
    public void visit(NumberToken token) {
        stack.add((double) token.num);
    }

    @Override
    public void visit(Brace token) throws Exception {
        throw new Exception("braces are forbidden in reverse polish notation");
    }

    @Override
    public void visit(Operation token) throws Exception {
        if (stack.size() < 2) {
            throw new Exception("invalid polish notation");
        }
        double y = stack.pop();
        double x = stack.pop();
        stack.add(token.calc(x, y));
    }

    public double getResult() throws Exception {
        if (stack.size() != 1) {
            throw new Exception("invalid polish notation");
        }
        return stack.peek();
    }
}
