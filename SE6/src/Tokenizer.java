import tokens_visitors.*;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    private String exp;
    private int curId = 0;

    public Tokenizer(String exp) {
        this.exp = exp;
    }

    public List<Token> getArithmeticExpTokens() throws Exception {
        State state = new StartState();
        List<Token> tokens = new ArrayList<>();
        while (curId < exp.length()) {
            char c = exp.charAt(curId);
            StateStepResult stateStepResult = state.apply(c);
            state = stateStepResult.nextState;
            if (stateStepResult.token != null) {
                tokens.add(stateStepResult.token);
            }
            curId += stateStepResult.parsedChars;
        }
        if (state instanceof NumberState) {
            tokens.add(new NumberToken(Integer.parseInt(((NumberState) state).num)));
        }
        return tokens;
    }
}
