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
            for (int i = 0; i < keywords.length; i++) {
                String encodedKeyword = URLEncoder.encode(keywords[i], "UTF-8");
                URL url = new URL("https://newsapi.org/v2/top-headlines?q=" + encodedKeyword + "&apiKey=5385abb6e1604fe4ad3b032a386d998f");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.getResponseCode();
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                con.disconnect();

                String pattern = "\"title\":\"(.*?)\".*?\"description\":\"(.*?)\"";
                Pattern r = Pattern.compile(pattern, Pattern.DOTALL);
                Matcher m = r.matcher(content.toString());

                while (m.find()) {
                    String title = m.group(1);
                    String details = m.group(2);

                    if (title.toLowerCase().contains(keywords[i].toLowerCase()) || details.toLowerCase().contains(keywords[i].toLowerCase())) {
                        news.add("Title: " + title + "\nContent: " + details + "\n");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return news;
    }
}