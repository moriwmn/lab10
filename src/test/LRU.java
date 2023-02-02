package test;

import java.util.HashMap;

public class LRU implements CacheReplacementPolicy{

    class Node {
        String key;
        String value;
        Node prev;
        Node next;
        
        public Node(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
    
    HashMap<String, Node> map;
    Node head;
    Node tail;
    int capacity;

    public LRU() {
        map = new HashMap<>();
        head = null;
        tail = null;
    }

    public void add(String word) {
        if (map.containsKey(word)) {
            Node node = map.get(word);
            removeNode(node);
            setHead(node);
        } else {
            Node node = new Node(word, word);
            setHead(node);
            map.put(word, node);
        }
    }

    public String remove() {
        if (head == null) {
            return null;
        }
        Node node = tail;
        removeNode(node);
        map.remove(node.key);
        return node.value;
    }

    private void removeNode(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
    }

    private void setHead(Node node) {
        node.next = head;
        node.prev = null;
        if (head != null) {
            head.prev = node;
        }
        head = node;
        if (tail == null) {
            tail = head;
        }
    }


}
