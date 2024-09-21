package wc;

import wc.wordcounter.WordCounter;
import wc.wordcounter.WordCounterFactory;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

class Commander {

    private static Logger logger = Logger.getLogger("wc logger");

    private Commander(){}

    static void execute(String command, String source) {

        try {
            WordCounter wordCounter = WordCounterFactory.getWordCounter(source);

            switch (command) {
                case "-c" -> System.out.println(wordCounter.getFileSize());

                case "-l" -> System.out.println(wordCounter.getLinesCount());

                case "-w" -> System.out.println(wordCounter.getWordsCount());

                case "-m" -> System.out.println(wordCounter.getCharactersCount());

                case "-all" -> {
                    List<Object> results = wordCounter.getAll();
                    StringBuilder sb = new StringBuilder();
                    for (Object result : results) {
                        sb.append(result).append(" ");
                    }
                    System.out.println(sb.toString().trim());
                }

                default -> System.out.println("Invalid command");
            }
        } catch (IOException e) {
            logger.severe("Error: " + e.getMessage());
        }
    }
}
