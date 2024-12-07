
import java.sql.*;
import java.util.Scanner;

public class User {
    private int userID;
    private String name;
    private String email;
    private String phone;
    private String password;
    private int active; // 0: Logged out, 1: Logged in

    // Constructor
    public User(int userID,String name, String email, String phone, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.active = 0; // Initially inactive
        this.userID=userID;
    }
    public User(){
        //do nothing;
    }

    // Getters and Setters
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    // Add the user to the database
    public boolean addUserToDatabase(Connection conn) {
        String insertQuery = "INSERT INTO User (name, email, phone, password, active) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            
            pstmt.setString(1, this.name); // Corrected index
            pstmt.setString(2, this.email); // Corrected index
            pstmt.setString(3, this.phone);    // Corrected index
            pstmt.setString(4, this.password); // Corrected index
            pstmt.setInt(5, this.active);   // Corrected index

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("User added successfully!");
            } else {
                System.out.println("Failed to add user.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    // Login method
    public boolean login(Connection conn,String email,String Password) {
        // Scanner scanner = new Scanner(System.in);

        // System.out.print("Enter Email: ");
        // String inputEmail = scanner.nextLine();

        // System.out.print("Enter Password: ");
        // String inputPassword = scanner.nextLine();

        // String query = "SELECT * FROM User WHERE email = ? AND password = ?";
        // try (PreparedStatement pstmt = conn.prepareStatement(query)) {
        //     pstmt.setString(1, inputEmail);
        //     pstmt.setString(2, inputPassword);

        //     ResultSet resultSet = pstmt.executeQuery();
        //     if (resultSet.next()) {
        //         this.userID = resultSet.getInt("userID");
        //         this.name = resultSet.getString("name");
        //         this.email = resultSet.getString("email");
        //         this.phone = resultSet.getString("phone");
        //         this.password = resultSet.getString("password");
        //         this.active = 1;

        //         String updateActiveQuery = "UPDATE User SET active = 1 WHERE userID = ?";
        //         try (PreparedStatement updateStmt = conn.prepareStatement(updateActiveQuery)) {
        //             updateStmt.setInt(1, this.userID);
        //             updateStmt.executeUpdate();
        //         }

        //         System.out.println("Login successful! Welcome, " + this.name + ".");
        //     } else {
        //         this.active = 0;
        //         System.out.println("Invalid email or password.");
        //     }
        // } catch (SQLException e) {
        //     e.printStackTrace();
        // }
        return true;
    }

    // Logout method
    public boolean logout(Connection conn) {
        if (this.active == 1) {
            this.active = 0;

            String updateActiveQuery = "UPDATE User SET active = 0 WHERE userID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateActiveQuery)) {
                pstmt.setInt(1, this.userID);
                pstmt.executeUpdate();
                System.out.println("Successfully logged out.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("You are not logged in.");
        }
        return true;
    }
    
}
