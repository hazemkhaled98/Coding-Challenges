package lexer;

import java.io.IOException;
import java.util.List;

public interface Lexer {

    List<Token> generateTokens();
}
