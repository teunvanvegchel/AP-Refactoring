package nl.han.ica.core.util;

import org.apache.tools.ant.util.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Contains helpers for common operations involving files and directories.
 */
public class FileUtil {

    /**
     * Private constructor to prevent class initialization.
     */
    private FileUtil() {

    }

    /**
     * Recursively list all files in a directory.
     *
     * @param directory The directory to start searching in.
     * @return List of files.
     */
    public static List<File> listFilesRecursively(final File directory) {
        return listFilesRecursively(directory, null);
    }

    /**
     * Recursively list all files with a certain extension in a directory.
     *
     * @param directory The directory to start searching in.
     * @param extension Only include files with this extension.
     * @return List of files.
     */
    public static List<File> listFilesRecursively(final File directory, final String extension) {
        if (null == directory) {
            throw new IllegalArgumentException("Directory must not be null");
        }

        List<File> files = new ArrayList();
        Queue<File> directories = new LinkedList();
        directories.add(directory);
        while (!directories.isEmpty()) {
            for (File file : directories.poll().listFiles()) {
                if (file.isDirectory()) {
                    directories.add(file);
                } else if (file.isFile()) {
                    if (extension == null || file.getName().endsWith(extension)) {
                        files.add(file);
                    }
                }
            }
        }
        return files;
    }

    public static String getFileContent(File file) throws IOException {
        String lineSeparator = System.getProperty("line.separator");
        BufferedReader reader = new BufferedReader(new FileReader(file.getAbsoluteFile()));
        String nextLine;
        StringBuffer buffer = new StringBuffer();
        while ((nextLine = reader.readLine()) != null) {
            buffer.append(nextLine);
            buffer.append(lineSeparator);
        }
        return buffer.toString();
    }

    public static void setFileContent(File file, String content) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(content.getBytes());
    }
}
