package handler;

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
	public static void main(String[] args) {
		DBBroker broker = DBBroker.get_instance();
		try {
			broker.connect();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Povezivanje pokusano");
		User u = broker.getUser(1);
		Zemlja F = new Zemlja(1, "FRANCE");
		Zemlja G = new Zemlja(2, "GERMANY");
		LinkedList<Zemlja> Z = new LinkedList<Zemlja>();
		Z.add(G); Z.add(F);
		Putovanje p = new Putovanje(u, Z, LocalDate.now(), LocalDate.now().plusDays(10), LocalDate.now().plusDays(20), new Transport(1, "CAR"), true);
		
		broker.insertPutovanje(p);
		
		try {
			broker.disconnect();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}