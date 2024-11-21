import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.Statement;

public class Passenger2 extends User {
    private int passengerID;
    private int age;
    private String gender; 
    private List<String> bookingHistory;

    // Constructor
    public Passenger2(int passengerID, int userID, String name, String email, String phone, String password,int age, String gender) {
        super(userID, name, email, phone, password);
        this.passengerID = passengerID;
        this.age = age;
        this.gender = gender;
        this.bookingHistory = new ArrayList<>();
    }
    public Passenger2(){
        super();

    }

    // Getters and Setters
    public int getPassengerID() {
        return passengerID;
    }
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPassengerID(int passengerID) {
        this.passengerID = passengerID;
    }

    public List<String> getBookingHistory() {
        return bookingHistory;
    }

    // Overridden login method
    @Override
    public void login(Connection conn) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Email: ");
        String inputEmail = scanner.nextLine();

        System.out.print("Enter Password: ");
        String inputPassword = scanner.nextLine();

        String query = "SELECT * FROM User u JOIN Passenger p ON u.userID = p.userID WHERE u.email = ? AND u.password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, inputEmail);
            pstmt.setString(2, inputPassword);

            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                this.setUserID(resultSet.getInt("userID"));
                this.setPassengerID(resultSet.getInt("passengerID"));
                this.setName(resultSet.getString("name"));
                this.setEmail(resultSet.getString("email"));
                this.setPhone(resultSet.getString("phone"));
                this.setPassword(resultSet.getString("password"));
                this.setAge(resultSet.getInt("age"));
                this.setGender(resultSet.getString("gender"));
                this.setActive(1);
                this.passengerID = resultSet.getInt("passengerID");

                String updateActiveQuery = "UPDATE User SET active = 1 WHERE userID = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateActiveQuery)) {
                    updateStmt.setInt(1, this.getUserID());
                    updateStmt.executeUpdate();
                }

                System.out.println("Passenger login successful! Welcome, " + this.getName() + ".");
            } else {
                this.setActive(0);
                System.out.println("Invalid email or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Overridden logout method
    @Override
    public void logout(Connection conn) {
        if (this.getActive() == 1) {
            this.setActive(0);

            String updateActiveQuery = "UPDATE User SET active = 0 WHERE userID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateActiveQuery)) {
                pstmt.setInt(1, this.getUserID());
                pstmt.executeUpdate();
                System.out.println("Passenger successfully logged out.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Passenger is not logged in.");
        }
    }
    @Override
    public void addUserToDatabase(Connection conn) {
        String userInsertQuery = "INSERT INTO User (name, email, phone, password, active) VALUES (?, ?, ?, ?, ?)";
        String passengerInsertQuery = "INSERT INTO Passenger (userID, age, gender) VALUES (?, ?, ?)";
        try {
            // Insert into User table
            try (PreparedStatement userStmt = conn.prepareStatement(userInsertQuery, Statement.RETURN_GENERATED_KEYS)) {
                
                // Adjust the parameter indices to match the correct order
                userStmt.setString(1, this.getName());   // name
                userStmt.setString(2, this.getEmail());  // email
                userStmt.setString(3, this.getPhone());     // phone
                userStmt.setString(4, this.getPassword()); // password
                userStmt.setInt(5, this.getActive());    // active
    
                int rowsInsertedUser = userStmt.executeUpdate();
                if (rowsInsertedUser > 0) {
                    System.out.println("User added successfully!");
    
                    // Retrieve the generated userID from the inserted user
                    ResultSet generatedKeys = userStmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int generatedUserID = generatedKeys.getInt(1);
                        this.setUserID(generatedUserID);
                    }
                } else {
                    System.out.println("Failed to add user.");
                    return; // Abort if user insertion fails
                }
            }
    
            // Insert into Passenger table with age and gender
            try (PreparedStatement passengerStmt = conn.prepareStatement(passengerInsertQuery)) {
                
                passengerStmt.setInt(1, this.getUserID());  // userID
                passengerStmt.setInt(2, this.getAge());     // age
                passengerStmt.setString(3, String.valueOf(this.getGender())); // gender
    
                int rowsInsertedPassenger = passengerStmt.executeUpdate();
                if (rowsInsertedPassenger > 0) {
                    System.out.println("Passenger added successfully!");
                } else {
                    System.out.println("Failed to add passenger.");
                }
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void printCredential() {
        System.out.println("Passenger Credentials:");
        System.out.println("Name: " + this.getName());
        System.out.println("Email: " + this.getEmail());
        System.out.println("Phone: " + this.getPhone());
        System.out.println("Age: " + this.getAge());
        System.out.println("Gender: " + this.getGender());
        System.out.println("User ID: " + this.getUserID());
        System.out.println("Passenger ID: " + this.getPassengerID());
    }
    public void getUserBookings(Connection conn, int passengerID) {
        // SQL query with JOIN on Train, Bookings, and Passenger tables
        String query = "SELECT b.bookingID, b.trainID, b.coachType, b.numberOfSeats, b.passengerID, " +
                       "t.route_start, t.route_end,t.departure,t.arrival " +
                       "FROM Bookings b " +
                       "JOIN Train t ON b.trainID = t.TrainID " +
                       
                       "WHERE b.passengerID = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, passengerID);
        
            ResultSet resultSet = pstmt.executeQuery();
            System.out.println("Bookings for Passenger ID: " + passengerID);
            System.out.println("------------------------------------------------");
            System.out.printf("%-10s %-10s %-15s %-15s %-10s %-20s %-10s %-10s %-20s %-20s %-20s %-20s%n", 
            "BookingID", "TrainID", "CoachType", "NumberOfSeats", "PassengerID", "PassengerName", 
            "Age", "Gender", "RouteStart", "RouteEnd", "Departure", "Arrival");
        
        
            while (resultSet.next()) {
                int bookingID = resultSet.getInt("bookingID");
                String trainID = resultSet.getString("trainID");
                String coachType = resultSet.getString("coachType");
                int numberOfSeats = resultSet.getInt("numberOfSeats");
                String passengerName = this.getName();
                int age = this.getAge();
                String gender = this.getGender();
                String routeStart = resultSet.getString("route_start");
                String routeEnd = resultSet.getString("route_end");
                String departure = resultSet.getString("departure"); // Retrieving departure time
                String arrival = resultSet.getString("arrival"); // Retrieving arrival time
                
                // Adjusted printf format to include Departure and Arrival times
                System.out.printf("%-10d %-10s %-15s %-15d %-10d %-20s %-10d %-10s %-20s %-20s %-20s %-20s%n", 
                    bookingID, trainID, coachType, numberOfSeats, passengerID, passengerName, 
                    age, gender, routeStart, routeEnd, departure, arrival);
            }
            
        
            System.out.println("------------------------------------------------");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
}
