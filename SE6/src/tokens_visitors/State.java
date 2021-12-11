package tokens_visitors;

public interface State {
    StateStepResult apply(char c) throws Exception;
}
