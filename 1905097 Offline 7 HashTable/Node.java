public class Node{
    public Pair pair;
    private Node next;

    Node(){
        this.pair = new Pair("", -1);
        this.next = null;
    }

    Node(Pair pair) {
        this.pair = pair;
        this.next = null;
    }

    Node(Pair pair, Node next) {
        this.pair = pair;
        this.next = next;
    }

    Node(Node next) {
        this.next = next;
    }

    public Node next() {
        return next;
    }

    public Node setNext(Node next) {
        return this.next = next;
    }


}