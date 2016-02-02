import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Random;

public class RandomizedQueue<Item> implements Iterable<Item> {
    int capacity = 1;
    Item[] array = (Item[]) new Object[1];
    int size = 0;

    public RandomizedQueue() {
        // construct an empty randomized queue
    }

    public boolean isEmpty() {
        // is the queue empty?
        return size == 0;
    }

    public int size() {
        // return the number of items on the queue
        return size;
    }

    private void increaseCapacity() {
        capacity *= 2;
        Item[] newArray = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            newArray[i] = array[i];
        }
        array = newArray;
    }

    private void decreaseCapacity() {
        capacity /= 2;
        Item[] newArray = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            newArray[i] = array[i];
        }
        array = newArray;
    }

    public void enqueue(Item item) {
        // add the item
        if (item == null) {
            throw new NullPointerException();
        }
        array[size] = item;
        size++;
        if (size > capacity / 2) {
            increaseCapacity();
        }
    }

    private void checkEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
    }

    public Item dequeue() {
        // remove and return a random item
        checkEmpty();
        Random random = new Random(System.currentTimeMillis());
        int i = random.nextInt(size);
        Item item = array[i];
        array[i] = array[--size];
        array[size] = null;
        if (size < capacity / 4) {
            decreaseCapacity();
        }
        return item;
    }

    public Item sample() {
        // return (but do not remove) a random item
        checkEmpty();
        Random random = new Random(System.currentTimeMillis());
        int i = random.nextInt(size);
        return array[i];
    }

    public Iterator<Item> iterator() {
        // return an independent iterator over items in random order
        return new QueueIterator();
    }

    private class QueueIterator implements Iterator<Item> {
        int i = 0;
        int[] order = new int[size];

        public QueueIterator() {
            for (int i = 0; i < size; i++)
                order[i] = i;
            StdRandom.shuffle(order);
        }

        @Override
        public boolean hasNext() {
            return i < size;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return array[order[i++]];
        }
    }

    public static void main(String[] args) {
        // unit testing
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();
        randomizedQueue.enqueue(1);
        randomizedQueue.enqueue(2);
        randomizedQueue.enqueue(3);
        for(int i : randomizedQueue) {
            System.out.println(i+"");
        }
        for(int i : randomizedQueue) {
            System.out.println(i+"");
        }
    }
}