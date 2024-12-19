package DataBase;

import Commands.Elem;

import java.sql.*;
import java.util.Properties;

public class ConnectionDB {
    private static ConnectionDB INSTANCE;

    private Connection conn;

    public static ConnectionDB getINSTANCE() throws SQLException {
        if (INSTANCE == null) {
            INSTANCE = new ConnectionDB();
            INSTANCE.conn = getConnection("postgres", "postgres", "3310");
        }
        return INSTANCE;
    }

    private static Connection getConnection(String db, String login, String psswd) throws SQLException {
        String url = "jdbc:postgresql://localhost/" + db;
        Properties props = new Properties();
        props.setProperty("user", login);
        props.setProperty("password", psswd);
        return DriverManager.getConnection(url, props);
    }

    public void execute(String query, String login, Elem elem) throws SQLException {
        Connection conn = INSTANCE.conn;
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.executeUpdate();
        PreparedStatement history = conn.prepareStatement("INSERT INTO history VALUES ('" + login + "', (SELECT id FROM commands WHERE title='" + elem.toString() + "'), current_timestamp)");
        history.executeUpdate();
    }

    public ResultSet selectRegistration(String query) throws SQLException {
        Connection conn = INSTANCE.conn;
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    public ResultSet select(String query, String login, Elem elem) throws SQLException {
        Connection conn = INSTANCE.conn;
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        PreparedStatement history = conn.prepareStatement("INSERT INTO history VALUES ('" + login + "', (SELECT id FROM commands WHERE title='" + elem.toString() + "'), current_timestamp)");
        history.executeUpdate();
        return rs;
    }
}
