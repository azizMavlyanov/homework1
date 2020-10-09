package ru.digitalhabbits.homework1.service;

import javax.annotation.Nonnull;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static java.util.Arrays.stream;

public class FileEngine {
    private static final String RESULT_FILE_PATTERN = "results-%s.txt";
    private static final String RESULT_DIR = "results";
    private static final String RESULT_EXT = "txt";

    public boolean writeToFile(@Nonnull String text, @Nonnull String pluginName, @Nonnull String request) {
        final String cleanedText = text.replaceAll(";", " ");
        final String currentDir = System.getProperty("user.dir");
        final String resultPath = currentDir + "/" + RESULT_DIR;
        final File resultDir = new File(resultPath);

        if (!resultDir.isDirectory()) {
            resultDir.mkdir();
        }

        final String requestDirPath = resultPath + "/" + request;
        final File requestDir = new File(requestDirPath);
        final File resultFile = new File(requestDirPath + "/" + String
                .format(RESULT_FILE_PATTERN, pluginName));

        if (!requestDir.isDirectory()) {
            requestDir.mkdir();
        }

        if (!resultFile.isFile()) {
            try {
                resultFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (FileWriter writer = new FileWriter(resultFile);
             BufferedWriter bw = new BufferedWriter(writer)) {
            bw.write(cleanedText);

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

        return true;
    }

    public void cleanResultDir(@Nonnull String request) {
        final String currentDir = System.getProperty("user.dir");
        final File resultDir = new File(currentDir + "/" + RESULT_DIR + "/" + request);

        if(resultDir.exists()) {
            stream(resultDir.list((dir, name) -> name.endsWith(RESULT_EXT)))
                    .forEach(fileName -> new File(resultDir + "/" + fileName).delete());
        }
    }
}
