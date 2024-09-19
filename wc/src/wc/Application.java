package wc;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Scanner;
import java.util.logging.Logger;

public class Application {

    public static void main(String[] args){

        Logger logger = Logger.getLogger("wc logger");
        logger.info("==== WC Tool ====");

        FileChannel fileChannel = null;

        // TODO read from args instead of scanner
        Scanner scanner = new Scanner(System.in);

        String[] tokens = scanner.nextLine().split("\\s+");

        final String command = tokens[1];
        final String path = tokens[2];


        try {
            FileManager fileManager = new FileManager(path);

            switch(command){
                case "-c" -> System.out.println(fileManager.getFileSize());

                case "-l" -> System.out.println(fileManager.getLinesCount());

                case "-w" -> System.out.println(fileManager.getWordsCount());

                default -> System.out.println("Unknown command: " + command);
            }

        } catch (IOException e) {
            logger.severe("Error operating on file: " + path);
            System.exit(1);
        }

    }
}