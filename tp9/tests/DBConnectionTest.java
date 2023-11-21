import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.SQLException;

class DBConnectionTest {

    @Test
    void testSingletonPatron() throws SQLException {
        Connection conn1 = DBConnection.getConnection();
        Connection conn2 = DBConnection.getConnection();

        assertSame(conn1, conn2, "Deux objets Connection ne devraient pas être différents");
    }

    @Test
    void testConnectionType() throws SQLException {
        Connection conn = DBConnection.getConnection();
        assertTrue(conn instanceof java.sql.Connection, "L'objet Connection n'est pas de type java.sql.Connection");
    }

    @Test
    void testDatabaseChange() throws SQLException {
        String newDBName = "test";

        boolean res = true;

        DBConnection.getConnection();
        DBConnection.setNomDB(newDBName);
        DBConnection.getConnection();

        try {
            PrincipaleJDBC.main(new String[]{});
        }catch (SQLException e){
            res = false;
        }

        assertTrue(res);
    }
}