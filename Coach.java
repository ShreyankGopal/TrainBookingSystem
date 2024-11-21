import java.util.List;             // For List
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
class Coach extends Train {
    private String coachID;
    private String coachType;
    private List<Pair<Integer, Integer>> seatsAvail; // List of (seatID, avail)

    // Constructor
    public Coach(String trainID, String source, String destination, LocalDateTime departureTime, LocalDateTime arrivalTime,
                 List<String> availableClasses, String coachID, String coachType, int totalSeats) {
        super(trainID, source, destination, departureTime, arrivalTime, availableClasses);
        this.coachID = coachID;
        this.coachType = coachType;
        seatsAvail = new ArrayList<>();
        for (int i = 1; i <= totalSeats; i++) {
            seatsAvail.add(new Pair<>(i, 1)); // Initialize all seats as available (1)
        }
    }

    // Methods
    public void viewSeatAvail() {
        System.out.println("Coach ID: " + coachID + ", Type: " + coachType);
        for (Pair<Integer, Integer> seat : seatsAvail) {
            System.out.println("Seat " + seat.getFirst() + " - " + (seat.getSecond() == 1 ? "Available" : "Occupied"));
        }
    }

    public static void reserveSeat(Connection conn, int trainID, String coachType, int numOfSeats) throws Exception {
        String selectQuery = "SELECT " + coachType + " FROM Coach WHERE trainID = ?";
        String updateQuery = "UPDATE Coach SET " + coachType + " = ? WHERE trainID = ?";

        try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
            selectStmt.setInt(1, trainID);

            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    int availableSeats = rs.getInt(coachType);
                    if (availableSeats < numOfSeats) {
                        throw new Exception("Not enough seats available in " + coachType + " coach.");
                    }

                    int updatedSeats = availableSeats - numOfSeats;

                    try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                        updateStmt.setInt(1, updatedSeats);
                        updateStmt.setInt(2, trainID);
                        updateStmt.executeUpdate();
                        System.out.println(numOfSeats + " seat(s) reserved in " + coachType + " coach for train " + trainID);
                    }
                } else {
                    throw new Exception("No coach found for trainID: " + trainID);
                }
            }
        }
    }

public static void releaseSeat(int trainID, int seats, String coachType, Connection conn) {
    // SQL query to update the seat count in the Coaches table
    String query = "UPDATE Coach SET " + coachType + " = " + coachType + " + ? WHERE trainID = ?";
    
    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
        // Set the parameters for the query
        pstmt.setInt(1, seats);  // Number of seats to add
        pstmt.setInt(2, trainID);
        
        // Execute the update query
        int rowsUpdated = pstmt.executeUpdate();
        
        if (rowsUpdated > 0) {
            System.out.println("Seats released successfully for coach " + coachType + " on Train ID " + trainID + ".");
        } else {
            System.out.println("No matching coach found for Train ID " + trainID + " and Coach Type " + coachType + ".");
        }
    } catch (SQLException e) {
        System.out.println("Error while releasing seats: " + e.getMessage());
    }
}


}

