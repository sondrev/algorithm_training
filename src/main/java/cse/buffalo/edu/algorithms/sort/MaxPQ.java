package cse.buffalo.edu.algorithms.sort;

import java.util.Iterator;
import java.util.Comparator;
import java.util.NoSuchElementException;
import cse.buffalo.edu.algorithms.stdlib.StdIn;
import cse.buffalo.edu.algorithms.stdlib.StdOut;

public class MaxPQ<Key> implements Iterable<Key> {

  private Key[] pq;
  private int N;
  private Comparator<Key> comparator;

  public MaxPQ(int capacity) {
    // Because pq[0] isn't used, we must extend capacity by 1.
    pq = (Key[]) new Object[capacity + 1];
    N = 0;
  }

  // Create an empty priority queue.
  public MaxPQ() {
    this(1);
  }

  // Create an empty priority queue with the given initial capacity,
  // using the given comparator.
  public MaxPQ(int initCapacity, Comparator<Key> comparator) {
    this.comparator = comparator;
    pq = (Key[]) new Object[initCapacity + 1];
    N = 0;
  }

  // Create an empty priority queue using the given comparator.
  public MaxPQ(Comparator<Key> comparator) {
    this(1, comparator);
  }

  // Create a priority queue with the given items.
  public MaxPQ(Key[] keys) {
    N = keys.length;
    pq = (Key[]) new Object[keys.length + 1];
    for (int i = 0; i < N; i++) {
      pq[i+1] = keys[i];
    }
    for (int k = N/2; k >= 1; k--) {
      sink(k);
    }
  }

  private void resize(int capacity) {
    Key[] tmp = (Key[]) new Object[capacity];
    for (int i = 1; i <= N; i++) {
      tmp[i] = pq[i];
    }
    pq = tmp;
  }

  public void insert(Key v) {
    if (N >= pq.length - 1) resize(2 * pq.length);
    pq[++N] = v;
    swim(N);
  }

  public Key delMax() {
    if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
    Key max = pq[1];
    exch(1, N--);
    sink(1);
    pq[N+1] = null; // To avoid loitering

    if ((N > 0) && (N == (pq.length - 1) / 4)) resize(pq.length / 2);

    return max;
  }

  public boolean isEmpty() {
    return N == 0;
  }

  public int size() {
    return N;
  }

  private void sink(int k) {
    // Use 2 * k <= N, not k <= N
    // Because if 2 * k > N, it means this k has no child.
    while (2 * k <= N) {
      int bigChild = 2 * k;

      // Pick the bigger one of two children.
      if (bigChild < N && less(bigChild, bigChild + 1)) bigChild++;

      // Do nothing if the bigger child is smaller than the parent.
      if (!less(k, bigChild)) break;

      exch(k, bigChild);
      k = bigChild;
    }
  }

  private void swim(int k) {
    while (k > 1) {
      if (less(k/2, k)) exch(k, k/2);
      k = k/2;
    }
  }

  private boolean less(int i, int j) {
    if (comparator == null) {
      return ((Comparable<Key>) pq[i]).compareTo(pq[j]) < 0;
    } else {
      return comparator.compare(pq[i], pq[j]) < 0;
    }
  }

  private void exch(int i, int j) {
    Key tmp = pq[i];
    pq[i] = pq[j];
    pq[j] = tmp;
  }

  public Iterator<Key> iterator() {
    return new HeapIterator();
  }

  private class HeapIterator implements Iterator<Key> {

    private MaxPQ<Key> copy;

    public HeapIterator() {
      if (comparator == null) {
        copy = new MaxPQ<Key>(size());
      } else {
        copy = new MaxPQ<Key>(size(), comparator);
      }
      for (int i = 1; i <= N; i++) {
        copy.insert(pq[i]);
      }
    }

    public boolean hasNext() {
      return !copy.isEmpty();
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }

    public Key next() {
      if (!hasNext()) throw new NoSuchElementException();
      return copy.delMax();
    }
  }

  public static void main(String[] args) {
    MaxPQ<String> pq = new MaxPQ<String>();
    while (!StdIn.isEmpty()) {
      String item = StdIn.readString();
      if (!item.equals("-")) pq.insert(item);
      else if (!pq.isEmpty()) StdOut.print(pq.delMax() + " ");
    }
    StdOut.println("(" + pq.size() + " left on pq)");
  }
}
