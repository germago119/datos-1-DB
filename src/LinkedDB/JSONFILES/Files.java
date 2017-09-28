package LinkedDB.JSONFILES;

import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Files {

    @Nullable
    public static String readPath() {
        Path currentRelativePath = Paths.get("");
        String currentPath = currentRelativePath.toAbsolutePath().toString();
        String finalPath = currentPath + "LinkedDB.txt";
        File file = new File(finalPath);
        try (BufferedReader reader = new BufferedReader(new FileReader(file));) {
            String path = reader.readLine();
            reader.close();
            return path;
        } catch (IOException exception) {

            System.out.println("ERROR Files - readPath");

            return null;


        }
    }

    public static File generateFile(String path, String fileName) {

        String newPath = Paths.get(path, fileName + ".JSON").toString();

        File file = new File(newPath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else {
            System.out.println("File already exists");
        }
        return file;
    }

    public static void emptyFolder(String folderPath, Boolean isMainFolder) {
        File tempFolder = new File(folderPath);
        if (tempFolder.listFiles() == null) {
            return;
        }
        for (File inFile : tempFolder.listFiles()) {
            if (inFile.isDirectory()) {
                emptyFolder(inFile.getAbsolutePath(), false);
            } else {
                inFile.delete();
            }
        }
        if (!isMainFolder) {
            tempFolder.delete();
        }
    }

    public static void writePath(String path) {
        Path currentRelativePath = Paths.get("");
        String currentPath = currentRelativePath.toAbsolutePath().toString();
        String finalPath = currentPath + "LinkedDB.txt";
        File file = new File(finalPath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException exception) {
                System.out.println("ERROR Files - writePath");
            }
        }
        try (FileWriter writer = new FileWriter(file);) {
            writer.write(path);
            writer.close();
        } catch (IOException exception) {
            System.out.println("ERROR Files - writePath");
        }
    }

    public static void deleteFiles(File MainRoute) {
        File[] files = MainRoute.listFiles();
        for (File tempFile : files) {
            if (tempFile.isDirectory()) {
                deleteFiles(tempFile);
            } else {
                tempFile.delete();
            }
        }
    }

    public static String generateFolder(String path, String folderName) {
        String newPath = Paths.get(path, folderName).toString();
        File file = new File(newPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return newPath;
    }

    public static void deleteFolder(File Folder) {
        File[] files = Folder.listFiles();
        for (File tempFile : files) {
            if (tempFile.isDirectory()) {
                deleteFolder(tempFile);
            } else {
                tempFile.delete();
            }
        }

    }

}