package com.dal.a2;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * A  class for Extracting the data from News API and Automatically sending it to Processing Engine
 */
public class ExtractionEngine {
    /**
     * Hit the API and extracts news articles
     * @param args to kick start the app.
     * @throws Exception If there was an error hitting the api.
     */
    public static void main(String[] args) throws Exception {

        List<String> keywords = new ArrayList<String>(Arrays.asList("Canada", "University", "Dalhousie", "Halifax", "Canada Education", "Moncton", "hockey","Fredericton","celebration"));

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < keywords.size(); i++) {
            //System.out.println("Current keyword-----" + keywords.get(i));
            try {
                URL url = new URL("https://newsapi.org/v2/everything?q=" + keywords.get(i) + "+&apiKey=43d4a25591d4414e99904498054d6b5d\n");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");



                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((connection.getInputStream())));

                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);

                }
                bufferedReader.close();
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        DataProcessingEngine dataProcessingEngine=new DataProcessingEngine();
        dataProcessingEngine.processData(sb.toString());
    }

}