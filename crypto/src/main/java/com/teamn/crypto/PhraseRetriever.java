package com.teamn.crypto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

/**
 * For retrieving a random line from a file.
 */
public class PhraseRetriever {

    private final String PHRASES_FILE;
    private Random rand;
    private final String[] fallbackPhrases =
            {
            "The unexamined life is not worth living",
            "Entities should not be multiplied unnecessarily",
            "I think therefore I am",
            "We live in the best of all possible worlds"
            };

    public PhraseRetriever(String filename){
        this.rand = new Random(System.currentTimeMillis());
        this.PHRASES_FILE = filename;
    }

    /**
     * Gets the number of lines in the file.
     * @return Number of lines in the file.
     */
    private int getLinesInFile() {
        int ret = 0;

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream sentencesFileStream = classLoader.getResourceAsStream(PHRASES_FILE);
        if (sentencesFileStream == null) { return ret; }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(sentencesFileStream))){
            while(reader.readLine() != null) { ret++; }
        } catch (IOException e) {
            System.out.println(String.format("Could not read file %s",PHRASES_FILE));
        } return ret;
    }

    /**
     * Gets a sentence from the PHRASES_FILE at the line number specified by index
     * @param index is the line number of the phase to return
     * @return A line from the file at the specified index.
     */
    private String getPhraseWithIndex(int index) {
        String sentence = null;

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream sentencesFileStream = classLoader.getResourceAsStream(PHRASES_FILE);
        if (sentencesFileStream == null) { return null; }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(sentencesFileStream))){
            int i = 0;
            while(i < index) {reader.readLine(); i++;}
            sentence = reader.readLine().toUpperCase();
        } catch (IOException e) {
            System.out.println(String.format("Could not read file %s",PHRASES_FILE));
        } return sentence;
    }

    private String getRandomDefaultPhrase() {
        int randomIndex = this.rand.nextInt(fallbackPhrases.length);
        return this.fallbackPhrases[randomIndex].toUpperCase();
    }

    /**
     * Gets a random phrase from PHRASES_FILE
     * @return A random phrase from PHRASES_FILE.
     */
    public String getRandomPhrase() {
        int linesInFile = getLinesInFile();
        if (linesInFile == 0) {
            System.out.println("\nThere was a problem reading the sentences file. " +
                    "Make sure that it's present inside 'resources' and it's named 'sentences'." +
                    "\nLoading fallback sentences instead...\n");
            return getRandomDefaultPhrase();
        } else {
            int randomIndex = this.rand.nextInt(linesInFile);
            return this.getPhraseWithIndex(randomIndex);
        }
    }
}
