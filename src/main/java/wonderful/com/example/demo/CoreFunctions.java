package wonderful.com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLDecoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class CoreFunctions {
    public static String getMoviePoster(String movieQuery, String movieYear) throws JsonProcessingException {
        movieQuery = movieQuery.replaceAll("[^a-zA-Z0-9 ]", "");
        movieQuery = movieQuery.replaceAll(" ", "+");

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        // 2. Build the HttpRequest object
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://www.omdbapi.com/?t=" + movieQuery + "&y=" + movieYear + "&apikey=98f9696d")) // Replace with your API URL
                .header("Accept", "application/json") // Common header for JSON APIs
                .GET() // Specify the HTTP method (GET, POST, PUT, DELETE, etc.)
                .build();
        String posterResp = null;
        String poster = "'boo'";
        System.out.println(request);
        try {
            // 3. Send the request synchronously and receive the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 4. Check the response status code and body
            // System.out.println("Status Code: " + response.statusCode());
            if (response.statusCode() == 200) {
                // System.out.println("Response Body: " + response.body());

                posterResp = response.body();

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                // Deserialize the JSON string directly into a Person object
                Poster moviePoster = objectMapper.readValue(posterResp, Poster.class);
                String responseString = posterResp.toString();

                if (responseString.contains("\"Poster\":\"https://m.media-amazon.com/images")) {
                    poster = moviePoster.getPoster();
                    poster = "'" + poster + "'";
                }
                // You would typically parse the JSON response here
            } else {
                System.err.println("API request failed with status code: " + response.statusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return poster;
    }

    public static int countWords(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }

        // Trim leading/trailing spaces and split by one or more whitespace characters (\\s+)
        String[] words = text.trim().split("\\s+");

        // Return the length of the resulting array
        return words.length;
    }

    public static String decodeMovieURL(String movieQuery) {

        // It is critical to specify the character encoding, preferably UTF-8
        String decodedString = URLDecoder.decode(movieQuery, StandardCharsets.UTF_8);

        return decodedString;
    }
}
