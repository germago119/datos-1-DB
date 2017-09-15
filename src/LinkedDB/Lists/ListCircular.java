package LinkedDB.Lists;

public class ListCircular<T extends Comparable<T>> implements List<T>{
    
    private String Name;
    private NodeDouble<T> head;
    private int length;

    public ListCircular(String name) {
        setName(name);
    }

    public void setName(String name){
        this.Name = name;
    }
    public String getName(){
        return this.Name;
    }


    @Override
    public void print() {
        NodeDouble<T> temp = this.head;
        Boolean headFlag = false;
        while ((temp != this.head || !headFlag) && temp != null){
            if (!headFlag){
                headFlag = true;
            }
            System.out.println(temp.getValue());
            temp = temp.getNext();
        }

    }

    @Override
    public void append(T value) {
        if (this.head == null){
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
    public NodeDouble<T> search(T value) {
        NodeDouble<T> temp = this.head;
        Boolean headFlag = false;
        while ((temp != this.head || !headFlag) && temp != null){
            if (!headFlag){
                headFlag = true;
            }
            if (temp.getValue().equals(value)){
                return temp;
            }
            temp = temp.getNext();
        }
        return null;
    }

    @Override
    public void delete(T value){
        NodeDouble<T> temp = this.head;
        Boolean headFlag = false;
        while ((temp != this.head || !headFlag) && temp != null){
            if (!headFlag){
                headFlag = true;
            }
            if (temp.getValue().equals(value)){
                if (temp == this.head){
                    if (this.head.getNext() == this.head){
                        this.head = null;
                        break;
                    }
                    else{
                        this.head.getPrev().setNext(this.head.getNext());
                        NodeDouble<T> newHead = this.head.getNext();
                        this.head.getNext().setPrev(this.head.getPrev());
                        this.head = newHead;
                    }
                } else{
                    temp.getPrev().setNext(temp.getNext());
                    temp.getNext().setPrev(temp.getPrev());
                }
            }
            temp = temp.getNext();
        }
    }

    @Override
    public NodeDouble<T> getHead() {
        return this.head;
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
            int conteo = 0;
            NodeDouble<T> temp = this.head;
            while (conteo < i) {
                temp = temp.getNext();
                conteo++;
            }
            System.out.println(temp.getValue());
            return temp.getValue();
        }
    }

    @Override
    public int length() {
        return this.length;
    }
}
