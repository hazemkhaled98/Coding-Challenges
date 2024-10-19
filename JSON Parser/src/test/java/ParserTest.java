import lexer.DefaultLexer;
import lexer.Lexer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import parser.Parser;

import java.io.IOException;



@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ParserTest {


    @ParameterizedTest(name = "{0}")
    @CsvFileSource(resources = "/valid_test_cases.csv", numLinesToSkip = 1)
    void Parser_handles_valid_JSON(String scenario, String path, int expected) throws IOException {

        Lexer lexer = new DefaultLexer(path);
        Parser parser = new Parser(lexer);

        Assertions.assertTrue(parser.parse());
    }

}
