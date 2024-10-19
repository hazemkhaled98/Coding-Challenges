import lexer.DefaultLexer;
import lexer.Lexer;
import lexer.Token;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.IOException;
import java.util.List;


class LexerTest {


    Lexer lexer;

    @ParameterizedTest(name = "{0}")
    @CsvFileSource(resources = "/valid_test_cases.csv", numLinesToSkip = 1)
    void Lexer_handles_valid_JSON(String scenario, String path, int expected) throws IOException {

        lexer = new DefaultLexer(path);

        List<Token> tokens = lexer.generateTokens();
        System.out.println(scenario + " " +tokens);

        Assertions.assertEquals(expected, tokens.size());
    }

    @ParameterizedTest(name = "{0}")
    @CsvFileSource(resources = "/invalid_test_cases.csv", numLinesToSkip = 1)
    void Lexer_throws_IllegalStateException_when_Invalid_JSON(String scenario, String path, String message) throws IOException {

        lexer = new DefaultLexer(path);

        IllegalStateException exception = Assertions.assertThrows(IllegalStateException.class, () -> lexer.generateTokens());

        Assertions.assertEquals(message, exception.getMessage(), "The exception message does not match the expected value.");

    }

}
