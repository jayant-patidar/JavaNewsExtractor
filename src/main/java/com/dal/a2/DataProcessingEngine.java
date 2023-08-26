package com.dal.a2;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * A  class for Processing the Articles from Extraction Engine, pull title and content and write to files
 * and Automatically call Transformation Engine
 */
public class DataProcessingEngine {
    /**
     * Process the news articles by finding the title and content and writing them to files
     * @param allArticles to process them.
     */
    public void processData(String allArticles) {
        Pattern titleRegexPattern = Pattern.compile("\"title\":\\s*\"([^\"]+)\"");
        Pattern contentRegexPattern = Pattern.compile("\"content\":\\s*\"([^\"]+)]\"");

        Matcher titleMatcher = titleRegexPattern.matcher(allArticles);
        Matcher contentMatcher = contentRegexPattern.matcher(allArticles);

        int articlesCount = 0;
        int filesCount = 1;
        StringBuilder sb = new StringBuilder();
        while (titleMatcher.find() && contentMatcher.find()) {

                String title = titleMatcher.group(1);
                //System.out.println("title" + articlesCount + " : " + title);
                String content = contentMatcher.group(1);
                //System.out.println("content" + articlesCount + " : " + content);

                String fileName =  "./output/articles" + filesCount + ".txt";
                try (FileWriter writer = new FileWriter(fileName, true)) {
                    writer.write("Title: " + title + "\n");
                    writer.write("Content: " + content + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                articlesCount++;
                if (articlesCount % 5 == 0) {
                    filesCount++;
                }

        }
        TransformationEngine transformationEngine = new TransformationEngine();
        transformationEngine.cleanAndUpload();
    }
}