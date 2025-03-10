package org.example;
import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.models.generate.OllamaStreamHandler;
import io.github.ollama4j.models.response.OllamaResult;
import io.github.ollama4j.types.OllamaModelType;
import io.github.ollama4j.utils.OptionsBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Duration;
import java.net.http.HttpClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
@SpringBootApplication
public class HashtaggingService {
    public static void main(String[] args) throws OllamaBaseException, IOException, URISyntaxException, InterruptedException {

       SpringApplication.run(HashtaggingService.class, args);
    }
}