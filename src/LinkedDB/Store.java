package LinkedDB;

import com.google.gson.Gson;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Store {

    private String atributo;
    private String value;
    private String nombre;
    private String defecto;
    private String requiered;
    private String StoreName;

    final String currentdirectory = System.getProperty("user.dir");

    Gson gson = new Gson();

    Path path = Paths.get(currentdirectory);

    Boolean X = Files.exists(path);

    public Store(String atributo, String value, String nombre, String defecto, String required){
        this.atributo = atributo;
        this.value = value;
        this.nombre = nombre;
        this.defecto = defecto;
        this.requiered = required;
    }

    public String getCurrentdirectory() {
        return currentdirectory;
    }

    public String getAtributo() {
        return atributo;
    }

    public void setAtributo(String atributo) {
        this.atributo = atributo;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDefecto() {
        return defecto;
    }

    public void setDefecto(String defecto) {
        this.defecto = defecto;
    }

    public String getRequiered() {
        return requiered;
    }

    public void setRequiered(String requiered) {
        this.requiered = requiered;
    }


    private String Validar(){ //todo REVISAR RETURN
        try{
            switch(getAtributo()){
                case "int": Integer.parseInt(getValue());
                break;

                case "float": Float.parseFloat(getValue());
                break;
                //case "fecha" PREGUNTAR
                default: Controller.errordialog();

            }

        }catch(Exception e){
            System.out.println("ERROR EN VALIDACION");
        }
        return getValue();
    }

    public static Store stringtostore(String value) {
        //String = nombre-valor-atributo-defecto-requiered
        String[] array = value.split("-", 0);
        return new Store(array[0], array[1], array[2], array[3], array[4]);

    }
//    public static String storetostring(Store store){
//        return
//    }
}
