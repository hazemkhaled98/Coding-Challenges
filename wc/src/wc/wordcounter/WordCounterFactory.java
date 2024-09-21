package wc.wordcounter;

import java.io.IOException;

public class WordCounterFactory {

    private WordCounterFactory(){}

    public static WordCounter getWordCounter(String source) throws IOException {
        if("Stdin".equalsIgnoreCase(source)){
            return new InputWordCounter();
        }
        else {
            return new FileWordCounter(source);
        }
    }
}
