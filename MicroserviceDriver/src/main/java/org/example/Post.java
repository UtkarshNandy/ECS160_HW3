package org.example;

import java.util.List;


public class Post {

    private int postId;

    private int likeCount;

    private String postContent;

    private List<Post> replies;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public List<Post> getReplies() {
        return replies;
    }

    public void setReplies(List<Post> replies) {
        this.replies = replies;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }

    public int getLikeCount() { return likeCount; }
}

