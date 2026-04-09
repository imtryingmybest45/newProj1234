package wonderful.com.example.demo;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@RestController

public class AddController {

    //@CrossOrigin(origins="http://localhost:3000")
    @CrossOrigin(origins = {"http://localhost:3000",
            "https://green-smoke-0fa35931e.6.azurestaticapps.net/",
            "https://www.aprilshorrorcorner.com",
            "https://aprilshorrorcorner.com",
            "https://zealous-desert-09313150f.6.azurestaticapps.net/",
            "https://help.aprilshorrorcorner.com"})

    @PostMapping("/addEndpoint")

    public String addData(@RequestBody MyRequestDTO dto) throws IOException, SQLException {

        // AWS RDS Endpoint from the AWS Console
        String endpoint = "lizard.c6de8wseq94u.us-east-1.rds.amazonaws.com";
        String port = "3306"; // Default for MySQL
        String dbName = "mysql";
        String url = "jdbc:mysql://" + endpoint + ":" + port + "/" + dbName;
        String username = "tomthelizard";
        String password = "lizarddd";

        String name;
        String poster = "placeholder";
        String review;
        String tier = "placeholder";
        String fullReview = "0";
        String year;

        Map<String, Object> response = new HashMap<>();
        CoreFunctions coreFunctions = new CoreFunctions();

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            if (conn != null) {
                System.out.println("Connected to AWS RDS successfully!");
                Statement stmt = conn.createStatement();
                stmt.execute("USE movies");
                //stmt.execute("INSERT INTO horrorMovies (name, poster, year, review, tier, fullReview)");

                // Retrieve values by column name
                name = dto.getMovieName();
                review = dto.getMovieReview();
                year = dto.getMovieYear();
                tier = dto.getMovieTier();
                poster = coreFunctions.getMoviePoster(name, year);

                int wordCount = coreFunctions.countWords(review);
                if (wordCount > 500) {
                    fullReview = "1";
                }

                name = "'"+name+"'";
                year = "'"+year+"'";

                String valuesInserted = String.format("INSERT INTO horrorMovies (name, poster, year, review, tier, fullReview) VALUES (%s, %s, %s, %s, %s, %s);",name, poster, year, review, tier, fullReview);
                stmt.executeUpdate(valuesInserted);
                System.out.println(valuesInserted);

                }
            }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return "You have submitted your review. Please wait a few minutes for the website to refresh.";
    }
}
