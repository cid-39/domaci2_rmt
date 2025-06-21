package ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import communication.Operation;
import communication.Request;
import communication.Response;
import communication.Transceiver;
import model.User;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;

public class WelcomeFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField usernameTextField;
	private JPasswordField passwordField;

	Transceiver trans = new Transceiver(null);
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WelcomeFrame frame = new WelcomeFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public WelcomeFrame() {
		try {
			trans = new Transceiver(new Socket("localhost",9454));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		setResizable(false);
		setTitle("Putovanja");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400, 400, 304, 167);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setHorizontalAlignment(SwingConstants.RIGHT);
		lblUsername.setBounds(25, 25, 72, 17);
		contentPane.add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPassword.setBounds(25, 54, 72, 17);
		contentPane.add(lblPassword);
		
		usernameTextField = new JTextField();
		usernameTextField.setBounds(115, 23, 155, 21);
		contentPane.add(usernameTextField);
		usernameTextField.setColumns(10);
		
		JButton btnLogIn = new JButton("Login");
		btnLogIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = usernameTextField.getText();
				String password = passwordField.getText();
				User u = new User();
				u.setUsername(username);
				u.setPassword(password);
				try {
					trans.send(new Request(Operation.LOGIN, u));
					
					Response res = (Response) trans.recieve();
					if (((Boolean) res.getData()).booleanValue()) {
						JOptionPane.showMessageDialog(getParent(), "Welcome "+u.getUsername());
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		btnLogIn.setBounds(171, 85, 99, 27);
		contentPane.add(btnLogIn);
		
		JButton btnRegister = new JButton("Register");
		btnRegister.setBounds(60, 85, 99, 27);
		contentPane.add(btnRegister);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(115, 52, 155, 21);
		contentPane.add(passwordField);

	}
}
