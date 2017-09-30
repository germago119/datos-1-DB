package LinkedDB.JSONFILES;

import LinkedDB.ListMain.ListUI;
import LinkedDB.Lists.List;
import LinkedDB.Lists.ListSimple;
import LinkedDB.Lists.Node;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class JSONDocument implements Comparable<JSONDocument> {

    private String name;
    private String store;
    private ListSimple<Metadata> attributes;
    private ListSimple<JSONInstance> instances;
    private ListSimple<String> references = new ListSimple<>("REFERENCES");

    public JSONDocument(String name) {
        this.name = name;
        this.attributes = new ListSimple<>("Atributes");
        this.instances = new ListSimple<>("Instances");
    }

    public void addInstance(JSONInstance instance) {
        this.instances.append(instance);
    }

    public void addAtributes(Metadata data) {
        this.attributes.append(data);
    }

    public List<Metadata> getAttributes() {
        return this.attributes;
    }

    public void setAttributes(ListSimple<Metadata> attributes) {
        this.attributes = attributes;
    }

    public void setAtributes(boolean primary, String data, String dataType, boolean required, String defaultValue,
                             String foreignStore, String foreignDocument) {
        Metadata metadata = new Metadata(primary, data, dataType, required, defaultValue, foreignStore, foreignDocument);
        addAtributes(metadata);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<JSONInstance> getInstances() {
        return instances;
    }

    public void setInstances(ListSimple<JSONInstance> instances) {
        this.instances = instances;
    }

    public String getStore() {
        return this.store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public Metadata findPrimary() {
        Node<Metadata> currentNode = attributes.getHead();
        while (currentNode != null) {
            Metadata currentData = currentNode.getValue();
            if (currentData.getPrimary()) {
                return currentData;
            }
            currentNode = currentNode.getNext();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public void writeInstanceToFile(File file) {
        Node<JSONInstance> temp = this.instances.getHead();
        JSONArray array = new JSONArray();
        while (temp != null) {
            JSONInstance jsonInstance = temp.getValue();
            array.add(jsonInstance.getJSONStructure());
            temp = temp.getNext();
        }
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(array.toJSONString());
            writer.close();
        } catch (IOException exception) {
            System.out.println("ERROR JSONDocument - writeInstancetoFile");
        }
    }

    @SuppressWarnings("unchecked")
    public void writeAttributesToFile(File file) {
        Node<Metadata> temp = this.attributes.getHead();
        JSONObject jsonObject = new JSONObject();
        while (temp != null) {
            Metadata tempData = temp.getValue();
            jsonObject = tempData.getStructureJSON(jsonObject);
            temp = temp.getNext();
        }
        JSONArray referencesArray = new JSONArray();
        Node<String> current = references.getHead();
        while (current != null) {
            referencesArray.add(current.getValue());
            current = current.getNext();
            jsonObject.put("$REFERENCES$", referencesArray);
        }
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(jsonObject.toJSONString());
            writer.close();
        } catch (IOException exception) {
            System.out.println("ERROR JSONDocument - writeAttributesToFile");
        }
    }

    @Override
    public int compareTo(JSONDocument comparableDocument) {
        return 0;
    }

    public void readInstances(File document, String documentName, String storeName) {
        try (JsonReader fileReader = new JsonReader(new FileReader(document))) {
            JsonToken token;
            fileReader.beginArray();
            while (fileReader.hasNext()) {
                token = fileReader.peek();
                if (token.equals(JsonToken.END_ARRAY)) {
                    fileReader.endArray();
                    break;
                } else if (token.equals(JsonToken.BEGIN_OBJECT)) {
                    JSONInstance instance = new JSONInstance("");
                    instance.readJSONInstance(document, findPrimary(), fileReader);
                    instance.setDocument(documentName);
                    instance.setStore(storeName);
                    addInstance(instance);
                } else {
                    fileReader.skipValue();
                }
            }
            fileReader.close();
        } catch (IOException exception) {
            System.out.println("ERROR JsonDocument - readInstances");
        }
    }

    public boolean[] deletePrimaryKey(String primaryKey) {
        Node<JSONInstance> instance = instances.getHead();
        boolean[] deleted = {false, false};
        updateReferences();
        while (instance != null) {
            if (instance.getValue().getName().equals(primaryKey)) {
                if (!instance.getValue().hasReferences()) {
                    instances.delete(instance.getValue());
                    deleted[1] = true;
                } else {
                    Alert invalidPath = new Alert(AlertType.ERROR);
                    invalidPath.setTitle("ERROR");
                    invalidPath.setHeaderText("Referenced Object");
                    invalidPath.setContentText("The object " + instance.getValue().getName() + " can't be deleted since it is referenced by another object");
                    invalidPath.showAndWait();
                    deleted[0] = true;
                }
                break;
            }
            instance = instance.getNext();
        }
        return deleted;
    }

    public ObservableList<JSONInstance> searchByAttribute(String value, String atribute) {
        Node<JSONInstance> instance = instances.getHead();
        ObservableList<JSONInstance> result = FXCollections.observableArrayList();

        while (instance != null) {
            HashMap<String, String> map = instance.getValue().getValue();
            if ((atribute == null && map.containsValue(value)) || (map.containsKey(atribute) && map.get(atribute).equals(value))) {
                result.add(instance.getValue());
            }
            instance = instance.getNext();
        }
        return result;
    }

    public JSONInstance findInstance(String Name) {
        Node<JSONInstance> current = instances.getHead();
        while (current != null) {
            JSONInstance instance = current.getValue();
            if (instance.getName().equals(Name)) {
                return instance;
            }
            current = current.getNext();
        }
        return null;
    }

    public void addReference(String reference) {
        references.append(reference);
    }

    public Metadata findAttribute(String atribute) {
        Node<Metadata> currentAtribute = attributes.getHead();
        while (currentAtribute != null) {
            if (currentAtribute.getValue().getData().equals(atribute)) {
                return currentAtribute.getValue();
            }
            currentAtribute = currentAtribute.getNext();
        }
        return null;
    }

    public void readAttributes(File document) {
        try (JsonReader fileReader = new JsonReader(new FileReader(document))) {
            JsonToken token;
            fileReader.beginObject();
            while (fileReader.hasNext()) {
                Metadata data = new Metadata();
                token = fileReader.peek();
                if (token.equals(JsonToken.END_OBJECT)) {
                    fileReader.endObject();
                    break;
                } else if (token.equals(JsonToken.NAME)) {
                    String nextName = fileReader.nextName();
                    try {
                        if (nextName.equals("$REFERENCES$")) {
                            fileReader.beginArray();
                            token = fileReader.peek();
                            while (token != JsonToken.END_ARRAY) {
                                String nextString = fileReader.nextString();
                                addReference(nextString);
                                token = fileReader.peek();
                            }
                            fileReader.endArray();
                            token = fileReader.peek();
                        } else {
                            data.setData(nextName);
                            fileReader.beginArray();
                            data.setPrimary(fileReader.nextBoolean());
                            data.setType(fileReader.nextString());
                            data.setRequired(fileReader.nextBoolean());
                            data.setDefaultValue(fileReader.nextString());
                            fileReader.beginArray();
                            data.setForeignStore(fileReader.nextString());
                            data.setForeignDocument(fileReader.nextString());
                            fileReader.endArray();
                            fileReader.endArray();
                            addAtributes(data);
                        }
                    } catch (Exception exception) {
                        System.out.println("ERROR JsonDocument - readAttributes");
                    }
                } else {
                    fileReader.skipValue();
                }
            }
            fileReader.close();
        } catch (IOException exception) {
            System.out.println("ERROR JsonDocument - readAttributes");
        }
    }

    public void updateReferences() {
        Node<String> reference = references.getHead();
        while (reference != null) {
            String[] foreign = reference.getValue().split("/");
            if (ListUI.findDocument(foreign[1], foreign[0]) == null) {
                references.delete(reference.getValue());
            }
            reference = reference.getNext();
        }
        Node<JSONInstance> instance = instances.getHead();
        while (instance != null) {
            instance.getValue().updateReference();
            instance = instance.getNext();
        }


    }

    public void updateAttribute(String updateAtribute, String newValue, String lookAtribute, String lookValue) {
        if (findPrimary().getData().equals(updateAtribute)) {
            Alert invalidPath = new Alert(AlertType.ERROR);
            invalidPath.setTitle("ERROR");
            invalidPath.setHeaderText("Primary Key");
            invalidPath.setContentText("You can't update a primary key since there can't be two objects with the same primary key");
            invalidPath.showAndWait();
            return;
        }
        Node<JSONInstance> current = instances.getHead();
        while (current != null) {
            current.getValue().update(updateAtribute, newValue, lookAtribute, lookValue);
            current = current.getNext();
        }
    }

    public ListSimple<String> getAttributesNames() {
        Node<Metadata> metaData = attributes.getHead();
        ListSimple<String> attribute = new ListSimple<>("Atributes");

        while (metaData != null) {
            attribute.append(metaData.getValue().getData());
            metaData = metaData.getNext();
        }

        return attribute;
    }

    public boolean hasReferences() {
        return references.getHead() != null;
    }
}
