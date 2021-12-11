package tokens_visitors;

public class NumberState implements State {
    public String num;

    public NumberState(char firstDigit) {
        num = firstDigit + "";
    }

    @Override
    public StateStepResult apply(char c) {
        if (Character.isDigit(c)) {
            num += c;
            return new StateStepResult(null, this);
        }
        return new StateStepResult(new NumberToken(Integer.parseInt(num)), new StartState(), 0);
    }
}
