import java.sql .*;

public class Database {
    private Connection connection;
    private Statement statement;

    // Constructor
    public Database(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost: 3306/db_akademik", "root", "");
            statement = connection.createStatement();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    // Digunakan untuk SELECT
    public ResultSet selectQuery(String sql){
        try {
            statement.executeQuery(sql);
            return statement.getResultSet();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    // Digunakan untuk DELETE, INSERT, UPDATE
    public int insertUpdateDeleteQuery(String sql){
        try {
            return statement.executeUpdate(sql);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    // Getter
    public Statement getStatement(){
        return statement;
    }
}
