package es.iesclaradelrey.dm2e.ut02.actividad.util;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.pool.HikariPool;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {
    private static final String CONNECTION_STRING = "jdbc:postgresql://localhost:5432/Chinook";
    private static final String USERNAME = "Chinook";
    private static final String PASSWORD = "Chinook";
    private final HikariPool dataSourceHikari;

    private static ConnectionPool instance;
    private ConnectionPool() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(CONNECTION_STRING);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.setMaximumPoolSize(10);
        dataSourceHikari = new HikariPool(config);
    }

    public static synchronized ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return dataSourceHikari.getConnection();
    }
}