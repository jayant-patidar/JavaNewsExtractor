package com.dal.a2;


/**
 * A  class for to create object of type NewsArticle with title and content
 */
public class NewsArticle {
    private String title;
    private String content;

    public NewsArticle(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
