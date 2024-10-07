package com.L2.static_tools;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public class DatabaseConfig {

    // Configure the SQLite DataSource manually
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        String url = "jdbc:sqlite:" + ApplicationPaths.secondaryDbDirectory.resolve("notes.db");  // Path to the SQLite database file
        // Specify the SQLite database file location
        dataSource.setUrl(url);
        return dataSource;
    }

    // Manually create a JdbcTemplate instance using the DataSource
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(getDataSource());
    }
}
