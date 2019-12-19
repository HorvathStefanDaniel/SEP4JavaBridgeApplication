package SQL;

import java.sql.*;
import java.util.Date;
import static SQL.DatabaseConnection.*;

public class SQLConnection {
    private static SQLConnection instance = null;
    private Date date = new Date();
    private Timestamp sqlDate;

    private Connection conn = null;

    //Constructor class opening connection to database
    public SQLConnection() throws SQLException {
        conn = DriverManager.getConnection(MY_SQL_URL, DB_USERNAME, DB_PASSWORD);
    }

    //Checks and return current instance of connection; creates one if none
    public static SQLConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new SQLConnection();
        }

        return instance;
    }

    //Tests if connection can be established
    private boolean openConnection() {
        try {

            conn = DriverManager.getConnection(MY_SQL_URL, DB_USERNAME, DB_PASSWORD);
            System.out.println("Open connection");

            return true;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }

    }

    //Method called from main, defines type of data and sends it to next method
    public void insertIntoDatabase(int id, double value)
            throws SQLException {
        //Checks the value of the id and sends appropriate data type to database
        switch (id) {
            case 1:
                insertMeasurement(id, "CO2", value);
                break;
            case 2:
                insertMeasurement(id, "Humidity", value);
                break;
            case 3:
                insertMeasurement(id, "Temperature", value);
                break;
            default: System.out.println("An error has occured with determining data type");
                break;
        }
    }

    //Method inserting an entry into the database
    public void insertMeasurement(int id, String type, double value) throws SQLException
    {
        sqlDate = new Timestamp(date.getTime());
        String insertStatement = "INSERT INTO \"Measurement\" (Date_Inserted, DataType, DataValue, SensorID) VALUES (?,?,?,?)";
        PreparedStatement statement = conn
                .prepareStatement(insertStatement);
        statement.setTimestamp(1, sqlDate);
        statement.setString(2, type);
        statement.setDouble(3, value);
        statement.setInt(4, id);
        statement.executeUpdate();
        System.out.println("Record inserted");

    }

    //Returns the current connection
    public Connection getConnection() {
        if (conn == null) {
            if (openConnection()) {
                System.out.println("Connection opened");
                return conn;
            } else {
                return null;
            }
        }
        return conn;
    }

    //Terminates connection to database
    public void close() {
        System.out.println("Closing connection");
        try {
            conn.close();
            conn = null;
        } catch (Exception e) {
        }
    }
}

