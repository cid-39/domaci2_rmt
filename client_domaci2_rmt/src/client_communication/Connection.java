package client_communication;

import java.util.LinkedList;

import communication.Operation;
import communication.Request;
import communication.Response;
import main.Main;
import model.Putovanje;
import model.Transport;
import model.User;
import model.Zemlja;

public class Connection {
	public static User login(String username, String password) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);

		Request request = new Request(Operation.LOGIN, user);
		try {
			Main.transceiver.send(request);
			Response response = (Response) Main.transceiver.recieve();
			
			if (response.getException() != null) {
				response.getException().printStackTrace();
				return null;
			}
			user = (User) response.getData();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return user;
	}
	
	public static User register(User reg_user) {
		Request request = new Request(Operation.REGISTER, reg_user);
		User user;
		try {
			Main.transceiver.send(request);
			Response response = (Response) Main.transceiver.recieve();
			
			if (response.getException() != null) {
				response.getException().printStackTrace();
				return null;
			}
			user = (User) response.getData();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return user;
	}
	
	public static LinkedList<Putovanje> get_putovanja(int uid) {
		LinkedList<Putovanje> put;
		@SuppressWarnings("removal")
		Integer id = new Integer(uid);
		Request request = new Request(Operation.GET_PUT, id);
		try {
			Main.transceiver.send(request);
			Response response = (Response) Main.transceiver.recieve();
			
			if (response.getException() != null) {
				response.getException().printStackTrace();
				return null;
			}
			put = (LinkedList<Putovanje>) response.getData();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return put;
	}
	
	public static Transport get_transport(String naziv) {
		Transport ret;
		Request request = new Request(Operation.GET_TRANS, naziv);
		try {
			Main.transceiver.send(request);
			Response response = (Response) Main.transceiver.recieve();
			
			if (response.getException() != null) {
				response.getException().printStackTrace();
				return null;
			}
			ret = (Transport) response.getData();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return ret;
	}
	
	public static Zemlja get_zemlja(String naziv) {
		Zemlja ret;
		Request request = new Request(Operation.GET_ZEMLJA, naziv);
		try {
			Main.transceiver.send(request);
			Response response = (Response) Main.transceiver.recieve();
			
			if (response.getException() != null) {
				response.getException().printStackTrace();
				return null;
			}
			ret = (Zemlja) response.getData();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return ret;
	}
	
	public static void updatePutovanje (Putovanje putovanje) {
		Request request = new Request(Operation.UPDATE_PUTOVANJE, putovanje);
		try {
			Main.transceiver.send(request);
			Response response = (Response) Main.transceiver.recieve();
			
			if (response.getException() != null) {
				response.getException().printStackTrace();
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		return;
	}
	
	public static void insertPutovanje (Putovanje putovanje) {
		Request request = new Request(Operation.INSERT_PUTOVANJE, putovanje);
		try {
			Main.transceiver.send(request);
			Response response = (Response) Main.transceiver.recieve();
			
			if (response.getException() != null) {
				response.getException().printStackTrace();
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		return;
	}
}
