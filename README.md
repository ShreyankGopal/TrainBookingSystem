# Train Management System

## Table of Contents
- [About](#about)
- [Features](#features)
- [Installation](#installation)
- [Technologies Used](#technologies-used)


---

## About
The **Train Management System** is a Java-based application that facilitates managing train schedules, passenger bookings, and seat availability. It provides admin features to edit train schedules, reset seat availability, and generate reports, along with passenger functionalities like booking and viewing available seats.

---

## Features
### Admin Features
- Edit train schedules (departure and arrival times).
- Change seat availability for all coach types (AC, SC, GE) for a particular train.
- Generate reports of all trains and their coach details.
- Login and logout functionality.

### Passenger Features
- Book and cancel tickets.
- View available seats.
- Login, logout, and manage profile details.

---

## Installation

1. Set up the database: Create the following mysql schema.
 ### `User` Table Schema

| Field    | Type         | Null | Key | Default | Extra          |
|----------|--------------|------|-----|---------|----------------|
| `userID` | int          | NO   | PRI | NULL    | auto_increment |
| `name`   | varchar(255) | NO   |     | NULL    |                |
| `email`  | varchar(255) | NO   |     | NULL    |                |
| `phone`  | varchar(15)  | NO   |     | NULL    |                |
| `password` | varchar(255) | NO |     | NULL    |                |
| `active` | int          | YES  |     | 0       |                |

 ### `Passenger` Table Schema

| Field       | Type        | Null | Key | Default | Extra          |
|-------------|-------------|------|-----|---------|----------------|
| `passengerID` | int       | NO   | PRI | NULL    | auto_increment |
| `userID`    | int         | NO   | MUL | NULL    |                |
| `age`       | int         | NO   |     | NULL    |                |
| `gender`    | varchar(50) | NO   |     | NULL    |                |

userID in `Passenger` has a foreign key to userID in `User` table.
### `Train` Table Schema

| Field       | Type         | Null | Key | Default | Extra          |
|-------------|--------------|------|-----|---------|----------------|
| `id`        | int          | NO   | PRI | NULL    | auto_increment |
| `TrainID`   | varchar(50)  | NO   | UNI | NULL    |                |
| `route_start` | varchar(100) | NO   |     | NULL    |                |
| `route_end`   | varchar(100) | NO   |     | NULL    |                |
| `departure` | time         | NO   |     | NULL    |                |
| `arrival`   | time         | NO   |     | NULL    |                |
| `coachtypes` | text         | NO   |     | NULL    |                |

### `Coach` Table Schema

| Field     | Type | Null | Key | Default | Extra          |
|-----------|------|------|-----|---------|----------------|
| `coachID` | int  | NO   | PRI | NULL    | auto_increment |
| `trainID` | int  | NO   | MUL | NULL    |                |
| `AC`      | int  | YES  |     | 0       |                |
| `GE`      | int  | YES  |     | 0       |                |
| `SC`      | int  | YES  |     | 0       |                |

the trainID in `Coach` tables has a foreign key to TrainID in `Train` table.

### `Bookings` Table Schema

| Field         | Type        | Null | Key | Default | Extra          |
|---------------|-------------|------|-----|---------|----------------|
| `bookingID`   | int         | NO   | PRI | NULL    | auto_increment |
| `trainID`     | varchar(20) | NO   | MUL | NULL    |                |
| `passengerID` | int         | NO   | MUL | NULL    |                |
| `coachType`   | varchar(50) | NO   |     | NULL    |                |
| `numberOfSeats` | int       | NO   |     | NULL    |                |

trainID in `Bookings` has a foreign key to TrainID in `Train` table

### `Admin` Table Schema

| Field   | Type | Null | Key | Default | Extra          |
|---------|------|------|-----|---------|----------------|
| `adminID` | int  | NO   | PRI | NULL    | auto_increment |
| `userID`  | int  | YES  | MUL | NULL    |                |

userID in `Admin` has a foreign key to userID in `User` table.

### `Train` Table Initial Data

| id  | TrainID  | route_start | route_end | departure | arrival  | coachtypes |
|-----|----------|-------------|-----------|-----------|----------|------------|
|  1  | TRAIN001 | Delhi       | Mumbai    | 06:00:00  | 18:30:00 | AC, SC, GE |
|  2  | TRAIN002 | Mumbai      | Chennai   | 07:30:00  | 21:45:00 | AC, SC, GE |
|  3  | TRAIN003 | Chennai     | Kolkata   | 05:15:00  | 20:00:00 | AC, SC, GE |
|  4  | TRAIN004 | Kolkata     | Delhi     | 11:30:00  | 14:30:00 | AC, SC, GE |

---

### `Coach` Table Initial Data

| coachID | trainID | AC   | GE   | SC   |
|---------|---------|------|------|------|
|       1 |       1 | 100  | 100  | 100  |
|       2 |       2 | 100  | 100  | 100  |
|       3 |       3 | 100  | 100  | 100  |
|       4 |       4 | 100  | 100  | 100  |


3. Install `hamcrest-core-1.3.jar` , `junit-4.13.2.jar` , `mysql-connector-j-8.3.0.jar` files.

4. Java Project Directory Structure

This is the directory structure for the Java project.
Create a java project:
```
project
└───.vscode
└───bin
└───lib
│  │   hamcrest-core-1.3.jar
│  │   junit-4.13.2.jar
│  │   mysql-connector-j-8.3.0.jar
│
└───src
   │   Java files 
```
5. ```cd src```

   ```git clone https://github.com/ShreyankGopal/TrainBookingSystem.git```

## Technologies Used

- **Programming Language**: Java
- **Database**: MySQL
- **JDBC**: For database connectivity


