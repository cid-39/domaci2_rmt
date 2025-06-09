package broker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.LinkedList;

import common_domaci2_rmt.Putovanje;
import common_domaci2_rmt.User;
import common_domaci2_rmt.Zemlja;


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

    public User getUser (int id) {
    	try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM User WHERE id = "+id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) { 
            	return new User(id, rs.getString("username"), rs.getString("password"), rs.getString("ime"), rs.getString("prezime"), rs.getString("email"), rs.getString("jmbg"), rs.getString("broj_pasosa"), rs.getDate("datum_rodjenja").toLocalDate());        
            }
        } catch (SQLException e) {
            System.out.println("DBBroker: error in getUser");
            e.printStackTrace();
        }
		return null;
    }
    
    public void insertPutovanje(Putovanje putovanje) {
    	try {
    		LinkedList<Integer> zemlje = new LinkedList<Integer>();
    		for (Zemlja z : putovanje.getZemlja()) {
    			zemlje.add(z.getId());
    		}
    		
    		PreparedStatement statement = connection.prepareStatement("INSERT INTO Putovanje (user_id, zemlja_id, datum_prijave, datum_ulaska, datum_izlaska, transport_id, placa_se) VALUES (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, putovanje.getPutnik().getId());
			statement.setInt(2, zemlje.get(0));
			statement.setDate(3, Date.valueOf(putovanje.getDatum_prijave()));
			statement.setDate(4, Date.valueOf(putovanje.getDatum_ulaska()));
			statement.setDate(5, Date.valueOf(putovanje.getDatum_izlaska()));
			statement.setInt(6, putovanje.getTransport().getId());
			statement.setBoolean(7, putovanje.isPlaca_se());
			statement.executeUpdate();				
			ResultSet rsId = statement.getGeneratedKeys();
			if (rsId.next()) {
                putovanje.setId(rsId.getInt(1));
            }
			zemlje.remove(0);
			
    		for (int zemlja_id : zemlje) {
    			PreparedStatement statement2 = connection.prepareStatement("INSERT INTO Putovanje (id, user_id, zemlja_id, datum_prijave, datum_ulaska, datum_izlaska, transport_id, placa_se) VALUES (?,?,?,?,?,?,?,?)");
    			statement2.setInt(1, putovanje.getId());
    			statement2.setInt(2, putovanje.getPutnik().getId());
    			statement2.setInt(3, zemlja_id);
    			statement2.setDate(4, Date.valueOf(putovanje.getDatum_prijave()));
    			statement2.setDate(5, Date.valueOf(putovanje.getDatum_ulaska()));
    			statement2.setDate(6, Date.valueOf(putovanje.getDatum_izlaska()));
    			statement2.setInt(7, putovanje.getTransport().getId());
    			statement2.setBoolean(8, putovanje.isPlaca_se());
    			statement2.executeUpdate();				
			}
    		
    		connection.commit();
        } catch (SQLException e) {
            System.out.println("DBBroker: error in insertPutovanje");
            e.printStackTrace();
            try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
    }
}
