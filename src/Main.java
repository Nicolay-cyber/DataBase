import java.sql.*;
public class Main {
    private static Connection connection;
    private static Statement stmt;
    private static PreparedStatement pstmt;

    public static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:main.db");
            stmt = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void main(String[] args) {
        connect();

        try {
            createTable();
            addUser("User1", "123");
            addUser("User2", "123");
            readUser("User1");
            deleteUser("User1");

            //deleteTable();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }

    }

    private static void deleteTable() throws SQLException {
        stmt.executeUpdate("DROP TABLE IF EXISTS MyTable;");
    }
    private static void addUser(String name, String password) throws SQLException {
        try{
            pstmt = connection.prepareStatement("INSERT INTO MyTable(name, password) VALUES (?, ?);");
            pstmt.setString(1, name);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("This name isn't available");
        }

    }
    private static void deleteUser(String name) throws SQLException {
        stmt.executeUpdate("DELETE FROM MyTable WHERE name = '" + name + "';");
    }
    private static void readUser(String name) throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT password FROM MyTable WHERE name = '" + name + "';");
        System.out.println(name + " " + rs.getString("password"));
    }
    private static void createTable() throws SQLException {
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS MyTable(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT UNIQUE, password TEXT);");
    }
}
