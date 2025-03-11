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

        PriorityQueue<Post> topPosts = new PriorityQueue<>((a, b) -> Integer.compare(a.getLikeCount(), b.getLikeCount()));

        for (Post post : newPosts) {
            if (topPosts.size() < 10) {
                topPosts.add(post);
            } else if (post.getLikeCount() > topPosts.peek().getLikeCount()) {
                topPosts.poll();  // remove post with least likes
                topPosts.add(post);
            }
        }

        List<Post> topPostsList = new ArrayList<>(topPosts);
        for (Post post : topPostsList) {
            processPost(post, false);
        }

    }

    private static void processPost(Post post, boolean isReply) throws Exception {
        String result = moderatePost(post);
        String prefix = isReply ? "--> " : "";
        String output;
        if ("FAILED".equals(result)) {
            output = prefix + "[DELETED]";
        } else {
            output = prefix + post.getPostContent() + " " + result;
        }
        System.out.println(output);

        // recurse on replies
        List<Post> replies = post.getReplies();
        if (replies != null && !replies.isEmpty()) {
            for (Post reply : replies) {
                processPost(reply, true);
            }
        }
    }


    private static String moderatePost(Post post) throws Exception {
        // create JSON payload for the post
        String jsonInputString = "{\"postContent\": \"" + escapeJson(post.getPostContent()) + "\"}";

        URL url = new URL("http://localhost:30000/moderate");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        java.io.InputStream responseStream = (connection.getResponseCode() >= 400) ? connection.getErrorStream() : connection.getInputStream();
        java.util.Scanner s = new java.util.Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";
        s.close();
        return response.trim();
    }


    private static String escapeJson(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}