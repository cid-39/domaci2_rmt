package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import client_communication.Connection;
import model.Putovanje;
import model.User;
import model.Zemlja;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Dimension;

public class Dash extends JDialog {

	private User user;
	
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Dash dialog = new Dash(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Dash(User user) {
		this.user = user;
	
		
		setBounds(100, 100, 701, 507);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setPreferredSize(new Dimension(400, 400));
			scrollPane.setSize(new Dimension(50, 50));
			contentPanel.add(scrollPane);
			{
				table = new JTable();
				table.setEnabled(false);
				scrollPane.setViewportView(table);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		loadTable();
	}
	
	private void loadTable() {
		String[] columnNames = {"Zemlje", "Datum prijave", "Datum ulaska", "Datum izlaska", "Nacin transporta", "Placa se", "Status"};
		Object[][] data = getPutovanjeData();

		DefaultTableModel model = new DefaultTableModel(data, columnNames);
		table.setModel(model);
	}

	private Object[][] getPutovanjeData() {
		LinkedList<Putovanje> putovanja = Connection.get_putovanja(user.getId());
		Object[][] ret = new Object[putovanja.size()][7];
		int red=0;
		for (Putovanje putovanje : putovanja) {
			ret[red][0] = getZemljeString(putovanje.getZemlja());
			ret[red][1] = putovanje.getDatum_prijave();
			ret[red][2] = putovanje.getDatum_ulaska();
			ret[red][3] = putovanje.getDatum_izlaska();
			ret[red][4] = putovanje.getTransport().getTip();
			ret[red][5] = putovanje.isPlaca_se() ? "Da" : "Ne";
			ret[red][6] = getStatus(putovanje);
			red++;
		}
		return ret;
	}

	private Object getStatus(Putovanje putovanje) {
		if (putovanje.getDatum_ulaska().isBefore(LocalDate.now())) {
			return "Zavrsena";
		} else if (putovanje.getDatum_ulaska().isBefore(LocalDate.now().plusDays(2))) {
			return "Zakljucana";
		} else return "U obradi";
	}

	private Object getZemljeString(LinkedList<Zemlja> zemlja) {
		String ret = "";
		for (Zemlja zemlja2 : zemlja) {
			ret += zemlja2.getNaziv() + ", ";
		}
		return ret.substring(0, ret.length() - 2);
	}

}
