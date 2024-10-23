/* *****************************************************************************
 *  Name: Yun Zhu
 *  Date: August 19, 2024
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queue;
    private int size;

    // construct an empty randomized queue
    public RandomizedQueue() {
        queue = (Item[]) new Object[8];
        size = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    private void resize(int capacity) {
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            temp[i] = queue[i];
        }
        queue = temp;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("Cannot add null item.");
        if (size == queue.length) resize(2 * queue.length);
        queue[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue is empty.");
        int randomIndex = StdRandom.uniformInt(size);
        Item item = queue[randomIndex];
        queue[randomIndex] = queue[size - 1];
        queue[size - 1] = null;
        size--;
        if (size > 0 && size == queue.length / 4) resize(queue.length / 2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Queue is empty.");
        int randomIndex = StdRandom.uniformInt(size);
        return queue[randomIndex];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private final Item[] copy;
        private int count;

        public RandomizedQueueIterator() {
            copy = (Item[]) new Object[size];
            for (int i = 0; i < size; i++) {
                copy[i] = queue[i];
            }
            StdRandom.shuffle(copy);
            count = 0;
        }

        @Override
        public boolean hasNext() {
            return count < copy.length;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return copy[count++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        System.out.println("Is queue empty? " + rq.isEmpty());
        rq.enqueue(1);
        rq.enqueue(2);
        rq.enqueue(3);
        rq.enqueue(4);
        rq.enqueue(5);
        System.out.println("Queue size after enqueues: " + rq.size());
        System.out.println("Sampled item: " + rq.sample());
        System.out.println("Dequeued item: " + rq.dequeue());
        System.out.println("Queue size after dequeue: " + rq.size());
        System.out.println("Is queue empty? " + rq.isEmpty());

        for (int item : rq) {
            System.out.println("Iterating item: " + item);
        }
    }
}
