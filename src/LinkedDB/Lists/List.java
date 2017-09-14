package LinkedDB.Lists;

public interface List <T extends Comparable<T>> {

    public void print();

    public void append(T value);

    public Node<T> search(T value);

    public void delete(T value);

    public Node<T> getHead();

    public T iterator(int i);

    public int length();
}
