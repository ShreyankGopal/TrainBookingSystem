

import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class testPrintAvailableClasses {

    @Test
    public void testPrintAvailableClasses() throws Exception {
        // Database configuration
        final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://localhost:3306/TrainBookingSystem?useSSL=false";
        final String USER = "root"; // Add your database username
        final String PASSWORD = "Sghu*560"; // Add your database password

        // Establish database connection
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        conn.setAutoCommit(false);

        // Arrange
        String trainID1 = "TRAIN001"; // Train ID to test
        String expectedClasses1 = "AC, SC, GE"; // Expected output
        String trainID2 = "TRAIN002"; // Train ID to test
        String expectedClasses2 = "AC, SC, GE"; // Expected output

        // Act
        String actualClasses1 = Train.printAvailableClasses(conn, trainID1);
        String actualClasses2 = Train.printAvailableClasses(conn, trainID2);

        // Assert
        assertEquals("The available classes should match the expected result", expectedClasses1, actualClasses1);
        assertEquals("The available classes should match the expected result", expectedClasses2, actualClasses2);

        // Close the connection (optional cleanup)
        conn.close();
    }
}

