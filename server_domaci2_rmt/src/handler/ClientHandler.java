package handler;

import java.sql.SQLException;

import broker.DBBroker;

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
		try {
			broker.disconnect();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}