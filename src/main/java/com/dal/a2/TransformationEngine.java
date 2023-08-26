package com.dal.a2;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.*;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A  class for cleaning up the data from the files and sending to the mongodb database
 */
public class TransformationEngine {
    /**
     * controls the clean up and upload process
     *@throws Exception If there was an error reading the files.
     */
    public void cleanAndUpload() {
        String directoryPath = "output";
        try {
            List<File> fileList = getFileList(directoryPath);
            for (File file : fileList) {


                NewsArticle article = parseNewsArticle(file);

                NewsArticle cleanedArticle = cleanArticle(article);

                System.out.println("Uploading");


                writeDocumentToMongoDB(cleanedArticle);
            }
        }catch(Exception e){

        }
    }

    /**
     * bringing the list of all the files from folder specified
     *
     * @param directoryPath path to get all files
     *
     * @return fileList list of files
     */
    public static List<File> getFileList(String directoryPath) {
        List<File> fileList = new ArrayList<>();
        File directory = new File(directoryPath);
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    fileList.add(file);
                }
            }
        }
        for (int i = 0; i < fileList.size(); i++) {
            System.out.println(fileList.get(i));

        }
        return fileList;
    }

    /**
     * to clean up the articles
     *
     * @param article to be cleaned
     *
     * @return article
     */
    public static NewsArticle cleanArticle(NewsArticle article) {

        article.setTitle(article.getTitle().replaceAll("[^a-zA-Z0-9\\s:]", " ").replaceAll("(http://|https://|www)\\S+", "").replaceAll(":[)DpP\\]\\|/\\\\]+|:[(\\[]+[DdPp]+", ""));


        return article;
    }

    /**
     * to store in MongoDB
     *
     * @param cleanedText to be stored on mongodb
     *
     */
    public static void writeDocumentToMongoDB(NewsArticle cleanedText) {

        //System.out.println("in mongo***************");
        ConnectionString connectionString = new ConnectionString("mongodb+srv://jayant:Jayant%40123@cluster0.gop9itj.mongodb.net/?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase("myMongoNews");
        MongoCollection<Document> collection = database.getCollection("NewsArticles");

        Document doc = new Document("title", cleanedText.getTitle())
                .append("content", cleanedText.getContent());

        collection.insertOne(doc);

        mongoClient.close();
    }

    /**
     * to store in MongoDB
     *
     * @param file to parse to the files convert the title and content text lines into NewsArticle objects
     *
     * @throws IOException if there is an error in reading the file
     *
     * @return newsArticle
     */
    public NewsArticle parseNewsArticle(File file) throws IOException {
        String title = null;
        String content = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Title:")) {
                    title = line.substring(7).trim();

                }
                if (line.startsWith("Content:")) {
                    content = line.substring(9).trim();

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("Title: " + title);
        //System.out.println("Content: " + content);
        return new NewsArticle(title, content);
    }



}