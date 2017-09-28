package LinkedDB.Lists;

/**
 * This class defines the implementation of a Double Linked List.
 *
 * @param <T>
 * @author Roger Valderrama
 */


public class ListDouble<T extends Comparable<T>> implements List<T> {

    private String name;
    private NodeDouble<T> head;

    public ListDouble(String name) {
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
            this.head = new NodeDouble<>(value);
            return;
        }
        NodeDouble<T> temp = this.head;
        while (temp.hasNext()) {
            temp = temp.getNext();
        }
        NodeDouble<T> temp2 = new NodeDouble<>(value);
        temp.setNext(temp2);
        temp2.setPrev(temp);
    }

    @Override
    public int length() {
        NodeDouble<T> current = head;
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
        NodeDouble<T> temp = this.head;
        while (temp != null) {
            if (temp.getValue().equals(value)) {
                if (temp == this.head) {
                    this.head = temp.getNext();
                } else {
                    temp.getPrev().setNext(temp.getNext());
                }
                break;

            }
            temp = temp.getNext();
        }

    }


    @Override
    public boolean isEmpty() {
        return this.head == null;
    }


    public Node<T> getHead() {
        return this.head;
    }

    @Override
    public NodeDouble<T> search(T value) {
        NodeDouble<T> temp = this.head;
        while (temp.hasNext()) {
            if (temp.getValue().equals(value)) {
                break;
            }
            temp = temp.getNext();
        }
        return temp;
    }
}
