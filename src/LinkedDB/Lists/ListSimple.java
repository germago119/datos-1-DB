package LinkedDB.Lists;

public class ListSimple<T extends Comparable<T>> implements List<T> {

    private NodeSimple<T> head;
    private String name;
    private int length;

    public ListSimple(String name) {
        setName(name);
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public NodeSimple<T> getHead(){
        return this.head;
    }

    @Override
    public void append (T value) {
        if (this.head == null){
            this.head = new NodeSimple<>(value);
            return;
        }
        NodeSimple<T> temp = this.head;
        while (temp.hasNext()){
            temp = temp.getNext();
        }
        temp.setNext(new NodeSimple<T>(value));
    }

    @Override
    public NodeSimple<T> search(T value) {
        NodeSimple<T> temp = this.head;
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
        NodeSimple<T> temp = this.head;
        NodeSimple<T> prev = null;
        while(temp != null){
            if(temp.getValue().equals(value)){
                if (temp == this.head){
                    this.head = temp.getNext();
                }
                else{
                    prev.setNext(temp.getNext());
                }
                break;
            }
            prev = temp;
            temp = temp.getNext();
        }
    }

    @Override
    public void print() {
        NodeSimple<T> temp = this.head;
        while (temp != null){
            System.out.println(temp.getValue());
            temp = temp.getNext();
        }
    }

    @Override
    public T iterator(int i){
        if (i > length) {
            return null;
        } else {
            int conteo = 0;
            NodeSimple<T> temp = this.head;
            while (conteo < i) {
                temp = temp.getNext();
                conteo++;
            }
            return temp.getValue();
        }
    }

    @Override
    public int length() {
        return this.length;
    }

    @Override
    public boolean isEmpty(){
        return this.head == null;
    }

}

