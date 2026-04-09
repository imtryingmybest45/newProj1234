package wonderful.com.example.demo;
import javax.xml.transform.Result;
import java.sql.*;

public class sqlStuff {

        public static void main(String[] args) {
            // AWS RDS Endpoint from the AWS Console
            String endpoint = "lizard.c6de8wseq94u.us-east-1.rds.amazonaws.com";
            String port = "3306"; // Default for MySQL
            String dbName = "mysql";

            // JDBC URL format: jdbc:<engine>://<endpoint>:<port>/<dbName>
            String url = "jdbc:mysql://" + endpoint + ":" + port + "/" + dbName;
            String username = "tomthelizard";
            String password = "lizarddd";

            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                if (conn != null) {
                    System.out.println("Connected to AWS RDS successfully!");
                    Statement stmt = conn.createStatement();
                    stmt.execute("USE celebs");
                    ResultSet rs = stmt.executeQuery("SELECT * FROM celebInfo");
                    while (rs.next()) {
                        // Retrieve values by column name
                        int id = rs.getInt("id");
                        String name = rs.getString("name");

                        // Display to console
                        System.out.println("ID: " + id + ", Name: " + name);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
