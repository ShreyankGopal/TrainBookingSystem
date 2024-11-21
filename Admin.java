import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Admin extends User {
    private int adminID;

    public Admin() {
        super();  // Call to the User constructor
        
    }
    int getAdminID(){
        return this.adminID;
    }
    void setAdminID(int adminID){
        this.adminID=adminID;
    }
    // Method to edit the train schedule
    public void editTrainSchedule(Connection conn, String trainID) {
        Scanner scanner = new Scanner(System.in);
    
        System.out.println("Editing schedule for Train ID: " + trainID);
        System.out.print("Enter new departure time (HH:mm:ss): ");
        String departureTime = scanner.nextLine();
        System.out.print("Enter new arrival time (HH:mm:ss): ");
        String arrivalTime = scanner.nextLine();
        System.out.print("Do you want to reset seats? Y/N: ");
        String yesOrNo = scanner.nextLine();
    
        // SQL query to update departure and arrival times
        String updateQuery = "UPDATE Train SET departure = ?, arrival = ? WHERE trainID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, departureTime);
            pstmt.setString(2, arrivalTime);
            pstmt.setString(3, trainID);
    
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Train schedule updated successfully.");
            } else {
                System.out.println("Train with ID " + trainID + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        // If user chooses "Y", reset seat counts to 100
        if (yesOrNo.equalsIgnoreCase("Y")) {
            String resetSeatsQuery = "UPDATE Coach SET AC = 100, SC = 100, GE = 100 WHERE trainID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(resetSeatsQuery)) {
                pstmt.setString(1, trainID);
    
                int rowsUpdated = pstmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Seats reset  successfully for all coach types.");
                } else {
                    System.out.println("No coaches found for Train ID: " + trainID);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    

    // Method to print report of all trains and coach details
    public void printReport(Connection conn) {
        String query = "SELECT * FROM Train";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id=rs.getInt("id");
                String trainID = rs.getString("trainID");
                String routeStart = rs.getString("route_start");
                String routeEnd = rs.getString("route_end");
                String departure = rs.getString("departure");
                String arrival = rs.getString("arrival");

                System.out.println("Train ID: " + trainID);
                System.out.println("Route: " + routeStart + " -> " + routeEnd);
                System.out.println("Departure: " + departure + " Arrival: " + arrival);

                // Print coach details for this train
                printCoachDetails(conn, id);

                System.out.println("------------------------------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper method to print coach details for a train
    private void printCoachDetails(Connection conn, int trainID) {
        String coachQuery = "SELECT * FROM Coach WHERE trainID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(coachQuery)) {
            pstmt.setInt(1, trainID);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("Coach Details:");
            while (rs.next()) {
                int AC = rs.getInt("AC");
                int SC = rs.getInt("SC");
                int GE = rs.getInt("GE");
                
                System.out.println("AC:"+AC);
                System.out.println("SC:"+SC);
                System.out.println("GE:"+GE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Overriding login method
    @Override
    public void login(Connection conn) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Email: ");
        String inputEmail = scanner.nextLine();

        System.out.print("Enter Password: ");
        String inputPassword = scanner.nextLine();

        String query = "SELECT * FROM User u JOIN Admin a ON u.userID = a.userID WHERE u.email = ? AND u.password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, inputEmail);
            pstmt.setString(2, inputPassword);

            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                this.setUserID(resultSet.getInt("userID"));
                this.setAdminID(resultSet.getInt("adminID"));
                this.setName(resultSet.getString("name"));
                this.setEmail(resultSet.getString("email"));
                this.setPhone(resultSet.getString("phone"));
                this.setPassword(resultSet.getString("password"));
                
                this.setActive(1);
                

                String updateActiveQuery = "UPDATE User SET active = 1 WHERE userID = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateActiveQuery)) {
                    updateStmt.setInt(1, this.getUserID());
                    updateStmt.executeUpdate();
                }

                System.out.println("Admin login successful! Welcome, " + this.getName() + ".");
            } else {
                this.setActive(0);
                System.out.println("Invalid email or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void logout(Connection conn) {
        if (this.getActive() == 1) {
            this.setActive(0);

            String updateActiveQuery = "UPDATE User SET active = 0 WHERE userID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateActiveQuery)) {
                pstmt.setInt(1, this.getUserID());
                pstmt.executeUpdate();
                System.out.println("Admin successfully logged out.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Passenger is not logged in.");
        }
    }
    // Overriding logout method
    

    // Overriding addUserToDatabase method

    
}