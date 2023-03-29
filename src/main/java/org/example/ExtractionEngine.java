package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

public class ExtractionEngine {
    /**
     * Used to extract news from NewsAPI containing specific keywords.
     *
     * @return The title of the news and content of the news.
     */
    public static ArrayList<String> extractNews() {

        ArrayList<String> news = new ArrayList<>();
        try {
            String[] keywords = {"Canada", "University", "Dalhousie", "Halifax", "Canada Education", "Moncton", "hockey", "Fredericton", "celebration"};
            for (int i=0;i<keywords.length;i++){
                String encodedKeyword = URLEncoder.encode(keywords[i], "UTF-8");
                URL url = new URL("https://newsapi.org/v2/top-headlines?q=" + encodedKeyword + "&apiKey=5385abb6e1604fe4ad3b032a386d998f");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.getResponseCode();
                BufferedReader input = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputString;
                StringBuilder builder = new StringBuilder();
                while ((inputString = input.readLine()) != null) {
                    builder.append(inputString);
                }
                input.close();
                con.disconnect();

                String str = "\"title\":\"(.*?)\".*?\"description\":\"(.*?)\"";
                Pattern pattern = Pattern.compile(str, Pattern.DOTALL);
                Matcher match = pattern.matcher(builder.toString());

                do {
                    String title = match.group(1);
                    String content = match.group(2);

                    if (title.toLowerCase().contains(keywords[i].toLowerCase()) || content.toLowerCase().contains(keywords[i].toLowerCase())) {
                        news.add("Title: " + title + "\nContent: " + content + "\n");
                    }
                } while (match.find());

            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return news;
    }
}