package lexer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DefaultLexer implements Lexer {


    private final String source;
    private final List<Token> tokens;
    private int start;
    private int current;
    private boolean isArray;

    public DefaultLexer(String filePath) throws IOException {
        source = FileReader.readJson(Path.of(filePath));
        tokens = new ArrayList<>();
        start = 0;
        current = 0;
        isArray = false;
    }

    public List<Token> generateTokens() {

        if (start == source.length()) {
            throw new IllegalStateException("Empty Json file");
        }

        while (!isAtEnd()) {
            start = current;
            generateToken();
        }

        tokens.set(tokens.size() - 1, Token.JSON_END);
        return tokens;
    }

    private void generateToken() {
        char ch = advance();

        switch (ch) {
            case ' ', '\r', '\t', '\n' -> {
            } // ignore white spaces and new lines.
            case '{' -> scanLeftBrace();
            case '}' -> addToken(Token.OBJ_END);
            case ':' -> addToken(Token.COLON);
            case ',' -> addToken(Token.COMMA);
            case '[' -> {
                addToken(Token.ARRAY_START);
                isArray = true;
            }
            case ']' -> {
                addToken(Token.ARRAY_END);
                isArray = false;
            }
            case '"' -> scanString();
            default -> {
                if (isDigit(ch)) {
                    scanNumber();
                } else if (isAlpha(ch)) {
                    scanKeyword();
                } else throw new IllegalStateException("Unrecognized character: " + ch);
            }
        }
    }

    private void scanLeftBrace() {
        if (tokens.isEmpty()) {
            addToken(Token.JSON_START);
        } else {
            addToken(Token.OBJ_START);
        }
    }

    private void scanString() {
        while (peek() != '"' && !isAtEnd()) {
            advance();
        }
        if (isAtEnd()) {
            throw new IllegalStateException("Unterminated string.");
        }
        advance();

        addString();
    }

    private void addString() {
        Token lastToken = tokens.get(tokens.size() - 1);

        if (isArray)
            addToken(Token.ARRAY_ELEMENT);
        else if (lastToken == Token.COLON)
            addToken(Token.VALUE);
        else
            addToken(Token.KEY);
    }

    private void scanNumber() {
        while (isDigit(peek())) advance();
        if (peek() == '.' && isDigit(peekNext())) {
            do advance();
            while (isDigit(peek()));
        }
        addValue();
    }

    private void scanKeyword() {
        while (isAlpha(peek())) advance();
        String text = source.substring(start, current);

        if (text.equals("true") || text.equals("false") || text.equals("null")) {
            addValue();
        } else throw new IllegalStateException("Unrecognized Keyword: " + text);
    }

    private void addValue() {

        if (isArray)
            addToken(Token.ARRAY_ELEMENT);
        else
            addToken(Token.VALUE);
    }

    private void addToken(Token token) {
        tokens.add(token);
    }

    private char advance() {
        return source.charAt(current++);
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private boolean isAlpha(char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
    }

    private boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }
}
