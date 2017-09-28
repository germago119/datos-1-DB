package LinkedDB.Lists;

/**
 * This class defines the implementation of a Simple Linked List.
 *
 * @param <T>
 * @author Roger Valderrama
 */

public class ListSimple<T extends Comparable<T>> implements List<T> {

    private NodeSimple<T> head;
    private String name;

    public ListSimple(String name) {
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
        if (this.head == null) {
            this.head = new NodeSimple<>(value);
            return;
        }
        NodeSimple<T> temp = this.head;
        while (temp.hasNext()) {
            temp = temp.getNext();
        }
        temp.setNext(new NodeSimple<T>(value));
    }

    @Override
    public int length() {
        NodeSimple<T> current = head;
        int result = 0;
        while (current != null) {
            result++;
            current = current.getNext();
        }
        return result;
    }

    @Override
    public void deleteAll() {
        this.head = null;
    }

    @Override
    public void delete(T value) {
        NodeSimple<T> temp = this.head;
        NodeSimple<T> prev = null;
        while (temp != null) {
            if (temp.getValue().equals(value)) {
                if (temp == this.head) {
                    this.head = temp.getNext();
                } else {
                    prev.setNext(temp.getNext());
                }
                break;
            }
            prev = temp;
            temp = temp.getNext();
        }
    }


    @Override
    public boolean isEmpty() {
        return this.head == null;
    }


    public NodeSimple<T> getHead() {
        return this.head;
    }

    @Override
    public NodeSimple<T> search(T value) {
        NodeSimple<T> temp = this.head;
        while (temp.hasNext()) {
            if (temp.getValue().equals(value)) {
                break;
            }
            temp = temp.getNext();
        }
        return temp;
    }
}

