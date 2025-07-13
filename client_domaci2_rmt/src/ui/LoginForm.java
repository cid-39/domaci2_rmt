package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import client_communication.Connection;
import model.User;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

public class LoginForm extends JDialog {
	private User login_user;
	
	public User getLoginUser() {
		return login_user;
	}
	
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JPasswordField passwordField;

	/**
	 * Create the dialog.
	 */
	public LoginForm(JFrame parent) {
		super(parent, "Login", true);
//		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		setBounds(100, 100, 372, 163);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblUsername = new JLabel("Username:");
			lblUsername.setBounds(12, 12, 64, 17);
			lblUsername.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblUsername);
		}
		{
			textField = new JTextField();
			textField.setBounds(87, 10, 191, 21);
			textField.setColumns(10);
			contentPanel.add(textField);
		}
		{
			JLabel lblPassword = new JLabel("Password:");
			lblPassword.setBounds(12, 45, 60, 17);
			lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblPassword);
		}
		{
			passwordField = new JPasswordField();
			passwordField.setBounds(87, 43, 191, 21);
			contentPanel.add(passwordField);
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton registerButton = new JButton("Register");
				registerButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						RegisterForm regform = new RegisterForm();
						regform.setLocationRelativeTo(null);
						regform.setVisible(true);
						login_user = regform.getRegistered();
						regform.dispose();
						if (login_user != null)	setVisible(false);
					}
				});
				buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
				registerButton.setActionCommand("OK");
				buttonPane.add(registerButton);
				getRootPane().setDefaultButton(registerButton);
			}
			{
				JButton loginButton = new JButton("Login");
				loginButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String username = textField.getText();
						String password = passwordField.getText();
						try {
							login_user = Connection.login(username, password);
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(loginButton, e1.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
						}
						if (login_user!=null) {
							JOptionPane.showMessageDialog(loginButton, "Welcome", "Successful login!", JOptionPane.INFORMATION_MESSAGE);
							setVisible(false);
						}
					}
				});
				loginButton.setActionCommand("Cancel");
				buttonPane.add(loginButton);
			}
			{
				JButton btnContinueAsGuest = new JButton("Continue as guest");
				btnContinueAsGuest.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						login_user = new User();
						login_user.setId(Integer.MAX_VALUE);
						setVisible(false);
					}
				});
				buttonPane.add(btnContinueAsGuest);
			}
		}
	}

}
