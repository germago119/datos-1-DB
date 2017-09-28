package LinkedDB.JSONFILES;

import LinkedDB.Lists.List;

import java.io.File;

public class DocumentReader {

    public static void read(List<JSONStore> readingList, String mainPath) {
        File[] stores = new File(mainPath).listFiles();
        if (stores == null) {
            return;
        }
        for (File tempFile : stores) {
            JSONStore tempStore = new JSONStore(tempFile.getName());
            readDocumentsInStore(tempFile.getPath(), tempStore);
            readingList.append(tempStore);
        }
    }

    public static void readDocumentsInStore(String storePath, JSONStore store) {
        File[] subStore = new File(storePath).listFiles();
        boolean documentsExist = false;
        int i = 0;
        while (true) {
            if (i > 1) {
                i = 0;
            }

            if (subStore[i].getName().equalsIgnoreCase("MetaData")) {
                documentsExist = true;
                File[] documentStructure = new File(subStore[i].getPath()).listFiles();
                for (File aDocumentStructure : documentStructure) {

                    String docName = aDocumentStructure.getName();
                    docName = docName.substring(0, docName.length() - 5);

                    JSONDocument document = new JSONDocument(docName);
                    document.setStore(store.getName());
                    store.appendDocument(document);
                    document.readAtributes(aDocumentStructure);

                }

            } else if (documentsExist) {
                File[] documentInstances = new File(subStore[i].getPath()).listFiles();
                for (File documentInstance : documentInstances) {

                    String docName = documentInstance.getName();
                    docName = docName.substring(0, docName.length() - 5);
                    JSONDocument document = store.findDocument(docName);
                    document.readInstances(documentInstance, docName, store.getName());

                }
                break;
            }
            i++;
        }
    }

}
