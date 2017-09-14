package LinkedDB.Lists;

public class ListFactory {

    public static List<?> createList(ListTypes type, String name){
        if (type.equals(ListTypes.Simple)){
            return new ListSimple<>(name);
        }else if (type.equals(ListTypes.Double)){
            return new ListDouble<>(name);
        }else{
            return null;
        }
    }

}
