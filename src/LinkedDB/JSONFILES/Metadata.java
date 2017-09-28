package LinkedDB.JSONFILES;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class Metadata implements Comparable<Metadata> {

    private String data;
    private String type;
    private Boolean primary;
    private Boolean required;
    private String defaultValue;
    private String foreignStore = "";
    private String foreignDocument = "";

    public Metadata(boolean primary, String data, String datatype, boolean required,
                    String defaultValue, String foreignStore, String foreignDocument) {
        this.data = data;
        this.type = datatype;
        this.primary = primary;
        this.required = required;
        this.defaultValue = defaultValue;
        this.foreignStore = foreignStore;
        this.foreignDocument = foreignDocument;
    }

    public Metadata() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Boolean getPrimary() {
        if (primary == null) {
            return false;
        }
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    public String getForeignStore() {
        return foreignStore;
    }

    public void setForeignStore(String foreignStore) {
        this.foreignStore = foreignStore;
    }

    public String getForeignDocument() {
        return foreignDocument;
    }

    public void setForeignDocument(String foreignDocument) {
        this.foreignDocument = foreignDocument;
    }

    @Override
    public int compareTo(Metadata comparableData) {
        return 0;
    }

    @SuppressWarnings("unchecked")
    public JSONObject getStructureJSON(JSONObject object) {
        JSONArray foreignArray = new JSONArray();
        JSONArray array = new JSONArray();
        foreignArray.add(foreignStore);
        foreignArray.add(foreignDocument);
        array.add(this.primary);
        array.add(this.type);
        array.add(this.required);
        array.add(this.defaultValue);
        array.add(foreignArray);
        object.put(data, array);
        return object;
    }

    public void readAtributeJSON(File document) {
        try (JsonReader fileReader = new JsonReader(new FileReader(document))) {
            JsonToken token;
            fileReader.beginObject();
            while (fileReader.hasNext()) {
                token = fileReader.peek();
                if (token.equals(JsonToken.END_OBJECT)) {
                    fileReader.endObject();
                    break;
                } else if (token.equals(JsonToken.NAME)) {
                    setData(fileReader.nextName());
                    try {
                        fileReader.beginArray();
                        setPrimary(fileReader.nextBoolean());
                        setType(fileReader.nextString());
                        setRequired(fileReader.nextBoolean());
                        setDefaultValue(fileReader.nextString());
                        fileReader.beginArray();
                        setForeignStore(fileReader.nextString());
                        setForeignDocument(fileReader.nextString());
                        fileReader.endArray();
                        fileReader.endArray();

                    } catch (Exception exception) {
                        System.out.println("ERROR Metadata - readAtributeJSON BADStructure");
                    }
                } else {
                    fileReader.skipValue();
                }
                fileReader.close();
            }
        } catch (FileNotFoundException exception) {
            System.out.println("ERROR Metadata - readAtributeJSON 404");
        } catch (IOException exception) {
            System.out.println("ERROR Metadata - readAtributeJSON");
        }

    }


}