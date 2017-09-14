package LinkedDB.Lists;

public class NodeDouble<T extends Comparable<T>> extends Node<T> {

    private T value;
    private NodeDouble<T> next;
    private NodeDouble<T> prev;

    public NodeDouble(T value){
        setValue(value);
        setPrev(null);
        setNext(null);
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

    public boolean hasNext(){
        return this.next != null;
    }
}
