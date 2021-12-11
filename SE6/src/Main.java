import tokens_visitors.Token;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String exp = reader.readLine();
        Tokenizer tokenizer = new Tokenizer(exp);
        List<Token> tokens = tokenizer.getArithmeticExpTokens();
        ParserVisitor parserVisitor = new ParserVisitor();
        for (Token token : tokens) {
            token.accept(parserVisitor);
        }
        List<Token> reversePolishNotation = parserVisitor.getReversePolishNotation();
        PrintVisitor printVisitor = new PrintVisitor();
        System.out.println("Reverse polish notation: ");
        for (Token token : reversePolishNotation) {
            token.accept(printVisitor);
        }
        CalcVisitor calcVisitor = new CalcVisitor();
        for (Token token : reversePolishNotation) {
            token.accept(calcVisitor);
        }
        System.out.println("\nResult: " + calcVisitor.getResult());
    }
}
