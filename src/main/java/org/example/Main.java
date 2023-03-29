package org.example;

import java.util.ArrayList;

public class Main {
        /**
         * Main method used to trigger Code A, Code B and Code C without any manual intervention.
         *
         */
        public static void main(String[] args) {
        ArrayList<String> newsArticles = ExtractionEngine.extractNews();
        DataProcessingEngine.fileWriter(newsArticles);
        TransformationEngine.transform();
        }
    }