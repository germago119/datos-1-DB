package LinkedDB.JSONFILES;

import LinkedDB.ListMain.ListMain;
import LinkedDB.Lists.ListSimple;
import LinkedDB.Lists.Node;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;


public class JSONInstance implements Comparable<JSONInstance> {


    private String store;
    private String document;
    private String name;
    private ListSimple<String> references = new ListSimple<>("REFERENCES");

    private HashMap<String, String> value = new HashMap<>();

    public JSONInstance(String name) {
        this.name = name;
    }

    public HashMap<String, String> getValue() {
        return value;
    }

    public void setValue(HashMap<String, String> value) {
        this.value = value;
    }

    public boolean hasReferences() {
        return references.getHead() != null;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void readJSONInstance(File document, Metadata primary, JsonReader fileReader) {
        try {
            JsonToken token = fileReader.peek();
            fileReader.beginObject();
            while (!token.equals(JsonToken.END_OBJECT)) {
                String name = fileReader.nextName();
                if (name.equals("$REFERENCES$")) {
                    fileReader.beginArray();
                    token = fileReader.peek();
                    while (token != JsonToken.END_ARRAY) {
                        addReference(fileReader.nextString());
                        token = fileReader.peek();
                    }
                    fileReader.endArray();
                    token = fileReader.peek();
                    continue;
                }
                String value = fileReader.nextString();

                if (primary != null && name.equals(primary.getData())) {
                    setName(value);
                }
                addValue(name, value);
                token = fileReader.peek();
            }
            fileReader.endObject();
        } catch (IOException exception) {
            System.out.println("ERROR JsonInstance - writeJSONInstance");
        }

    }

    public void addValue(String key, String value) {
        this.value.put(key, value);
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public void update(String updateAtribute, String newValue, String lookAtribute, String lookValue) {
        Set<String> keySet = this.value.keySet();
        for (String key : keySet) {
            if (key.equals(lookAtribute) && value.get(key).equals(lookValue)) {
                for (String secondKey : keySet) {
                    if (secondKey.equals(updateAtribute)) {
                        value.replace(secondKey, newValue);
                    }
                }
            }
        }
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public void addReference(String reference) {
        references.append(reference);
    }

    public String getDoc() {
        return document;
    }

    @Override
    public int compareTo(JSONInstance comparableInstance) {
        return 0;
    }

    public JSONObject getJSONStructure() {
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        Set<String> keySet = this.value.keySet();
        for (String key : keySet) {
            object.put(key, this.value.get(key));
        }
        Node<String> reference = references.getHead();
        while (reference != null) {
            array.add(reference.getValue());
            reference = reference.getNext();
        }
        object.put("$REFERENCES$", array);
        return object;
    }

    public String findValue(String atribute) {
        return value.get(atribute);
    }

    public void updateReference() {
        Node<String> current = references.getHead();
        while (current != null) {
            String[] foreign = current.getValue().split("/");
            if (ListMain.findInstance(foreign[2], foreign[1], foreign[0]) == null) {
                references.delete(current.getValue());
            }
            current = current.getNext();
        }
    }

}

