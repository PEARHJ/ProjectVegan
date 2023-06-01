package kr.ac.duksung.projectvegan.model;

import java.util.Date;

public class FeedInfo {

    private String title, content, publisher, postId, uri;
    private Date createdAt;

    public FeedInfo(String title, String content, String publisher, String postId, String uri, Date createdAt) {
        this.title = title;
        this.content = content;
        this.publisher = publisher;
        this.postId = postId;
        this.uri = uri;
        this.createdAt = createdAt;
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
