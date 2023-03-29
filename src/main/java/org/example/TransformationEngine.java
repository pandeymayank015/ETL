package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class TransformationEngine {
    /**
     * Used to transfer data inside text files into mongoDB with database "myMongoNews" and collection "dmwacollection".
     *
     */
    public static void transform() {
        try {
            //Code copied from MongoDB document
            ConnectionString connectionString = new ConnectionString("mongodb+srv://mayankp:Securedmongo@cluster0.pkmymyo.mongodb.net/?retryWrites=true&w=majority");
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .build();
            MongoClient mongoClient = MongoClients.create(settings);
            MongoDatabase database = mongoClient.getDatabase("myMongoNews");
            MongoCollection<Document> collection = database.getCollection("dmwacollection");

            File dir = new File(".");
            for (File file : dir.listFiles()) {
                if (file.getName().startsWith("news")) {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("Title: ")) {
                            String title = line.substring("Title: ".length());
                            String content = reader.readLine();
                            String cleanedTitle = cleanData(title);
                            String cleanedContent = cleanData(content);

                            if (!cleanedTitle.isEmpty() && !cleanedContent.isEmpty()) {
                                Document doc = new Document("title", cleanedTitle)
                                        .append("content", cleanedContent);
                                collection.insertOne(doc);
                            }
                        }
                    }

                    reader.close();
                }
            }
            mongoClient.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    /**
     * Used to remove special characters, URLs, emoticons and white spaces using regex.
     *
     * @param str provided by the user.
     * @return The clean data which is present inside the text files.
     */

    private static String cleanData(String str) {
        /*reference from https://www.baeldung.com/java-string-remove-emojis,
        https://stackoverflow.com/questions/44620294/removing-special-character-from-java-string*/
        str = str.replaceAll("(?i)https?://\\S+\\b", "");

        str = str.replaceAll("[^\\s\\p{Alnum}]", "");

        Pattern emoticonPattern = Pattern.compile("[^\\p{L}\\p{N}\\p{P}\\p{Z}]");
        Matcher matcher = emoticonPattern.matcher(str);
        str = matcher.replaceAll("");

        str = str.replaceAll("\\s{2,}", " ");

        return str.trim();
    }
}