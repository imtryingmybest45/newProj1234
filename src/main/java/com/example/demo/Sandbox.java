package com.example.demo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class Sandbox {
    public static void main(String[] args) throws JsonProcessingException {
        OtherFunctions otherFunctions = new OtherFunctions();
        String poster = otherFunctions.getMoviePoster("The+Autopsy+of+Jane+Doe");

        //ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Deserialize the JSON string directly into a Person object
        //Poster moviePoster = objectMapper.readValue(poster, Poster.class);

        // Access the fields using standard Java getters
        System.out.println(poster);
        //System.out.println("Age: " + person.getPoster());


    }
}