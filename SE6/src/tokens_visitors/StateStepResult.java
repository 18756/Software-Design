package tokens_visitors;

public class StateStepResult {
    public Token token;
    public State nextState;
    public int parsedChars;


    public StateStepResult(Token token, State nextState, int parsedChars) {
        this.token = token;
        this.nextState = nextState;
        this.parsedChars = parsedChars;
    }

    public StateStepResult(Token token, State nextState) {
        this(token, nextState, 1);
    }
}
