package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
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
import javax.swing.ListSelectionModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Dash extends JDialog {

	private User user;
	private LinkedList<Putovanje> putovanja;
	
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTable table;

	/**
	 * Create the dialog.
	 */
	public Dash(User user) {
		
		this.user = user;
		setModal(true);
		
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
				table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				table.setRowSelectionAllowed(true);
				table.setColumnSelectionAllowed(false);
				table.setEnabled(true);
				scrollPane.setViewportView(table);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnNewButton = new JButton("New Putovanje");
				btnNewButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						NewPutovanjeDialog dialog = new NewPutovanjeDialog(user);
					    dialog.setLocationRelativeTo(Dash.this);
					    dialog.setVisible(true);
					    loadTable(); // reload table after creation
					}
				});
				buttonPane.add(btnNewButton);
			}
			{
				JButton okButton = new JButton("Edit Putovanje");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int selectedRow = table.getSelectedRow();
				        if (selectedRow == -1) {
				            JOptionPane.showMessageDialog(Dash.this, "Please select a row first.");
				            return;
				        }
				        
				        String status = (String) table.getValueAt(selectedRow, 6);
				        if (!status.equals("U obradi")) {
				            JOptionPane.showMessageDialog(Dash.this, "Only 'U obradi' entries can be edited.");
				            return;
				        }

				        Putovanje selectedPutovanje = putovanja.get(selectedRow);
				        PutovanjeEditDialog editDialog = new PutovanjeEditDialog(selectedPutovanje);
				        editDialog.setLocationRelativeTo(Dash.this);
				        editDialog.setVisible(true);
					
				        loadTable(); // Update table
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Exit");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}

		loadTable();
	}
	
	private void loadTable() {
		String[] columnNames = {"Zemlje", "Datum prijave", "Datum ulaska", "Datum izlaska", "Nacin transporta", "Placa se", "Status"};
		Object[][] data = getPutovanjeData();

		DefaultTableModel model = new DefaultTableModel(data, columnNames) {
		    @Override
		    public boolean isCellEditable(int row, int column) {
		        return false;
		    }
		};
		table.setModel(model);
	}

	private Object[][] getPutovanjeData() {
		try {
			putovanja = Connection.getPutovanja(user);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(table, "Greska u ocitavanju putovanja: " + e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
			return new Object[0][7];
		}
		if (putovanja == null || putovanja.isEmpty()) return new Object[0][7];
		Object[][] ret = new Object[putovanja.size()][7];
		int red=0;
		for (Putovanje putovanje : putovanja) {
			try {
			ret[red][0] = getZemljeString(putovanje.getZemlja());
			ret[red][1] = putovanje.getDatum_prijave();
			ret[red][2] = putovanje.getDatum_ulaska();
			ret[red][3] = putovanje.getDatum_izlaska();
			ret[red][4] = putovanje.getTransport().getTip();
			ret[red][5] = putovanje.isPlaca_se() ? "Da" : "Ne";
			ret[red][6] = getStatus(putovanje);
			} catch (Exception e) {
				System.err.println("Error in parsing data for table");
			}
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
