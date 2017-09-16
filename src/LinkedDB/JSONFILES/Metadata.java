package LinkedDB.JSONFILES;

import LinkedDB.UI.Controller;


public class Metadata {

    private String atributo;
    private String value;
    private String nombre;
    private String defecto;
    private String requiered;
    private String storeName;


    public Metadata(String atributo, String value, String nombre, String defecto, String required, String storeName) {
        this.atributo = atributo;
        this.value = value;
        this.nombre = nombre;
        this.defecto = defecto;
        this.requiered = required;
        this.storeName = storeName;
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

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String Validar() { //todo REVISAR RETURN
        try{
            switch(getAtributo()){
                case "int": Integer.parseInt(getValue());
                break;

                case "float": Float.parseFloat(getValue());
                break;
                //todo case "fecha" PREGUNTAR
                default: Controller.errordialog();

            }

        }catch(Exception e){
            System.out.println("ERROR EN VALIDACION");
        }
        return getValue();
    }

    public static Metadata stringtostore(String value) {
        //String = nombre-valor-atributo-defecto-requiered-storename
        String[] array = value.split("-", 0);
        return new Metadata(array[0], array[1], array[2], array[3], array[4], array[5]);

    }

    public String storetostring(Metadata metadata) {
        String storeString = "";
        storeString += metadata.getNombre();
        storeString += ("-" + metadata.getValue());
        storeString += ("-" + metadata.getAtributo());
        storeString += ("-" + metadata.getDefecto());
        storeString += ("-" + metadata.getRequiered());
        storeString += ("-" + metadata.getStoreName());

        return storeString;
    }
}
