package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DataProcessingEngine {
    /**
     * Used to write data extracted form ExtractionEngine into text files.
     *
     * @param newsArticles which are fetched from NewsAPI.
     */
    public static void fileWriter(ArrayList<String> newsArticles) {
        int fc = 1;
        int ac = 0;
        BufferedWriter writer = null;
        try {
            for (String newsArticle : newsArticles) {
                if (ac == 0) {
                    String fileName = "news" + fc + ".txt";
                    writer = new BufferedWriter(new FileWriter(fileName));
                    fc++;
                }
                writer.write(newsArticle + "\n\n");
                ac++;
                if (ac == 5) {
                    ac = 0;
                    writer.close();
                }
            }
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}