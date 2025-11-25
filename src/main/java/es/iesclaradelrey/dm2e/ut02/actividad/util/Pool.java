package es.iesclaradelrey.dm2e.ut02.actividad.util;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.pool.HikariPool;
import lombok.Getter;

import java.sql.Connection;
import java.sql.SQLException;

public class Pool {
    // Declaramos variables
    private static final String CONNECTION_STRING = "jdbc:postgresql://localhost:5432/Chinook";
    private static final String USERNAME = "Chinook";
    private static final String PASSWORD = "Chinook";
    // Declaramos la conexion, que esta envuelta en HikariPool
    private final HikariPool dataSourceHikari;
    // Declaramos la variable estatica final
    @Getter
    private final static Pool instance = new Pool();

    // Singleton
    private Pool() {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(CONNECTION_STRING);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);

        dataSourceHikari = new HikariPool(config);
    }

    // Donde me devuelve la conexion
    public Connection getConnection() throws SQLException {
        return dataSourceHikari.getConnection();
    }
}