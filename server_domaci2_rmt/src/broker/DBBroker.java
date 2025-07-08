package broker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.LinkedList;

import javax.management.RuntimeErrorException;

import model.Putovanje;
import model.Transport;
import model.User;
import model.Zemlja;



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
    
    public User loginUser(String username, String pass) {
    	try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM User WHERE username = ? AND password = ?");
            statement.setString(1, username);
            statement.setString(2, pass);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) { 
            	return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("ime"), rs.getString("prezime"), rs.getString("email"), rs.getString("jmbg"), rs.getString("broj_pasosa"), rs.getDate("datum_rodjenja").toLocalDate());       
            }
        } catch (SQLException e) {
            System.out.println("DBBroker: error in loginUser");
            e.printStackTrace();
        }
		return null;
    }
    
    public void updateUserCreds(User user) {
    	try {
    		PreparedStatement statement = connection.prepareStatement("UPDATE User SET username=?, password=?, email=? WHERE jmbg=?");
    		statement.setString(1, user.getUsername());
    		statement.setString(2, user.getPassword());
   		 	statement.setString(3, user.getEmail());
   		 	statement.setString(4, user.getJmbg());
   		 	int affectedrows = statement.executeUpdate();
   		 	if (affectedrows != 1) throw new RuntimeException("Updated rows != 1");
   		 	connection.commit(); 	
    	} catch (SQLException e) {
    		try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		System.out.println("DBBroker: error in updateUserCreds");
    		e.printStackTrace();
       }
    }
    
    public User getUser (String jmbg) {
    	try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM User WHERE jmbg = "+jmbg);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) { 
            	return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("ime"), rs.getString("prezime"), rs.getString("email"), rs.getString("jmbg"), rs.getString("broj_pasosa"), rs.getDate("datum_rodjenja").toLocalDate());        
            }
        } catch (SQLException e) {
            System.out.println("DBBroker: error in getUser");
            e.printStackTrace();
        }
		return null;
    }
    
    public User getPerson (String jmbg) {
    	try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Population WHERE jmbg = "+jmbg);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) { 
            	return new User(0, "", "", rs.getString("ime"), rs.getString("prezime"), "", rs.getString("jmbg"), rs.getString("broj_pasosa"), rs.getDate("datum_rodjenja").toLocalDate());        
            }
        } catch (SQLException e) {
            System.out.println("DBBroker: error in getUser");
            e.printStackTrace();
        }
		return null;
    }
    
    public void insertUser(User user) {
    	try {
    		PreparedStatement statement = connection.prepareStatement("INSERT INTO User (username, password, ime, prezime, email, jmbg, broj_pasosa, datum_rodjenja) VALUES (?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, user.getUsername());
			statement.setString(2, user.getPassword());
			statement.setString(3, user.getIme());
			statement.setString(4, user.getPrezime());
			statement.setString(5, user.getEmail());
			statement.setString(6, user.getJmbg());
			statement.setString(7, user.getBroj_pasosa());
			statement.setDate(8, Date.valueOf(user.getDatum_rodjenja()));
			statement.executeUpdate();				
			ResultSet rsId = statement.getGeneratedKeys();
			if (rsId.next()) {
                user.setId(rsId.getInt(1));
            }
    		connection.commit();
        } catch (SQLException e) {
            System.out.println("DBBroker: error in insertUser");
            e.printStackTrace();
            try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
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
    
    public LinkedList<Putovanje> getPutovanja(int userId) {
    	LinkedList<Putovanje> retList = new LinkedList<Putovanje>();
    	try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Putovanje WHERE user_id = "+userId+" ORDER BY id, zemlja_id");
            ResultSet rs = statement.executeQuery();
            Putovanje tempPutovanje = new Putovanje(null, null, null, null, null, null, false);
            tempPutovanje.setId(Integer.MIN_VALUE);
            while (rs.next()) { 
            	if ( rs.getInt("id")==tempPutovanje.getId() ) {
            		LinkedList<Zemlja> zemlje = tempPutovanje.getZemlja();
            		zemlje.add(getZemlja(rs.getInt("zemlja_id")));
            		tempPutovanje.setZemlja(zemlje);
            		continue;
            	}
            	if(tempPutovanje.getId() != Integer.MIN_VALUE) retList.add(tempPutovanje);
            	
            	User putnik = getUser(rs.getInt("user_id"));
            	Transport transport = getTransport(rs.getInt("transport_id"));
            	LinkedList<Zemlja> zemlje = new LinkedList<Zemlja>();
            	zemlje.add(getZemlja(rs.getInt("zemlja_id")));

            	tempPutovanje = new Putovanje(putnik, zemlje, rs.getDate("datum_prijave").toLocalDate(), rs.getDate("datum_ulaska").toLocalDate(), rs.getDate("datum_izlaska").toLocalDate(), transport, rs.getBoolean("placa_se"));
            	tempPutovanje.setId(rs.getInt("id"));
            }
            if (tempPutovanje.getId() != Integer.MIN_VALUE)
            	retList.add(tempPutovanje);
        } catch (SQLException e) {
            System.out.println("DBBroker: error in getPutovanja");
            e.printStackTrace();
        }
		return retList;
    }
    
    public LinkedList<Putovanje> getPutovanja(String jmbg, String broj_pasosa) {
    	LinkedList<Putovanje> retList = new LinkedList<Putovanje>();
    	try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Putovanje WHERE user_id in (SELECT id FROM User WHERE jmbg = ? AND broj_pasosa = ?) ORDER BY id, zemlja_id");
            statement.setString(1, jmbg);
            statement.setString(2, broj_pasosa);
            ResultSet rs = statement.executeQuery();
            Putovanje tempPutovanje = new Putovanje(null, null, null, null, null, null, false);
            tempPutovanje.setId(Integer.MIN_VALUE);
            while (rs.next()) { 
            	if ( rs.getInt("id")==tempPutovanje.getId() ) {
            		LinkedList<Zemlja> zemlje = tempPutovanje.getZemlja();
            		zemlje.add(getZemlja(rs.getInt("zemlja_id")));
            		tempPutovanje.setZemlja(zemlje);
            		continue;
            	}
            	if(tempPutovanje.getId() != Integer.MIN_VALUE) retList.add(tempPutovanje);
            	
            	User putnik = getUser(rs.getInt("user_id"));
            	Transport transport = getTransport(rs.getInt("transport_id"));
            	LinkedList<Zemlja> zemlje = new LinkedList<Zemlja>();
            	zemlje.add(getZemlja(rs.getInt("zemlja_id")));

            	tempPutovanje = new Putovanje(putnik, zemlje, rs.getDate("datum_prijave").toLocalDate(), rs.getDate("datum_ulaska").toLocalDate(), rs.getDate("datum_izlaska").toLocalDate(), transport, rs.getBoolean("placa_se"));
            	tempPutovanje.setId(rs.getInt("id"));
            }
            retList.add(tempPutovanje);
        } catch (SQLException e) {
            System.out.println("DBBroker: error in getPutovanja");
            e.printStackTrace();
        }
		return retList;
	}
    
    public void updatePutovanje(Putovanje putovanje) {
    	try {
    		// can't be bothered :)
    		removePutovanje(putovanje.getId());
    		insertPutovanje(putovanje);
    		connection.commit();
    		// yes, putovanje id will change, no, im not fixing it
        } catch (SQLException e) {
            System.out.println("DBBroker: error in updatePutovanje");
            e.printStackTrace();
            try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
    }
    
   private Zemlja getZemlja(int id) {
    	try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Zemlja WHERE id = "+id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) { 
            	return new Zemlja(id, rs.getString("naziv"));        
            }
        } catch (SQLException e) {
            System.out.println("DBBroker: error in getZemlja");
            e.printStackTrace();
        }
		return null;
    }
   
   public Zemlja getZemlja(String naziv) {
   	try {
           PreparedStatement statement = connection.prepareStatement("SELECT * FROM Zemlja WHERE naziv = ?");
           statement.setString(1, naziv);
           ResultSet rs = statement.executeQuery();
           if (rs.next()) { 
           	return new Zemlja(rs.getInt("id"), rs.getString("naziv"));        
           }
       } catch (SQLException e) {
           System.out.println("DBBroker: error in getZemlja");
           e.printStackTrace();
       }
		return null;
   }
    
    private Transport getTransport(int id) {
    	try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Transport WHERE id = "+id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) { 
            	return new Transport(id, rs.getString("tip"));        
            }
        } catch (SQLException e) {
            System.out.println("DBBroker: error in getTransport");
            e.printStackTrace();
        }
		return null;
    }
    
    public Transport getTransport(String naziv) {
    	try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Transport WHERE tip = ?");
            statement.setString(1, naziv);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) { 
            	return new Transport(rs.getInt("id"), rs.getString("tip"));        
            }
        } catch (SQLException e) {
            System.out.println("DBBroker: error in getTransport");
            e.printStackTrace();
        }
		return null;
    }
    
    public Putovanje getPutovanje(int id) {
    	Putovanje putovanje=new Putovanje(null, null, null, null, null, null, false);
    	try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Putovanje WHERE id = "+id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
            	User putnik = getUser(rs.getInt("user_id"));
            	Transport transport = getTransport(rs.getInt("transport_id"));
            	LinkedList<Zemlja> zemlje = new LinkedList<Zemlja>();
            	zemlje.add(getZemlja(rs.getInt("zemlja_id")));

            	putovanje = new Putovanje(putnik, zemlje, rs.getDate("datum_prijave").toLocalDate(), rs.getDate("datum_ulaska").toLocalDate(), rs.getDate("datum_izlaska").toLocalDate(), transport, rs.getBoolean("placa_se"));
            	putovanje.setId(rs.getInt("id"));
            }
            while (rs.next()) {
            	LinkedList<Zemlja> zemlje = putovanje.getZemlja();
            	zemlje.add(getZemlja(rs.getInt("zemlja_id")));
            	putovanje.setZemlja(zemlje);
            }
        } catch (SQLException e) {
            System.out.println("DBBroker: error in getPutovanja");
            e.printStackTrace();
        }
		return putovanje;
    }

    private int removePutovanje(int id) {
    	try {
    		 PreparedStatement statement = connection.prepareStatement("DELETE FROM Putovanje WHERE id="+id);
             int affectedrows = statement.executeUpdate();
             return affectedrows;
        } catch (SQLException e) {
            System.out.println("DBBroker: error in removePutovanje");
            e.printStackTrace();
        }
    	return 0;
    }

}
