package LinkedDB.JSONFILES;

import LinkedDB.Lists.List;
import LinkedDB.Lists.ListDouble;
import LinkedDB.Lists.Node;

public class JSONStore implements Comparable<JSONStore> {

    private String name;
    private List<JSONDocument> documents;

    public JSONStore(String name) {
        setName(name);
        this.documents = new ListDouble<>(this.name + "documents");
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<JSONDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(List<JSONDocument> documents) {
        this.documents = documents;
    }

    public JSONDocument findDocument(String name) {
        Node<JSONDocument> temp = this.documents.getHead();
        JSONDocument result = null;
        while (temp != null) {
            if (temp.getValue() != null) {
                JSONDocument tempDoc = temp.getValue();
                if (tempDoc.getName().equals(name)) {
                    result = tempDoc;
                }
            }
            if (!temp.hasNext()) {
                break;
            }
            temp = temp.getNext();
        }
        return result;
    }

    @Override
    public int compareTo(JSONStore o) {
        return 0;
    }

    public void appendDocument(JSONDocument document) {
        this.documents.append(document);
    }
}
