package handler;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;

import broker.DBBroker;
import communication.Operation;
import communication.Request;
import communication.Response;
import communication.Transceiver;
import model.User;


public class ClientHandler {
	private static DBBroker broker;
	public static void main(String[] args) {
		broker = DBBroker.get_instance();
		try {
			broker.connect();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int a = 0;
		ServerSocket socket;
		Transceiver trans = new Transceiver(null);
		try {
			socket = new ServerSocket(9000);
			trans = new Transceiver(socket.accept());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (a<10) {
			try {
				Request req = (Request) trans.polo();
				if (req.getOp() == Operation.LOGIN) {
					User user = (User) req.getArg();
					
					Response res = new Response(new Boolean(broker.loginUser(user.getUsername(), user.getPassword())), null);
					trans.marco(res);
				}
			
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
			
			a++;
		}
		
		try {
			broker.disconnect();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void registerUser(String username, String password, String ime, String prezime, String email, String jmbg,
			String broj_pasosa, LocalDate datum_rodjenja) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setIme(ime);
		user.setPrezime(prezime);
		user.setEmail(email);
		user.setJmbg(jmbg);
		user.setBroj_pasosa(broj_pasosa);
		user.setDatum_rodjenja(datum_rodjenja);
		User checkUser = broker.getPerson(user.getJmbg());
		if (!user.equals(checkUser)) {
			throw new RuntimeException("Data not matching for given jmbg");
		}
		if (broker.getUser(jmbg) != null) {
			throw new RuntimeException("User with given jmbg already registered");
		}
		broker.insertUser(user);
	}
}