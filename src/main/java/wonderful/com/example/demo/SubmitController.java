package wonderful.com.example.demo;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@RestController

public class SubmitController {

    //@CrossOrigin(origins="http://localhost:3000")

    @CrossOrigin(origins = {"http://localhost:3000",
            "https://green-smoke-0fa35931e.6.azurestaticapps.net/",
            "https://www.aprilshorrorcorner.com",
            "https://aprilshorrorcorner.com",
            "https://zealous-desert-09313150f.6.azurestaticapps.net/",
            "https://help.aprilshorrorcorner.com"})

    @GetMapping("/submitEndpoint")

    public Map<String, Object> getData() throws IOException {

        //MyRequestDTO dto = new MyRequestDTO();

        // AWS RDS Endpoint from the AWS Console
        String endpoint = "lizard.c6de8wseq94u.us-east-1.rds.amazonaws.com";
        String port = "3306"; // Default for MySQL
        String dbName = "mysql";

        // JDBC URL format: jdbc:<engine>://<endpoint>:<port>/<dbName>
        String url = "jdbc:mysql://" + endpoint + ":" + port + "/" + dbName;
        String username = "tomthelizard";
        String password = "lizarddd";
        String name;
        String poster;
        String review;
        String tier;
        int fullReview;
        int year;
        int movieId;
        Map<String, Object> response = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            if (conn != null) {
                System.out.println("Connected to AWS RDS successfully!");
                Statement stmt = conn.createStatement();
                stmt.execute("USE movies");
                ResultSet rs = stmt.executeQuery("SELECT * FROM horrorMovies ORDER BY name");
                while (rs.next()) {

                    HashMap<String, String> details = new HashMap<>();
                    // Retrieve values by column name
                    name = rs.getString("name");
                    poster = rs.getString("poster");
                    review = rs.getString("review");
                    tier = rs.getString("tier");
                    year = rs.getInt("year");
                    fullReview = rs.getInt("fullReview");
                    movieId = rs.getInt("movieId");

                    //System.out.println(name);

                    details.put("name",name);
                    details.put("year",Integer.toString(year));
                    details.put("poster",poster);
                    details.put("review",review);
                    details.put("tier",tier);
                    details.put("fullReview",Integer.toString(fullReview));
                    details.put("movieId",Integer.toString(movieId));

                    String movieName = name;
                    response.put(movieName, details);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return response;

    }
}
