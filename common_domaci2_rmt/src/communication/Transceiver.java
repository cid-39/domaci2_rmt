package communication;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Transceiver {
	private Socket sock;

	public Transceiver(Socket sock) {
		super();
		this.sock = sock;
	}
	
	public void marco(Object data) throws Exception {
		ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
		out.writeObject(data);
		out.flush();
	}
	
	public Object polo() throws Exception {
		ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
		return in.readObject();
	}
}
