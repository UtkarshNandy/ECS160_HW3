package com.ecs160.hw2;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ModerationController {

    @PostMapping("/moderate")
    public String moderate(@RequestBody MyRequest request) throws URISyntaxException, IOException, InterruptedException {
        // Moderation logic
        String[] keywords = {
                "illegal", "fraud", "scam", "exploit",
                "dox", "swatting", "hack", "crypto", "bots"
        };

        String content = request.getPostContent();
        String lowerCaseInput = content.toLowerCase();
        boolean found = false;
        for (String keyword : keywords) {
            if (lowerCaseInput.contains(keyword)) {
                found = true;
                break;
            }
        }

        if(found){
            return "FAILED";
        }else{
            String jsonBody = "{\"postContent\": \"" + content.replace("\"", "\\\"") + "\"}";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest secondRequest = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:30001/hashtag"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(secondRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response is: " + response.body());
            return "PASSED";
        }
    }

    static class MyRequest {
        private String postContent;

        public String getPostContent() {
            return postContent;
        }

        public void setPostContent(String postContent) {
            this.postContent = postContent;
        }
    }
}