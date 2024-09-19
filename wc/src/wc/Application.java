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
            fileChannel = FileManager.openFile(path);
        } catch (IOException e) {
            logger.severe("Error opening file: " + path);
            System.exit(1);
        }

        switch(command){
            case "-c" -> {
                try {
                    System.out.println(fileChannel.size());
                } catch (IOException e) {
                    logger.severe("File closed unexpectedly. Path:  " + path);
                    System.exit(1);
                }
            }

            default -> System.out.println("Unknown command: " + command);
        }

    }
}