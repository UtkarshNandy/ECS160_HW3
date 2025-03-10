package org.example;

import io.github.ollama4j.exceptions.OllamaBaseException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.models.response.OllamaResult;
import io.github.ollama4j.types.OllamaModelType;
import io.github.ollama4j.utils.OptionsBuilder;

import java.io.IOException;
import java.net.URISyntaxException;


@RestController
public class HashtaggingController {

    @PostMapping("/hashtag")
    public String hashtag(@RequestBody MyRequest request) throws OllamaBaseException, IOException, URISyntaxException, InterruptedException {

        String content = request.getPostContent();

        String host = "http://localhost:11434/";
        OllamaAPI ollamaAPI = new OllamaAPI(host);
        ollamaAPI.setRequestTimeoutSeconds(60);
        String prompt = "Generate a one word hashtag for the following sentence: " + content;
        OllamaResult result =
                ollamaAPI.generate(OllamaModelType.LLAMA2, prompt, false, new OptionsBuilder().build());

        return result.getResponse();
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
