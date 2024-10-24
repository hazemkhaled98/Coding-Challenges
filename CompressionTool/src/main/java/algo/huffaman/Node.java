package algo.huffaman;


import lombok.Builder;

@Builder
class Node implements Comparable<Node>{

    String key;
    long count;
    Node left;
    Node right;

    @Override
    public int compareTo(Node other) {
        return Long.compare(this.count, other.count);
    }
}
