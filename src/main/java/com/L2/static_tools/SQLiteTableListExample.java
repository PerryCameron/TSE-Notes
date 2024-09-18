package com.L2.static_tools;

import com.L2.mvci_main.MainController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLiteTableListExample {


    public static void main(String[] args) {
        // Path to your SQLite database file
//        String url = "jdbc:sqlite:C:\\Users\\sesa91827\\AppData\\Roaming\\TIPI\\DB\\SKM-Sync.cblite2\\db.sqlite3";
        String url = "jdbc:sqlite:C:\\Users\\sesa91827\\Downloads\\test.db";

        // Connect to the database and retrieve the list of tables
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("Connection to SQLite has been established.");

                // Create a statement object to execute SQL queries
                Statement stmt = conn.createStatement();

                // Query to get the list of all tables
                String query = "SELECT name FROM sqlite_master WHERE type='table';";
                ResultSet rs = stmt.executeQuery(query);

                // Print the names of all tables in the database
                System.out.println("Tables in the database:");
                while (rs.next()) {
                    String tableName = rs.getString("name");
                    System.out.println(tableName);
                }
            }
        } catch (Exception e) {
            System.out.println("Error occurred while connecting to the database or querying tables.");
            e.printStackTrace();
        }
    }
}

