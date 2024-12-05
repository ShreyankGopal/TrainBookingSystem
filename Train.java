import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class Train {
    private String trainID;
    private Pair<String, String> route; // Pair<Source, Destination>
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private List<String> availableClasses;

    // Constructor
    public Train(String trainID, String source, String destination, LocalDateTime departureTime, LocalDateTime arrivalTime, List<String> availableClasses) {
        this.trainID = trainID;
        this.route = new Pair<>(source, destination);
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.availableClasses = availableClasses;
    }

    // Getters and Setters

    public String getTrainID() {
        return trainID;
    }

    public void setTrainID(String trainID) {
        this.trainID = trainID;
    }

    public Pair<String, String> getRoute() {
        return route;
    }

    public void setRoute(Pair<String, String> route) {
        this.route = route;
    }

    public String getSource() {
        return route.getFirst(); // Accessing the source from the Pair
    }

    public void setSource(String source) {
        this.route = new Pair<>(source, route.getFirst());
    }

    public String getDestination() {
        return route.getSecond(); // Accessing the destination from the Pair
    }

    public void setDestination(String destination) {
        this.route = new Pair<>(route.getSecond(), destination);
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public List<String> getAvailableClasses() {
        return availableClasses;
    }

    public void setAvailableClasses(List<String> availableClasses) {
        this.availableClasses = availableClasses;
    }

    // Methods
    public static String getSchedule(Connection conn) {
        String query = "SELECT trainID, departure, arrival, route_start, route_end FROM Train";
        StringBuilder output = new StringBuilder();
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            // Set the trainID parameter

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("Train ID: " + rs.getString("trainID"));
                System.out.println("Route: " + rs.getString("route_start") + " -> " + rs.getString("route_end"));
                System.out.println("Departure: " + rs.getTime("departure"));
                System.out.println("Arrival: " + rs.getTime("arrival"));
                System.out.println("------------------------------");

                output.append(rs.getString("TrainID")).append(" ").append(rs.getString("route_start")).append(" ")
                .append(rs.getString("route_end")).append(" ").append(rs.getTime("departure")).append(" ").append(rs.getTime("arrival"))
                .append(" ,");
            }
            // Close the ResultSet
            rs.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error fetching the train schedule.");
        }
        //String output = rs.getString("trainID")+" "+rs.getString("route_start") + " " + rs.getString("route_end")+rs.getTime("departure")+rs.getTime("arrival");
        return output.toString();
    }

    public boolean checkAvailability(String coachType) {
        return availableClasses.contains(coachType);
    }
    public static String printAvailableClasses(Connection conn, String trainID) {
        String query = "SELECT coachtypes FROM Train WHERE trainID = ?";
        StringBuilder output = new StringBuilder();
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, trainID);

            ResultSet resultSet = pstmt.executeQuery();
            System.out.println("Available Coaches for Train " + trainID);
            if (!resultSet.isBeforeFirst()) { // Check if the cursor is before the first row
                System.out.println("The ResultSet is empty. No rows found.");
            }
            while (resultSet.next()) {
                System.out.println("- " + resultSet.getString("coachtypes"));
                output.append(resultSet.getString("coachtypes"));
            }
            System.out.println(output.toString()); //just to see the output
        } catch (SQLException e) {
            System.out.println("Error fetching available classes for Train ID: " + trainID);
            e.printStackTrace();
        }
        return output.toString();
    }
}
