package algo.huffaman;

import java.util.HashMap;
import java.util.Map;

record HuffmanCompressionResult(String encodedText, Map<String, String> huffmanCodes) {

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("### Huffman Codes ###\n");
        result.append("-----------------------\n");
        for (Map.Entry<String, String> entry : huffmanCodes.entrySet()) {
            result.append(String.format("%-10s : %s%n", entry.getKey(), entry.getValue()));
        }
        result.append("-----------------------\n");

        result.append("\n### Encoded Text ###\n");
        result.append(encodedText).append("\n");

        return result.toString();
    }


    static HuffmanCompressionResult fromString(String input) {
        Map<String, String> huffmanCodes = new HashMap<>();
        String encodedText = null;


        input = input.replace("### Huffman Codes ###", "")
                .replace("### Encoded Text ###", "")
                .replace("-----------------------", "");

        String[] lines = input.split("\\n+");


        for (String line : lines) {
            // Check if the line represents encoded text (starts with a '0' or '1')
            if (line.matches("^[01]+$")) {
                encodedText = line;
            }

            else if (line.contains(":")) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    huffmanCodes.put(parts[0].trim(), parts[1].trim());
                }
            }
        }

        return new HuffmanCompressionResult(encodedText, huffmanCodes);
    }


}