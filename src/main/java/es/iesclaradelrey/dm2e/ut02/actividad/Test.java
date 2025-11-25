package es.iesclaradelrey.dm2e.ut02.actividad;



import es.iesclaradelrey.dm2e.ut02.actividad.util.Pool;

import java.sql.Connection;
import java.sql.SQLException;

public class Test {
    public static void main(String[] args) throws SQLException {
        Connection pool = Pool.getInstance().getConnection();
        System.out.println(pool);

    }
}
