import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private String userName;
    private String password;
    private String serverName;
    private String portNumber;
    private static String dbName = "testpersonne";
    private static Connection instance;

    private DBConnection() throws SQLException {
        userName = "root";
        password = "";
        serverName = "localhost";
        portNumber = "3306";

        Properties connectionProps = new Properties();
        connectionProps.put("user", userName);
        connectionProps.put("password", password);
        String urlDB = "jdbc:mysql://" + serverName + ":";
        urlDB += portNumber + "/" + dbName;

        instance = DriverManager.getConnection(urlDB, connectionProps);
    }

    public static synchronized Connection getConnection() throws SQLException {
        if (instance == null)
            new DBConnection();
        return instance;
    }


    public static synchronized void setNomDB(String nomDB) throws SQLException {
        if (nomDB!=null && nomDB!=dbName){
            dbName = nomDB;
            instance = null;
        }
    }



}
