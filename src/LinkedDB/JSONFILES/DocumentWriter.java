package LinkedDB.JSONFILES;

import LinkedDB.Lists.List;
import LinkedDB.Lists.Node;

import java.io.File;
import java.io.IOException;

public class DocumentWriter {


    public static void write(List<JSONStore> Main, String path) throws IOException {
        Node<JSONStore> currentNode = Main.getHead();
        Files.emptyFolder(path, false);


        if (Main.isEmpty()) {
            return;
        }
        do {
            JSONStore currentStore = currentNode.getValue();
            String storePath = Files.generateFolder(path, currentStore.getName());
            writeDocumentInStore(storePath, currentStore);
            currentNode = currentNode.getNext();
        } while (currentNode != Main.getHead());

    }

    public static void writeDocumentInStore(String storePath, JSONStore store) throws IOException {
        String metaDataPath = Files.generateFolder(storePath, "MetaData");
        String instancesPath = Files.generateFolder(storePath, "Instances");
        Node<JSONDocument> currentDoc = store.getDocuments().getHead();
        while (currentDoc != null) {
            File docData = Files.generateFile(metaDataPath, currentDoc.getValue().getName());
            File docInstances = Files.generateFile(instancesPath, currentDoc.getValue().getName());
            currentDoc.getValue().writeInstanceToFile(docInstances);
            currentDoc.getValue().writeAttributesToFile(docData);
            currentDoc = currentDoc.getNext();
        }
    }
}

