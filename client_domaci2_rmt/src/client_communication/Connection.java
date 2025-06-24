package client_communication;

import java.util.LinkedList;

import communication.Operation;
import communication.Request;
import communication.Response;
import main.Main;
import model.Putovanje;
import model.User;

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
}
