package server;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;

import javax.management.RuntimeErrorException;

import broker.DBBroker;
import communication.Operation;
import communication.Request;
import communication.Response;
import communication.Transceiver;
import model.Putovanje;
import model.User;
import model.Zemlja;


public class ServerHandler extends Thread {
	private final int port = 9454;
	private Socket socket;
	private Transceiver transceiver;
	private static DBBroker broker;
	
	public static void main(String[] args) {
		System.out.println("Server starting...");
		try {
			DBBroker.get_instance().connect();
			ServerSocket serSock = new ServerSocket(9454);
			System.out.println("Server started on port: " + serSock.getLocalPort());
			
			while (true) {
				Socket socket = serSock.accept();
				
				System.out.println("++	New connection accepted");
				
				new ServerHandler(socket).start();
				
				// fake break
				if (socket.getPort() == new Integer(3)) {
					break;
				}
			}
			
			DBBroker.get_instance().disconnect();
		}
		catch (BindException e) {
			System.err.println("-- Port already in use - stop all server instances.");
		} 
		catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			System.err.printf("Error in communication with database\nShutting down...\n");
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public 	ServerHandler(Socket socket) {
		this.socket = socket;	
		
		System.out.println("	ServerHandler thread started for client: " + socket.getInetAddress());
		System.out.println();
	}

	@Override
	public void run() {
		transceiver = new Transceiver(socket);
		broker = DBBroker.get_instance();
		
		Response response = null;
		Exception exception = null;
		Object result = null;
		
		try {
			while (true) {
				Request request = (Request) transceiver.recieve();
				
				// fake break
				if (request.getArg() == new Integer(3)) {
					break;
				}
				
				
				try {
					result = handle(request);
				} catch (Exception e) {
					exception = e;
				}
				
				response = new Response(result, exception);
				
				try {
					transceiver.send(response);
				} catch (Exception e) {
					System.err.println("-- Could not send response");
					return;
				} 
			}
		} catch (ClassCastException e) {
			System.err.println("Failed to cast data to Request, closing connection to client " + socket.getInetAddress());
			return;
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	private Object handle(Request request) {
		Object result = null;
		
		switch (request.getOp()){
		
			case Operation.LOGIN: {
				User user = (User) request.getArg();
				result = broker.loginUser(user.getUsername(), user.getPassword());
				break;
			}
		
			case Operation.REGISTER: {
				User user = (User) request.getArg();
				result = registerUser(user.getUsername(), user.getPassword(), user.getIme(), user.getPrezime(), user.getEmail(), user.getJmbg(), user.getBroj_pasosa(), user.getDatum_rodjenja());    
				break;
			}
			
			case Operation.GET_PUT: {
				User user = (User) request.getArg();
				if (user.getId() == Integer.MIN_VALUE) {
					result = broker.getPutovanja(user.getJmbg(), user.getBroj_pasosa());
				} else result = broker.getPutovanja(user.getId());
				break;
			}
			
			case Operation.UPDATE_PUT: {
				Putovanje putovanje = (Putovanje) request.getArg();
				broker.updatePutovanje(putovanje);
				result = 1;	
				break;
			}
			
			case Operation.GET_TRANS: {
				String naziv = (String) request.getArg();
				result = broker.getTransport(naziv);
				break;
			}
			
			case Operation.GET_ZEMLJA: {
				String naziv = (String) request.getArg();
				result = broker.getZemlja(naziv);
				break;
			}
			
			case Operation.UPDATE_PUTOVANJE: {
				Putovanje put = (Putovanje) request.getArg();
				if (validateNoOverlap(put)) throw new RuntimeException("Another putovanje already exists in the same counry and period");
				broker.updatePutovanje(put);
				break;
			}
			
			case Operation.INSERT_PUTOVANJE: {
				Putovanje put = (Putovanje) request.getArg();
				if (validateNoOverlap(put)) throw new RuntimeException("Another putovanje already exists in the same counry and period");
				broker.insertPutovanje(put);
				break;
			}
			
			case Operation.INSERT_GUEST_PUTOVANJE: {
				Putovanje put = (Putovanje) request.getArg();

				User checkUser = broker.getPerson(put.getPutnik().getJmbg());
				if (!put.getPutnik().equals(checkUser)) {
					throw new RuntimeException("Data not matching for given jmbg");
				}
				User existingUser = broker.getUser(put.getPutnik().getJmbg());
				if (existingUser != null) {
					put.setPutnik(existingUser);
					broker.insertPutovanje(put);
					break;
				} 
				
				User newUser = addUserNoRegister(put.getPutnik().getIme(), put.getPutnik().getPrezime(), put.getPutnik().getJmbg(), put.getPutnik().getBroj_pasosa(), put.getPutnik().getDatum_rodjenja());        
				put.setPutnik(newUser);
				if (validateNoOverlap(put)) throw new RuntimeException("Another putovanje already exists in the same counry and period");
				broker.insertPutovanje(put);
				break;
			}
		}
		return result;
	}
	
	private static User registerUser(String username, String password, String ime, String prezime, String email, String jmbg,
			String broj_pasosa, LocalDate datum_rodjenja) {
		User user = new User();
		user.setUsername(username);
		// username check
		User usernameCheckUser = broker.getUserByUsername(username);
		if ( usernameCheckUser != null && usernameCheckUser.getUsername().equals(username)) {
			throw new RuntimeException("Username already in use");
		}
		//
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
		
		checkUser = null;
		checkUser = broker.getUser(jmbg);
		
		if (checkUser != null) {
			if (checkUser.getUsername().equals("h") || checkUser.getPassword().equals("hold") || checkUser.getEmail().equals("hold")) {
				broker.updateUserCreds(user);
				user.setId(checkUser.getId());
			} else {
				throw new RuntimeException("User with given jmbg already registered");
			}
		} else {
			broker.insertUser(user);			
		}
		return user;
	}
	
	private static User addUserNoRegister(String ime, String prezime, String jmbg, String broj_pasosa, LocalDate datum_rodjenja) {
		User user = new User(Integer.MIN_VALUE, "h", "hold", ime, prezime, "hold", jmbg, broj_pasosa, datum_rodjenja);
		broker.insertUser(user);
		return user;
	}
	
	private boolean validateNoOverlap(Putovanje newPutovanje) {
	    boolean ret = false;
		User user = newPutovanje.getPutnik();
	    LinkedList<Putovanje> existingPutovanja;

	    if (user.getId() == Integer.MIN_VALUE) {
	        existingPutovanja = broker.getPutovanja(user.getJmbg(), user.getBroj_pasosa());
	    } else {
	        existingPutovanja = broker.getPutovanja(user.getId());
	    }
	    LocalDate newStart = newPutovanje.getDatum_ulaska();
	    LocalDate newEnd = newPutovanje.getDatum_izlaska();

	    for (Putovanje existing : existingPutovanja) {
	        // preskakanje istog putovanja ako je UPDATE_PUTOVANJE
	        if (existing.getId() == newPutovanje.getId()) continue;

	        boolean sharesCountry = false;

	        for (Zemlja z1 : newPutovanje.getZemlja()) {
	            for (Zemlja z2 : existing.getZemlja()) {
	                if (z1.getId() == z2.getId()) {
	                    sharesCountry = true;
	                    break;
	                }
	            }
	            if (sharesCountry) break;
	        }

	        // Only check dates if they share at least one country
	        if (sharesCountry) {
	            LocalDate existingStart = existing.getDatum_ulaska();
	            LocalDate existingEnd = existing.getDatum_izlaska();
	            ret = newEnd.isAfter(existingStart) || newStart.isBefore(existingEnd);
	        }
	    }
	    return ret;
	}

	
}