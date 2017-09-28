package LinkedDB.Lists;

/**
 * This class defines the implementation of a Double Node.
 *
 * @param <T>
 * @author Roger Valderrama
 */

public class NodeDouble<T extends Comparable<T>> extends Node<T> {

    private NodeDouble<T> next;
    private NodeDouble<T> prev;
    private T value;

    public NodeDouble(T value) {
        setValue(value);
        setNext(null);
        setPrev(null);
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public NodeDouble<T> getNext() {
        return next;
    }

    public void setNext(NodeDouble<T> next) {
        this.next = next;
    }

    public NodeDouble<T> getPrev() {
        return prev;
    }

    public void setPrev(NodeDouble<T> prev) {
        this.prev = prev;
    }

    public boolean hasNext() {
        return this.next != null;
    }
}
