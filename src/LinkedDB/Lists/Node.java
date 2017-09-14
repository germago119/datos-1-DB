package LinkedDB.Lists;

public abstract class Node<T extends Comparable<T>> {
    Node<T> next;
    T value;
    public abstract boolean hasNext();
    public abstract T getValue();
    public abstract Node<T> getNext();
}
