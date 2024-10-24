package algo.huffaman;

import algo.CompressionStrategy;
import counter.CharacterCounter;
import lombok.Builder;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanCompressionStrategy implements CompressionStrategy {


    @Builder
    private static class Node implements Comparable<Node> {

        String key;
        long count;
        Node left;
        Node right;

        @Override
        public int compareTo(Node other) {
            return Long.compare(this.count, other.count);
        }
    }

    private record HuffmanCompressionResult(String encodedText, Map<String, String> huffmanCodes) {

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

    }


    @Override
    public String compress(String input) {
        Map<Character, Long> charactersCount = CharacterCounter.count(input);
        PriorityQueue<Node> heap = buildHeap(charactersCount);
        Node root = buildTree(heap);
        Map<String, String> huffmanCodes = generateHuffmanCodes(root);
        String encodedText = generateEncodedText(input, huffmanCodes);
        return new HuffmanCompressionResult(encodedText, huffmanCodes).toString();
    }

    private PriorityQueue<Node> buildHeap(Map<Character, Long> charactersCount) {
        return charactersCount.
                entrySet()
                .stream()
                .map(entry -> Node.builder().key(String.valueOf(entry.getKey())).count(entry.getValue()).build())
                .collect(PriorityQueue::new, PriorityQueue::add, PriorityQueue::addAll);
    }

    private Node buildTree(PriorityQueue<Node> heap) {
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


    private Map<String, String> generateHuffmanCodes(Node root) {

        Map<String, String> huffmanCodes = new HashMap<>();

        generateCodesHelper(root, new StringBuilder(), huffmanCodes);

        return huffmanCodes;
    }

    private void generateCodesHelper(Node node, StringBuilder code, Map<String, String> huffmanCodes) {
        if (node == null) {
            return;
        }

        if (node.left == null && node.right == null) {
            if(code.isEmpty())
                code.append('0');
            huffmanCodes.put(node.key, code.toString());
            return;
        }

        generateCodesHelper(node.left, code.append('0'), huffmanCodes);

        generateCodesHelper(node.right, code.append('1'), huffmanCodes);
    }
    
    private String generateEncodedText(String input, Map<String, String> huffmanCodes) {
        for (Map.Entry<String, String> entry : huffmanCodes.entrySet()) {
            input = input.replace(entry.getKey(), entry.getValue());
        }
        return input;
    }


}
