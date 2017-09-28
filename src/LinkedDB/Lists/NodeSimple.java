package LinkedDB.Lists;

public class NodeSimple<T extends Comparable<T>> extends Node<T> {

    private NodeSimple<T> next;
    private T value;

    public NodeSimple(T value) {
        setValue(value);
        setNext(null);
    }

    public NodeSimple<T> getNext() {
        return next;
    }

    public void setNext(NodeSimple<T> next) {
        this.next = next;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public boolean hasNext() {
        return this.next != null;
    }
}





