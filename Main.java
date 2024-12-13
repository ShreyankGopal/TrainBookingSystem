
import java.io.Console;
import java.sql.*;
import java.util.Scanner;

public class Main {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/TrainBookingSystem?useSSL=false";
    static final String USER = "root"; // Add your database username
    static final String PASSWORD = ""; // Add your database password
    
    public static void main(String[] args) {
        Connection conn = null;
        Scanner sc = new Scanner(System.in);

        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            // STEP 2: Open a connection
            System.out.println("Connecting to the database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            conn.setAutoCommit(false);

            boolean exit = false;
            User user = null; // To track the logged-in user
            //
            User adminUser=null;
            while (!exit) {
                // Display menu
                System.out.println("\nMenu:");
                System.out.println("Click 1 to View your credentials");
                System.out.println("Click 2 to Purchase Ticket");
                System.out.println("Click 3 to Register Customer");
                System.out.println("Click 4 to See your Tickets");
                System.out.println("Click 5 to Login");
                System.out.println("Click 6 to Logout");
                System.out.println("Click 7 to Delete Customer");
                System.out.println("Click 8 to Delete Ticket");
                System.out.println("Click 9 for admin Login");
                System.out.println("Click 10 for admin Logout");
                System.out.println("Click 11 for report generation(Admin only)");
                System.out.println("Click 12 for Train Scheduling(Admin only)");
                System.out.println("Click 13 to Exit");
                System.out.print("Enter your choice: ");
                System.out.println("");
                int choice = sc.nextInt();
                sc.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        if (user != null) {
                            ((Passenger2) user).printCredential();
                            
                        } else {
                            System.out.println("Please log in first.");
                        }
                        
                        break;
                    case 2:
                        if (user != null) {
                            System.out.println("Where to?");
                            Train.getSchedule(conn);
                            System.out.println("Enter the train ID u want to book?");
                            String trainID=sc.nextLine();
                            System.out.println("Enter the name of the coach you want");
                            Train.printAvailableClasses(conn, trainID);
                            String coachType = sc.nextLine();
                            System.out.println("Enter the number of seats");
                            int seats=sc.nextInt();
                            sc.nextLine();
                            Bookings booking=new Bookings(trainID, ((Passenger2) user).getPassengerID(),coachType, seats);
                            System.out.println("Do you confirm booking? Y/N");
                            String confirm=sc.nextLine();
                            if(confirm.equalsIgnoreCase("Y")){
                                booking.confirmBooking(conn);
                            }
                            else{
                                System.out.println("Booking cancelled");
                            }
                            
                        } else {
                            System.out.println("Please log in first.");
                        }
                        break;
                    
                    case 3:
                        try {
                            // Retrieve the last user ID from the database
                            
                        
                            // Collect user input
                            System.out.print("Enter your name: ");
                            String name = sc.nextLine();
                            System.out.print("Enter your email: ");
                            String email = sc.nextLine();
                            System.out.print("Enter your phone: ");
                            String phone = sc.nextLine();
                            System.out.print("Enter your Age: ");
                            int age = sc.nextInt();
                            System.out.print("Enter your sex: ");
                            String gender = sc.next();
                            // Hide password input
                            Console console = System.console();
                            String password;
                            if (console != null) {
                                char[] passwordArray = console.readPassword("Enter your password: ");
                                password = new String(passwordArray);
                            } else {
                                System.out.print("Enter your password: ");
                                password = sc.nextLine();
                            }
                        
                            // Create a new user object with the dynamic ID
                            user = new Passenger2(0,0, name, email, phone, password,age,gender);
                     //-----------------------------------------------------------------------------------//       //change here.....
                            // Save the user to the database
                            user.addUserToDatabase(conn);
                            System.out.println("Customer registered successfully!");
                        } catch (Exception e) {
                            System.out.println("Error during customer registration: " + e.getMessage());
                        }
                        
                        break;

                    case 4:
                        if (user != null) {
                            System.out.println("Your tickets:");
                           ( (Passenger2)user).getUserBookings(conn, ((Passenger2 )user).getPassengerID());
                        } else {
                            System.out.println("Please log in first.");
                        }
                        break;

                    case 5:
                        if(user !=null){
                            System.out.println("You are already logged in!!");
                        }
                        else{
                            user=new Passenger2();
                            System.out.print("Enter Email: ");
                            String inputEmail = sc.nextLine();
                
                            System.out.print("Enter Password: ");
                            String inputPassword = sc.nextLine();
                            user.login(conn,inputEmail,inputPassword);
                        }
                        break;
                        

                    case 6:
                        if (user != null) {
                            user.logout(conn);
                            System.out.println("Goodbye, " + user.getName());
                            user = null;
                        } else {
                            System.out.println("You are not logged in.");
                        }
                        break;

                    
                    case 7:   
                    
                    case 8:
                        if (user != null) {
                            System.out.print("Enter Booking ID to delete: ");
                            int ticketId = sc.nextInt();
                            sc.nextLine();
                            Bookings.cancelBooking(conn, ticketId);
                            System.out.println("Ticket deleted successfully.");
                        } else {
                            System.out.println("Please log in first.");
                        }
                        break;

                    
                    case 9:
                        if(user !=null){
                            System.out.println("First logout as user");
                        }
                        else{
                            adminUser = new Admin();
                            System.out.print("Enter Email: ");
                            String inputEmail = sc.nextLine();
                
                            System.out.print("Enter Password: ");
                            String inputPassword = sc.nextLine();
                            adminUser.login(conn,inputEmail,inputPassword);
                        }
                        break;
                    case 10:
                        if (adminUser != null) {
                            adminUser.logout(conn);
                            System.out.println("Goodbye, " + adminUser.getName());
                            adminUser = null;
                        } else {
                            System.out.println("You are not logged in.");
                        }
                    break;
                    case 11:
                        if(adminUser !=null){
                            ((Admin) adminUser).printReport(conn);
                        }
                        else{
                            System.out.println("You are not Authorised");
                        }
                        break;
                    case 12:
                        if(adminUser !=null){
                            System.out.println("enter Train ID");
                            String trainID=sc.nextLine();
                            ((Admin) adminUser).editTrainSchedule(conn,trainID);
                        }
                        else{
                            System.out.println("You are not Authorised");
                        }
                        break;
                    case 13:
                        System.out.println("Exiting program...");
                        exit = true;
                        break;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }

                // Commit changes
                conn.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    System.out.println("Rolling back changes...");
                    conn.rollback();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        } finally {
            try {
                if (conn != null) conn.close();
                sc.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
