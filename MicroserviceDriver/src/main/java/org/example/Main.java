package org.example;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import com.google.gson.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception {
        String resourceName = "input.json";
        ParseAndCreate newParser = new ParseAndCreate();
        List<Post> newPosts = newParser.parsePosts(resourceName);
        List<Post> topLikedPosts;
        int[] indexes = new int[10];
        // Set each element to -1
        Arrays.fill(indexes, -1);
        int indexPointer = 0;

        PriorityQueue<Post> topPosts = new PriorityQueue<>((a, b) -> Integer.compare(a.getLikeCount(), b.getLikeCount()));

        for (Post post : newPosts) {
            if (topPosts.size() < 10) {
                topPosts.add(post);
            } else if (post.getLikeCount() > topPosts.peek().getLikeCount()) {
                topPosts.poll();  // Remove the post with the least likes
                topPosts.add(post);
            }
        }

        List<Post> topPostsList = new ArrayList<>(topPosts);
        for (Post post : topPostsList) {
            processPost(post, false);
        }

    }

    private static void processPost(Post post, boolean isReply) throws Exception {
        String moderationResult = moderatePost(post);
        String prefix = isReply ? "--> " : "";
        String output;
        if ("FAILED".equals(moderationResult)) {
            output = prefix + "[DELETED]";
        } else {
            output = prefix + post.getPostContent() + " " + moderationResult;
        }
        System.out.println(output);

        // Process replies recursively
        List<Post> replies = post.getReplies();
        if (replies != null && !replies.isEmpty()) {
            for (Post reply : replies) {
                processPost(reply, true);
            }
        }
    }


    private static String moderatePost(Post post) throws Exception {
        // Create JSON payload for the post
        String jsonInputString = "{\"postContent\": \"" + escapeJson(post.getPostContent()) + "\"}";

        URL url = new URL("http://localhost:30000/moderate");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Read response from the moderation service.
        java.io.InputStream responseStream = (con.getResponseCode() >= 400) ? con.getErrorStream() : con.getInputStream();
        java.util.Scanner s = new java.util.Scanner(responseStream).useDelimiter("\\A");
        String moderationResponse = s.hasNext() ? s.next() : "";
        s.close();
        return moderationResponse.trim();
    }


    private static String escapeJson(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}