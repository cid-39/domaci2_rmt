package client_communication;

import java.time.LocalDate;
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
	
	public static User register(User reg_user) throws Exception {
		Request request = new Request(Operation.REGISTER, reg_user);
		User user;
		try {
			Main.transceiver.send(request);
			Response response = (Response) Main.transceiver.recieve();
			
			if (response.getException() != null) {
				throw response.getException();
			}
			user = (User) response.getData();
		} catch (Exception e) {
			throw e;
		}
		
		return user;
	}
	
	public static LinkedList<Putovanje> getPutovanja(User user) throws Exception {
		LinkedList<Putovanje> put;
		Request request = new Request(Operation.GET_PUT, user);
		try {
			Main.transceiver.send(request);
			Response response = (Response) Main.transceiver.recieve();
			
			if (response.getException() != null) {
				throw response.getException();
			}
			put = (LinkedList<Putovanje>) response.getData();
		} catch (Exception e) {
			throw e;
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
	
	public static void updatePutovanje (Putovanje putovanje) throws Exception {
		try {     // this is clunky way to not send broken putovanje
			Putovanje check = new Putovanje(putovanje.getPutnik(), putovanje.getZemlja(), putovanje.getDatum_prijave(), putovanje.getDatum_ulaska(), putovanje.getDatum_izlaska(), putovanje.getTransport(), putovanje.isPlaca_se());     
		} catch (Exception e) {
			throw e;
		}
		Request request = new Request(Operation.UPDATE_PUTOVANJE, putovanje);
		try {
			Main.transceiver.send(request);
			Response response = (Response) Main.transceiver.recieve();
			
			if (response.getException() != null) {
				throw response.getException();
			}
		} catch (Exception e) {
			throw e;
		}
		
		return;
	}
	
	public static void insertPutovanje (Putovanje putovanje) throws Exception {
		Request request = new Request(Operation.INSERT_PUTOVANJE, putovanje);
		try {
			Main.transceiver.send(request);
			Response response = (Response) Main.transceiver.recieve();
			
			if (response.getException() != null) {
				throw response.getException();
			}
		} catch (Exception e) {
			throw e;
		}
		
		return;
	}

	public static void insertGuestPutovanje(Putovanje putovanje) throws Exception {
		Request request = new Request(Operation.INSERT_GUEST_PUTOVANJE, putovanje);
		try {
			Main.transceiver.send(request);
			Response response = (Response) Main.transceiver.recieve();
			
			if (response.getException() != null) {
				throw response.getException();
			}
		} catch (Exception e) {
			throw e;
		}
		
		return;
	}
}
