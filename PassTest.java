import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.AfterClass;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PassTest {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/TrainBookingSystem?useSSL=false";
    static final String USER = "root"; // Add your database username
    static final String PASSWORD = "Sghu*560"; // Add your database password

    private Passenger2 testPassenger;
    private static Connection conn = null;

    @BeforeClass
    public static void setUpDatabaseConnection() throws Exception {
        Class.forName(JDBC_DRIVER);

        System.out.println("Connecting to the database...");
        conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    @Before
    public void setUpTestPassenger() {
        // Initialize a sample test passenger
        testPassenger = new Passenger2(0, 0, "John Doe", "john.doe@example.com", "1234567890", "password123", 25, "Male");
    }

    @AfterClass
    public static void closeDatabaseConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("Failed to close database connection: " + e.getMessage());
        }
    }

    @Test
    public void testAddUserToDatabase() {
        boolean result = testPassenger.addUserToDatabase(conn);
        assertTrue("User and passenger should be added to the database successfully.", result);

        // Verify that userID and passengerID were set
        assertTrue("userID should be set after insertion.", testPassenger.getUserID() > 0);
        //assertTrue("passengerID should be set after insertion.", testPassenger.getPassengerID() > 0);
    }

    @Test
    public void testLoginSuccess() {
        boolean loginSuccess = testPassenger.login(conn,"john.doe@example.com","password123");
        assertTrue("Login should succeed for valid credentials.", loginSuccess);
        assertEquals("Active status should be set to 1 after successful login.", 1, testPassenger.getActive());
    }




    @Test
    public void testLogoutSuccess() {
        // Ensure the passenger is logged in first
        testPassenger.login(conn,"john.doe@example.com","password123");
        boolean logoutSuccess = testPassenger.logout(conn);

        assertTrue("Logout should succeed if the passenger is logged in.", logoutSuccess);
        assertEquals("Active status should be set to 0 after logout.", 0, testPassenger.getActive());
    }
}
