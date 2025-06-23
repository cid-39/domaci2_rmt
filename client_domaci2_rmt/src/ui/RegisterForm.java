package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import client_communication.Connection;
import model.User;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.awt.event.ActionEvent;

public class RegisterForm extends JDialog {
	private User registered;
	
	public User getRegistered() {
		return registered;
	}
	
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField usernameTextField;
	private JTextField fNameTextField_1;
	private JTextField lNameTextField_2;
	private JTextField emailTextField_3;
	private JTextField jmbgTextField_4;
	private JTextField passporNumberTextField_5;
	private JTextField bdayTextField_6;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			RegisterForm dialog = new RegisterForm();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public RegisterForm() {
		setModal(true);
		
		setBounds(100, 100, 344, 319);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setHorizontalAlignment(SwingConstants.TRAILING);
		lblUsername.setBounds(12, 12, 89, 17);
		contentPanel.add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setHorizontalAlignment(SwingConstants.TRAILING);
		lblPassword.setBounds(12, 41, 89, 17);
		contentPanel.add(lblPassword);
		
		JLabel lblName = new JLabel("First name:");
		lblName.setHorizontalAlignment(SwingConstants.TRAILING);
		lblName.setBounds(12, 70, 89, 17);
		contentPanel.add(lblName);
		
		JLabel lblLastName = new JLabel("Last name:");
		lblLastName.setHorizontalAlignment(SwingConstants.TRAILING);
		lblLastName.setBounds(12, 99, 89, 17);
		contentPanel.add(lblLastName);
		
		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setHorizontalAlignment(SwingConstants.TRAILING);
		lblEmail.setBounds(12, 128, 89, 17);
		contentPanel.add(lblEmail);
		
		JLabel lblJmbg = new JLabel("JMBG:");
		lblJmbg.setHorizontalAlignment(SwingConstants.TRAILING);
		lblJmbg.setBounds(12, 158, 89, 17);
		contentPanel.add(lblJmbg);
		
		JLabel lblPasportNumber = new JLabel("No. passport:");
		lblPasportNumber.setHorizontalAlignment(SwingConstants.TRAILING);
		lblPasportNumber.setBounds(12, 187, 89, 17);
		contentPanel.add(lblPasportNumber);
		
		JLabel lblBirthDate = new JLabel("Date of birth:");
		lblBirthDate.setHorizontalAlignment(SwingConstants.TRAILING);
		lblBirthDate.setBounds(12, 216, 89, 17);
		contentPanel.add(lblBirthDate);
		
		usernameTextField = new JTextField();
		usernameTextField.setBounds(119, 10, 197, 21);
		contentPanel.add(usernameTextField);
		usernameTextField.setColumns(10);
		
		fNameTextField_1 = new JTextField();
		fNameTextField_1.setColumns(10);
		fNameTextField_1.setBounds(119, 68, 197, 21);
		contentPanel.add(fNameTextField_1);
		
		lNameTextField_2 = new JTextField();
		lNameTextField_2.setColumns(10);
		lNameTextField_2.setBounds(119, 97, 197, 21);
		contentPanel.add(lNameTextField_2);
		
		emailTextField_3 = new JTextField();
		emailTextField_3.setColumns(10);
		emailTextField_3.setBounds(119, 126, 197, 21);
		contentPanel.add(emailTextField_3);
		
		jmbgTextField_4 = new JTextField();
		jmbgTextField_4.setColumns(10);
		jmbgTextField_4.setBounds(119, 156, 197, 21);
		contentPanel.add(jmbgTextField_4);
		
		passporNumberTextField_5 = new JTextField();
		passporNumberTextField_5.setColumns(10);
		passporNumberTextField_5.setBounds(119, 185, 197, 21);
		contentPanel.add(passporNumberTextField_5);
		
		bdayTextField_6 = new JTextField();
		bdayTextField_6.setColumns(10);
		bdayTextField_6.setBounds(119, 214, 197, 21);
		contentPanel.add(bdayTextField_6);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(119, 39, 197, 21);
		contentPanel.add(passwordField);
		
		JLabel lblYyyy = new JLabel("YYYY-MM-DD");
		lblYyyy.setForeground(new Color(165, 29, 45));
		lblYyyy.setHorizontalAlignment(SwingConstants.TRAILING);
		lblYyyy.setBounds(207, 235, 109, 17);
		contentPanel.add(lblYyyy);
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			{
				JButton okButton = new JButton("Register");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						User new_user = new User();
						new_user.setUsername(usernameTextField.getText());
						new_user.setPassword(passwordField.getText());
						new_user.setIme(fNameTextField_1.getText());
						new_user.setPrezime(lNameTextField_2.getText());
						new_user.setEmail(emailTextField_3.getText());
						new_user.setJmbg(jmbgTextField_4.getText());
						new_user.setBroj_pasosa(passporNumberTextField_5.getText());
						new_user.setDatum_rodjenja(Date.valueOf(bdayTextField_6.getText().strip()).toLocalDate());
						
						new_user = Connection.register(new_user);
						if(new_user!=null) {
							JOptionPane.showMessageDialog(null, "Registered!", "Successful register!", JOptionPane.INFORMATION_MESSAGE);
							registered = new_user;
							setVisible(false);
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
