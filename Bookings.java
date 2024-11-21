
import java.sql.*;

public class Bookings {
  
    private String trainID;
    private int passengerID;
    private String coachType;
    private int numberOfSeats;
   

    // Constructor
    public Bookings(String trainID, int passengerID, String coachType,int numberOfSeats) {
        
        this.trainID = trainID;
        this.passengerID = passengerID;
        this.coachType = coachType;
        this.numberOfSeats=numberOfSeats;
       
    }

    // Getters and Setters


    public int getSeats() {
        return this.numberOfSeats;
    }
    public void setSeats(int seats) {
        this.numberOfSeats=seats;
    }
    public String getTrainID() {
        return trainID;
    }

    public void setTrainID(String trainID) {
        this.trainID = trainID;
    }

    public int getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(int passengerID) {
        this.passengerID = passengerID;
    }

    public String getCoachType() {
        return coachType;
    }

    public void setCoachType(String coachType) {
        this.coachType = coachType;
    }

    // Method to confirm booking (adds the booking details to the database)
    public void confirmBooking(Connection conn) {
        try {

            String getID="Select id from Train where TrainID = ?";
            int tid=0;
            try(PreparedStatement pstmt=conn.prepareStatement(getID)){
                System.out.println(this.trainID);
                pstmt.setString(1, this.trainID);
                ResultSet rst=pstmt.executeQuery();
                if (rst.next()) { // Check if there is at least one row
                    tid = rst.getInt("id");
                    System.out.println(tid);
                } else {
                    System.out.println("No train found with TrainID: " + this.trainID);
                }

            }
            catch(SQLException e){
                System.out.println("Couldn't get the trainID");
            }
            // Call reserveSeat before confirming the booking
            Coach.reserveSeat(conn, tid, this.coachType, this.numberOfSeats);
    
            // Proceed to add the booking if reservation is successful
            String insertQuery = "INSERT INTO Bookings (trainID, passengerID, coachType, numberOfSeats) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                pstmt.setString(1, this.trainID);
                pstmt.setInt(2, this.passengerID);
                pstmt.setString(3, this.coachType);
                pstmt.setInt(4, this.numberOfSeats);
    
                int rowsInserted = pstmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Booking confirmed successfully!");
                } else {
                    System.out.println("Failed to confirm booking.");
                }
            }
        } catch (Exception e) {
            System.out.println("Booking failed: " + e.getMessage());
        }
    }

    // Method to cancel booking (removes the row with this bookingID from the database)
    public static void cancelBooking(Connection conn, int bookingId) {
        // First, retrieve the trainID, coachType, and numberOfSeats for this booking from the Bookings table
        String selectQuery = "SELECT trainID, coachType, numberOfSeats FROM Bookings WHERE bookingID = ?";
        try (PreparedStatement pstmtSelect = conn.prepareStatement(selectQuery)) {
            pstmtSelect.setInt(1, bookingId);
            ResultSet resultSet = pstmtSelect.executeQuery();
    
            // If no booking is found, return early
            if (!resultSet.next()) {
                System.out.println("No booking found with ID: " + bookingId);
                return;
            }
    
            // Extract the necessary data from the result set
            String trainID = resultSet.getString("trainID");
            String coachType = resultSet.getString("coachType");
            int numberOfSeats = resultSet.getInt("numberOfSeats");
            String getID="Select id from Train where TrainID = ?";
            int tid=0;
            try(PreparedStatement pstmt=conn.prepareStatement(getID)){
                System.out.println(trainID);
                pstmt.setString(1,trainID);
                ResultSet rst=pstmt.executeQuery();
                if (rst.next()) { // Check if there is at least one row
                    tid = rst.getInt("id");
                    System.out.println(tid);
                } else {
                    System.out.println("No train found with TrainID: " + trainID);
                }

            }
            catch(SQLException e){
                System.out.println("Couldn't get the trainID");
            }
            // Now, release the seats in the Coach table
            Coach.releaseSeat( tid, numberOfSeats, coachType,conn);
    
            // Then, delete the booking from the Bookings table
            String deleteQuery = "DELETE FROM Bookings WHERE bookingID = ?";
            try (PreparedStatement pstmtDelete = conn.prepareStatement(deleteQuery)) {
                pstmtDelete.setInt(1, bookingId);
                int rowsDeleted = pstmtDelete.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Booking canceled and seats released successfully!");
                } else {
                    System.out.println("Failed to cancel booking with ID: " + bookingId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
}

