package LinkedDB;

public class ListaSimple<T extends Comparable<T>> {
    private Nodo<T> primero;

    public ListaSimple() {
        this.primero = null;
    }

    public boolean isEmpty() {
        return this.primero == null;
    }

    public Nodo<T> getPrimero() {
        return primero;
    }

    public void setPrimero(Nodo<T> primero) {
        this.primero = primero;
    }

    public void insertarFinal(T valor) {
        if (this.primero == null) {
            this.primero = new Nodo<T>(valor);
        } else {
            Nodo<T> actual = this.primero;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(new Nodo<T>(valor));
        }

    }

    public void imprimir() {
        Nodo<T> actual = this.primero;
        while (actual != null) {
            System.out.println(actual.getValor());
            actual = actual.getSiguiente();
        }
    }

    public void insertarInicio(T valor) {
        if (this.primero == null) {
            this.primero = new Nodo<T>(valor);
        } else {
            Nodo<T> nuevo = new Nodo<T>(valor);
            nuevo.setSiguiente(this.primero);
            this.primero = nuevo;
        }
    }

    public Nodo<T> buscar(T buscado) {
        Nodo<T> actual = this.primero;
        while (actual != null) {
            if (actual.getValor().compareTo(buscado) == 0) {
                return actual;
            } else {
                actual = actual.getSiguiente();
            }
        }
        return null;
    }
}

