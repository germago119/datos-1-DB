package LinkedDB.Lists;

/**
 * This class defines the implementation of a Double Circular List.
 *
 * @param <T>
 * @author Roger Valderrama
 */

public class ListCircular<T extends Comparable<T>> implements List<T> {

    private String name;
    private NodeDouble<T> head;

    public ListCircular(String name) {
        setName(name);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void append(T value) {
        if (isEmpty()) {
            this.head = new NodeDouble<>(value);
            this.head.setNext(this.head);
            this.head.setPrev(this.head);
            return;
        }
        NodeDouble<T> temp = this.head;
        NodeDouble<T> temp2 = new NodeDouble<>(value);
        temp.getNext().setPrev(temp2);
        temp2.setNext(temp.getNext());
        temp2.setPrev(temp);
        temp.setNext(temp2);

    }

    @Override
    public int length() {
        NodeDouble<T> current = head;
        int result = 0;
        if (isEmpty()) {
            return result;
        }
        do {
            result++;
            current = current.getNext();
        } while (current != null);

        return result;
    }

    @Override
    public void deleteAll() {
        this.head = null;
    }

    @Override
    public void delete(T value) {
        NodeDouble<T> temp = this.head;
        if (isEmpty()) {
            return;
        }
        do {
            if (temp.getValue().equals(value)) {
                if (temp == this.head) {
                    if (this.head.getNext() == this.head) {
                        this.head = null;
                        break;
                    } else {
                        this.head.getPrev().setNext(this.head.getNext());
                        NodeDouble<T> newHead = this.head.getNext();
                        this.head.getNext().setPrev(this.head.getPrev());
                        this.head = newHead;

                    }
                } else {
                    temp.getPrev().setNext(temp.getNext());
                    temp.getNext().setPrev(temp.getPrev());
                }
            }
            temp = temp.getNext();
        } while (temp != this.head);
    }

    @Override
    public boolean isEmpty() {
        return this.head == null;
    }


    @Override
    public NodeDouble<T> getHead() {
        return this.head;
    }

    @Override
    public NodeDouble<T> search(T value) {
        NodeDouble<T> temp = this.head;
        if (isEmpty()) {
            return null;
        }
        do {
            if (temp.getValue().equals(value)) {
                return temp;
            }
            temp = temp.getNext();
        }
        while (temp != this.head);

        return null;
    }


}

