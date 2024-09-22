package wc;


import wc.wordcounter.WordCounter;

import java.io.IOException;

enum Command {
    COUNT_BYTES("-c") {
        @Override
        String execute(WordCounter counter) throws IOException {
            return String.valueOf(counter.getFileSize());
        }
    },
    COUNT_LINES("-l") {
        @Override
        String execute(WordCounter counter) throws IOException {
            return String.valueOf(counter.getLinesCount());
        }
    },
    COUNT_WORDS("-w") {
        @Override
        String execute(WordCounter counter) throws IOException {
            return String.valueOf(counter.getWordsCount());
        }
    },
    COUNT_CHARS("-m") {
        @Override
        String execute(WordCounter counter) throws IOException {
            return String.valueOf(counter.getCharactersCount());
        }
    },
    ALL("-all") {
        @Override
        String execute(WordCounter counter) throws IOException {
            var results = counter.getAll();
            return String.join(" ", results.stream().map(Object::toString).toList());
        }
    };

    private final String flag;

    Command(String flag) {
        this.flag = flag;
    }

    abstract String execute(WordCounter counter) throws IOException;

    static Command fromString(String flag) {
        for (Command c : values()) {
            if (c.flag.equals(flag)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Unknown command: " + flag);
    }
}