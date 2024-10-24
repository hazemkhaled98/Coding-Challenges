package algo.huffaman;

import algo.CompressionStrategy;
import counter.CharacterCounter;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class HuffmanCompressionStrategy implements CompressionStrategy {


    @Override
    public String compress(String input) {
        Map<Character, Long> charactersCount = CharacterCounter.count(input);
        PriorityQueue<Node> heap = buildHeap(charactersCount);
        Node root = buildTree(heap);
        Map<String, String> huffmanCodes = generateHuffmanCodes(root);
        String encodedText = generateEncodedText(input, huffmanCodes);
        return new HuffmanCompressionResult(encodedText, huffmanCodes).toString();
    }

    @Override
    public String decompress(String compressedText) {
        HuffmanCompressionResult compressionResult = HuffmanCompressionResult.fromString(compressedText);
        String encodedText = compressionResult.encodedText();
        Map<String, String> huffmanCodes = compressionResult.huffmanCodes();
        return generateOriginalText(encodedText, huffmanCodes);
    }

    private static PriorityQueue<Node> buildHeap(Map<Character, Long> charactersCount) {
        return charactersCount.
                entrySet()
                .stream()
                .map(entry -> Node.builder().key(String.valueOf(entry.getKey())).count(entry.getValue()).build())
                .collect(PriorityQueue::new, PriorityQueue::add, PriorityQueue::addAll);
    }

    private static Node buildTree(PriorityQueue<Node> heap) {
        while (heap.size() > 1) {
            Node minNode1 = heap.poll();
            Node minNode2 = heap.poll();
            Node newNode = Node.builder()
                    .key(minNode1.key + minNode2.key)
                    .count(minNode1.count + minNode2.count)
                    .left(minNode1)
                    .right(minNode2)
                    .build();
            heap.offer(newNode);
        }
        return heap.poll();
    }


    private static Map<String, String> generateHuffmanCodes(Node root) {

        Map<String, String> huffmanCodes = new HashMap<>();

        generateCodesHelper(root, new StringBuilder(), huffmanCodes);

        return huffmanCodes;
    }

    private static void generateCodesHelper(Node node, StringBuilder code, Map<String, String> huffmanCodes) {
        if (node == null) {
            return;
        }

        if (node.left == null && node.right == null) {
            if (code.isEmpty())
                code.append('0');
            huffmanCodes.put(node.key, code.toString());
            return;
        }

        generateCodesHelper(node.left, code.append('0'), huffmanCodes);

        generateCodesHelper(node.right, code.append('1'), huffmanCodes);
    }

    private static String generateEncodedText(String input, Map<String, String> huffmanCodes) {
        for (Map.Entry<String, String> entry : huffmanCodes.entrySet()) {
            input = input.replace(entry.getKey(), entry.getValue());
        }
        return input;
    }

    private static String generateOriginalText(String encodedText, Map<String, String> huffmanCodes) {
        Map<String, String> reversedHuffmanCodes = reverseHuffmanCodes(huffmanCodes);


        for(var entry : reversedHuffmanCodes.entrySet()) {
            encodedText = encodedText.replace(entry.getKey(), entry.getValue());
        }

        return encodedText;
    }

    private static Map<String, String> reverseHuffmanCodes(Map<String, String> huffmanCodes) {
        return huffmanCodes
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Entry::getValue,
                        Entry::getKey,
                        (e1, e2) -> e1,
                        () -> new TreeMap<>((a, b) -> Integer.compare(b.length(), a.length()))
                ));
    }


}
