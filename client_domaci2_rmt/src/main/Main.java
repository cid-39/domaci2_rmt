package main;

import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;

import communication.Transceiver;
import model.User;
import ui.Dash;
import ui.GuestDash;
import ui.LoginForm;

public class Main {
	public static Transceiver transceiver;

	public static void main(String[] args) {
		try {
			transceiver = new Transceiver(new Socket("localhost", 9454));
		} catch (IOException e) {
			System.err.println("-- Failed to connect to the server...");
			JOptionPane.showMessageDialog(null, "Couldn't connected to server.", null, JOptionPane.ERROR_MESSAGE, null);
			System.exit(-1);
		}
		JOptionPane.showMessageDialog(null, "Connected to server.");
		
		LoginForm loginform = new LoginForm(null);
		loginform.setLocationRelativeTo(null);
		loginform.setVisible(true);
		User logedUser = loginform.getLoginUser();
		loginform.dispose();
		
		if (logedUser == null) {
			GuestDash gdash = new GuestDash();
			gdash.setLocationRelativeTo(null);
			gdash.setVisible(true);
		} else {		
			Dash dash = new Dash(logedUser);
			dash.setLocationRelativeTo(null);
			dash.setVisible(true);
		}
		System.out.println("too soon....");
//		
//		System.out.println("jebo mame ubise ga");
	}

}
