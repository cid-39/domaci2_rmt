package broker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBBroker {
	private void DBBroker() {}
	private static final DBBroker instance = new DBBroker();
	public static DBBroker get_instance() {
		return instance;
	}
	
	private Connection connection;
    
    public void connect() throws SQLException{
        try {
            String url = "jdbc:mysql://localhost:3306/domaci2RMT";
            String user = "root";
            String pass = "root";
            connection = DriverManager.getConnection(url,user,pass);
            connection.setAutoCommit(false);
            System.out.println("DBBroker: connection established");
        } catch (SQLException e) {
            System.out.println("DBBroker: error establishing connection");
            e.printStackTrace();
            throw e;
        }
    }
    
    public void disconnect() throws SQLException {
        if (connection==null || connection.isClosed())
            return;
        try {
            connection.close();
            System.out.println("DBBroker: disconnected");
        } catch (SQLException e) {
            System.out.println("DBBroker: error while disconnecting");
            e.printStackTrace();
            throw e;
        }
    }
}
