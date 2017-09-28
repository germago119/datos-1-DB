package LinkedDB.Lists;

/**
 * This interface sets the template for all the lists,
 * regardless of their type.
 *
 * @param <T>
 * @author Roger Valderrama
 */

public interface List<T extends Comparable<T>> {

    /**
     * This method allows the list to add a certain value in it.
     *
     * @param value new value.
     */

    void append(T value);

    /**
     * This method allows the list to delete all the content in it.
     */

    void deleteAll();

    /**
     * This method allows the list to get the reference of the first {@link Node}
     * .
     *
     * @return null if list is empty, referenced Node otherwise.
     */

    Node<T> getHead();

    /**
     * This method allows the list to know the number of elements inside.
     *
     * @return the quantity of nodes
     */

    int length();

    /**
     * This method allows the list to search a certain value in it.
     *
     * @param value value to search.
     * @return null if value is absent and a {@link Node} if present.
     */

    Node<T> search(T value);

    /**
     * This method allows the list to know if it is empty.
     *
     * @return booelan
     */

    boolean isEmpty();

    /**
     * This method allows the list to delete a certain value in it
     *
     * @param value value to delete
     */

    void delete(T value);


}
