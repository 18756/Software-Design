package tokens_visitors;

public class StartState implements State {
    @Override
    public StateStepResult apply(char c) throws Exception {
        if (Character.isWhitespace(c)) {
            return new StateStepResult(null, this);
        }
        if (Character.isDigit(c)) {
            return new StateStepResult(null, new NumberState(c));
        }
        return switch (c) {
            case '(' -> new StateStepResult(new Brace(Brace.Type.LEFT), this);
            case ')' -> new StateStepResult(new Brace(Brace.Type.RIGHT), this);
            case '-' -> new StateStepResult(new Subtraction(), this);
            case '+' -> new StateStepResult(new Plus(), this);
            case '/' -> new StateStepResult(new Div(), this);
            case '*' -> new StateStepResult(new Prod(), this);
            default -> throw new Exception("illegal character: " + c);
        };
    }
}
