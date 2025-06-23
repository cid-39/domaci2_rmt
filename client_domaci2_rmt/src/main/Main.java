package main;

import java.io.IOException;
import java.net.Socket;

import javax.security.auth.login.LoginContext;
import javax.swing.JFrame;

import communication.Transceiver;
import model.User;
import ui.LoginForm;
import ui.WelcomeFrame;

public class Main {
	public static Transceiver transceiver;

	public static void main(String[] args) {
		try {
			transceiver = new Transceiver(new Socket("localhost", 9454));
		} catch (IOException e) {
			System.err.println("-- Failed to connect to the server...");
			System.exit(-1);
		}
		
		LoginForm loginform = new LoginForm(null);
		loginform.setLocationRelativeTo(null);
		loginform.setVisible(true);
		User logedUser = loginform.getLoginUser();
		loginform.dispose();
		
		System.out.println("too soon....");
//		
//		System.out.println("jebo mame ubise ga");
	}

}
