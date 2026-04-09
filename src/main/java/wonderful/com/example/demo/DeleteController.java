package wonderful.com.example.demo;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@RestController

public class DeleteController {
    //@CrossOrigin(origins="http://localhost:3000")
    @CrossOrigin(origins = {"http://localhost:3000",
            "https://green-smoke-0fa35931e.6.azurestaticapps.net/",
            "https://www.aprilshorrorcorner.com",
            "https://aprilshorrorcorner.com",
            "https://zealous-desert-09313150f.6.azurestaticapps.net/",
            "https://help.aprilshorrorcorner.com"})

    @PostMapping("/deleteEndpoint")
    public String editData(@RequestBody String movie) throws IOException, SQLException {
        // AWS RDS Endpoint from the AWS Console
        // AWS RDS Endpoint from the AWS Console
        String endpoint = "lizard.c6de8wseq94u.us-east-1.rds.amazonaws.com";
        String port = "3306"; // Default for MySQL
        String dbName = "mysql";
        String url = "jdbc:mysql://" + endpoint + ":" + port + "/" + dbName;
        String username = "tomthelizard";
        String password = "lizarddd";

        String name;

        CoreFunctions coreFunctions = new CoreFunctions();

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            if (conn != null) {
                System.out.println("Connected to AWS RDS successfully!");
                Statement stmt = conn.createStatement();
                stmt.execute("USE movies");
                //stmt.execute("INSERT INTO horrorMovies (name, poster, year, review, tier, fullReview)");

                // Retrieve values by column name
                movie = movie.substring(0, movie.length() - 1);
                movie = coreFunctions.decodeMovieURL(movie);
                name = "'"+movie+"'";

                String valuesInserted = String.format("DELETE from horrorMovies WHERE name = %s;",name);
                stmt.executeUpdate(valuesInserted);
                System.out.println(valuesInserted);

            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return "You have deleted " + movie +". Please wait a few minutes for the website to refresh.";
    }
}
