package client_communication;

import communication.Operation;
import communication.Request;
import communication.Response;
import main.Main;
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
}
