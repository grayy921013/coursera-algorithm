import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int size = 0;
    private Node first = null;
    private Node last = null;

    private class Node {
        public Item item;
        public Node next;
        public Node previous;

        public Node(Item item) {
            this.item = item;
            this.next = null;
            this.previous = null;
        }
    }

    public Deque() {
        // construct an empty deque
    }

    public boolean isEmpty() {
        // is the deque empty?
        return size == 0;
    }

    public int size() {
        // return the number of items on the deque
        return size;
    }

    private void checkNull(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
    }

    public void addFirst(Item item) {
        // add the item to the front
        checkNull(item);
        Node node = new Node(item);
        Node oldFirst = first;
        first = node;
        first.next = oldFirst;
        if (oldFirst != null) {
            oldFirst.previous = first;
        }
        if (last == null) {
            last = first;
        }
        size++;
    }

    public void addLast(Item item) {
        // add the item to the end
        checkNull(item);
        Node node = new Node(item);
        if (last != null) {
            last.next = node;
            node.previous = last;
            last = node;
        } else {
            first = node;
            last = node;
        }
        size++;
    }

    public Item removeFirst() {
        // remove and return the item from the front
        if (first == null) {
            throw new NoSuchElementException();
        } else if (first.next != null) {
            Node node = first;
            first = first.next;
            first.previous = null;
            size--;
            return node.item;
        } else {
            Node node = first;
            first = null;
            last = null;
            size--;
            return node.item;
        }
    }

    public Item removeLast() {
        // remove and return the item from the end
        if (last == null) {
            throw new NoSuchElementException();
        }
        if (last.previous != null) {
            Node node = last;
            last = last.previous;
            last.next = null;
            size--;
            return node.item;
        } else {
            Node node = last;
            first = null;
            last = null;
            size--;
            return node.item;
        }
    }

    public Iterator<Item> iterator() {
        // return an iterator over items in order from front to end
        return new Iterator<Item>() {
            Node ptr = first;

            @Override
            public boolean hasNext() {
                return ptr != null;
            }

            @Override
            public Item next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Node node = ptr;
                ptr = ptr.next;
                return node.item;
            }
        };
    }

    public static void main(String[] args) {
        // unit testing
        Deque<Integer> deque = new Deque<>();
        deque.addLast(2);
        deque.removeLast();
        deque.addFirst(1);
        deque.removeLast();
        for (Integer integer : deque) {
            System.out.println(integer + "");
        }
    }
}