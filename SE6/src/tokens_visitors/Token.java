package tokens_visitors;

public interface Token {
    void accept(TokenVisitor visitor) throws Exception;
}
