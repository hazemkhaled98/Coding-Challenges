package wc;

import wc.wordcounter.WordCounter;
import wc.wordcounter.WordCounterFactory;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

class CommandInvoker {
    private static final Logger logger = Logger.getLogger("wc logger");

    private CommandInvoker() {}

    static void execute(Command command, String source) {
        try {
            WordCounter wordCounter = WordCounterFactory.getWordCounter(source);
            String result = command.execute(wordCounter);
            System.out.println(result);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error processing source: " + source, e);
            System.err.println("Error: " + e.getMessage());
        }
    }
}
