package algo.huffaman;

import java.util.HashMap;
import java.util.Map;

record HuffmanCompressionResult(String encodedText, Map<String, String> huffmanCodes) {

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : huffmanCodes.entrySet()) {
            result.append(String.format("%s:%s%n", entry.getKey(), entry.getValue()));
        }

        result.append(encodedText).append("\n");

        return result.toString();
    }


    static HuffmanCompressionResult fromString(String input) {
        Map<String, String> huffmanCodes = new HashMap<>();
        String encodedText = null;

        String[] lines = input.split("\n");


        for (String line : lines) {
            line = line.trim();

            if (line.contains(":")) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    huffmanCodes.put(parts[0], parts[1]);
                }
            }
            else {
                encodedText = line;
            }
        }

        return new HuffmanCompressionResult(encodedText, huffmanCodes);
    }


}