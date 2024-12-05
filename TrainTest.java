
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.Console;
import java.sql.*;
import java.util.Scanner;
public class TrainTest {

    // Static variables for database configuration
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/TrainBookingSystem?useSSL=false";
    static final String USER = "root"; // Add your database username
    static final String PASSWORD = "Sghu*560"; // Add your database password

    @Test
    public void testGetSchedule() {
        Connection conn = null;
        try {
            // Load the JDBC driver
            Class.forName(JDBC_DRIVER);

            // Connect to the database
            System.out.println("Connecting to the database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            conn.setAutoCommit(false);

            // Expected schedule result from the database
            String expectedSchedule = "TRAIN001 Delhi Mumbai 06:00:00 18:30:00 ,"
            + "TRAIN002 Mumbai Chennai 07:30:00 21:45:00 ,"
            + "TRAIN003 Chennai Kolkata 05:15:00 20:00:00 ,"
            + "TRAIN004 Kolkata Delhi 11:30:00 14:30:00 ,";
            
            // Act: Call the getSchedule method
            String actualSchedule = Train.getSchedule(conn);

            // Assert: Check if the returned schedule matches the expected result
            assertEquals("The schedule should match the expected result", expectedSchedule, actualSchedule);

        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during the test: " + e.getMessage());
        } finally {
            // Close the database connection
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
