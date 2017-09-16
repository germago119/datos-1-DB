package LinkedDB.Lists;

public class ListDouble<T extends Comparable<T>> implements List<T> {

    private String name;
    private NodeDouble<T> head;
    private int length;

    public ListDouble(String name) {
        setName(name);
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public Node<T> getHead(){
        return this.head;
    }

    @Override
    public void append(T value) {
        if (this.head == null){
            this.head = new NodeDouble<>(value);
            return;
        }
        NodeDouble<T> temp = this.head;
        while (temp.hasNext()){
            temp = temp.getNext();
        }
        NodeDouble<T> temp2 = new NodeDouble<>(value);
        temp.setNext(temp2);
        temp2.setPrev(temp);
    }

    @Override
    public NodeDouble<T> search(T value) {
        NodeDouble<T> temp = this.head;
        while(temp.hasNext()){
            if(temp.getValue().equals(value)){
                break;
            }
            temp = temp.getNext();
        }
        return temp;
    }

    @Override
    public void delete(T value) {
        NodeDouble<T> temp = this.head;
        while(temp != null){
            if(temp.getValue().equals(value)){
                if (temp == this.head){
                    this.head = temp.getNext();
                }
                else{
                    temp.getPrev().setNext(temp.getNext());
                }
                break;

            }
            temp = temp.getNext();
        }

    }

    @Override
    public void print() {
        NodeDouble<T> temp = this.head;
        while (temp != null){
            System.out.println(temp.getValue());
            temp = temp.getNext();
        }
    }
    
    @Override
    public boolean isEmpty(){
        return this.head == null;
    }

    @Override
    public T iterator(int i) {
        if (i > length) {
            return null;
        } else {
            int cont = 0;
            NodeDouble<T> actual = this.head;
            while (cont < i) {
                actual = actual.getNext();
                cont++;
            }
            return actual.getValue();
        }
    }

    @Override
    public int length() {
        return this.length;
    }

    @Override
    public void deleteAll() {
        this.head = null;
    }
}
