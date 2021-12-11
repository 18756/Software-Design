import tokens_visitors.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ParserVisitor implements TokenVisitor {
    private List<Token> reversePolishNotation = new ArrayList<>();
    private Stack<Token> stack = new Stack<>();

    @Override
    public void visit(NumberToken token) {
        reversePolishNotation.add(token);
    }

    @Override
    public void visit(Brace token) throws Exception {
        if (token.type == Brace.Type.LEFT) {
            stack.add(token);
        } else {
            while (!stack.isEmpty() && !isPeekLeftBrace()) {
                reversePolishNotation.add(stack.pop());
            }
            if (stack.isEmpty()) {
                throw new Exception("no open brace found to close brace");
            } else {
                stack.pop();
            }
        }
    }

    @Override
    public void visit(Operation token) {
        while (!stack.isEmpty() &&
                (isPeekOperationHasMorePriority(token.getPriority()) ||
                        token.getPriority() == 1 && stack.peek() instanceof Subtraction ||
                        token.getPriority() == 2 && stack.peek() instanceof Div)) {
            reversePolishNotation.add(stack.pop());
        }
        stack.add(token);
    }

    private boolean isPeekLeftBrace() {
        return stack.peek() instanceof Brace && ((Brace) stack.peek()).type == Brace.Type.LEFT;
    }

    private boolean isPeekOperationHasMorePriority(int priority) {
        return stack.peek() instanceof Operation && ((Operation) stack.peek()).getPriority() > priority;
    }

    public List<Token> getReversePolishNotation() throws Exception {
        while (!stack.isEmpty()) {
            Token token = stack.pop();
            if (!(token instanceof Operation)) {
                throw new Exception("incorrect brace balance");
            }
            reversePolishNotation.add(token);
        }
        return reversePolishNotation;
    }
}
