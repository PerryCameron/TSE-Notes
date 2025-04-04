package com.L2.static_tools;

import com.L2.BaseApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.sqlite.SQLiteDataSource;

import java.sql.SQLException;

public class DatabaseConnector {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnector.class);

    public static SQLiteDataSource getDataSource(String caller) {
        String DATABASE_URL = "jdbc:sqlite:" + BaseApplication.dataBaseLocation + "/" + BaseApplication.dataBase;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(DATABASE_URL);

        try {
            // Test the connection
            dataSource.getConnection().close();
            logger.info("Connection to SQLite has been established -> {}", caller);
        } catch (SQLException e) {
            logger.error("Failed to establish SQLite connection.", e);
        }
        return dataSource;
    }
}

