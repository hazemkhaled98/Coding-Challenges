package parser;

import lexer.Lexer;
import lexer.Token;

import static lexer.Token.*;

import java.util.*;

public class Parser {


    private final Lexer lexer;
    private final Map<Token, Set<Token>> validPrecedents = new HashMap<>();

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        validPrecedents.put(JSON_START, Collections.emptySet());
        validPrecedents.put(KEY, Set.of(COMMA, JSON_START, OBJ_START));
        validPrecedents.put(VALUE, Set.of(COLON));
        validPrecedents.put(COMMA, Set.of(VALUE, ARRAY_ELEMENT, OBJ_END, ARRAY_END));
        validPrecedents.put(COLON, Set.of(KEY));
        validPrecedents.put(OBJ_START, Set.of(COLON));
        validPrecedents.put(OBJ_END, Set.of(OBJ_START, VALUE, ARRAY_END));
        validPrecedents.put(ARRAY_START, Set.of(COLON));
        validPrecedents.put(ARRAY_END, Set.of(ARRAY_START, ARRAY_ELEMENT));
        validPrecedents.put(ARRAY_ELEMENT, Set.of(COMMA, ARRAY_START));
        validPrecedents.put(JSON_END, Set.of(JSON_START, VALUE, ARRAY_END, OBJ_END));
    }


    public boolean parse() {

        List<Token> tokens = lexer.generateTokens();

        ArrayDeque<Token> stack = new ArrayDeque<>();

        for (Token token : tokens) {

            if (stack.isEmpty()) {
                if (token == JSON_START)
                    stack.add(token);
                else{
                    return false;
                }
            } else {
                Token prev = stack.peek();
                if (validPrecedents.get(token).contains(prev)) {
                    if(prev != JSON_START) stack.pop();
                    stack.push(token);
                } else {
                    return false;
                }
            }
        }

        return stack.size() == 2 && stack.pop() == JSON_END && stack.pop() == JSON_START;
    }
}
