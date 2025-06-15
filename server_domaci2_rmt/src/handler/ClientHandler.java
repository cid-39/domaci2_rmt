package handler;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;

import broker.DBBroker;

import common_domaci2_rmt.Putovanje;
import common_domaci2_rmt.Transport;
import common_domaci2_rmt.User;
import common_domaci2_rmt.Zemlja;
import common_domaci2_rmt.Putovanje;

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

//		User u = broker.getUser(5);
//		Zemlja F = new Zemlja(1, "FRANCE");
//		Zemlja G = new Zemlja(2, "GERMANY");
//		LinkedList<Zemlja> Z = new LinkedList<Zemlja>();
//		Z.add(G); Z.add(F);
//		Putovanje p = new Putovanje(u, Z, LocalDate.now(), LocalDate.now().plusDays(10), LocalDate.now().plusDays(20), new Transport(1, "CAR"), true);
//		
//		broker.insertPutovanje(p);

//		registerUser("asd", "asdasdasd", "test", "testerovic", "kitan@gmail.com", "1111111111111", "111111111", Date.valueOf("1900-01-01").toLocalDate());  
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