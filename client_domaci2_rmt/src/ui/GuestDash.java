package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import client_communication.Connection;
import model.Putovanje;
import model.User;
import model.Zemlja;

public class GuestDash extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTable table;

    private JTextField txtJmbg;
    private JTextField txtPasos;
    private String currentJmbg;
    private String currentPasos;

    public GuestDash() {
        setTitle("Guest Dashboard");
        setBounds(100, 100, 701, 550);
        setModal(true);
        getContentPane().setLayout(new BorderLayout());

        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.add(new JLabel("JMBG:"));
        txtJmbg = new JTextField(10);
        searchPanel.add(txtJmbg);

        searchPanel.add(new JLabel("Broj Pasosa:"));
        txtPasos = new JTextField(10);
        searchPanel.add(txtPasos);

        JButton btnSearch = new JButton("Search");
        searchPanel.add(btnSearch);
        getContentPane().add(searchPanel, BorderLayout.NORTH);

        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPanel.setLayout(new BorderLayout(0, 0));
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(400, 400));
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        table = new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        table.setEnabled(true);
        scrollPane.setViewportView(table);

        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton btnNew = new JButton("New Putovanje");
        btnNew.addActionListener(e -> {
            NewPutovanjeGuestDialog dialog = new NewPutovanjeGuestDialog(txtJmbg.getText().strip(), txtPasos.getText().strip());
            dialog.setLocationRelativeTo(GuestDash.this);
            dialog.setVisible(true);
            });
        buttonPane.add(btnNew);

        JButton btnExit = new JButton("Exit");
        btnExit.addActionListener(e -> dispose());
        buttonPane.add(btnExit);

        setEmptyTable();

        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentJmbg = txtJmbg.getText().strip();
                currentPasos = txtPasos.getText().strip();

                if (currentJmbg.isEmpty() || currentPasos.isEmpty()) {
                    JOptionPane.showMessageDialog(GuestDash.this, "Both JMBG and Broj Pasosa must be filled.");
                    return;
                }
                try {
                	loadTable();
                } catch (Exception e1) {
                	currentJmbg="";
                	currentPasos="";
                	JOptionPane.showMessageDialog(btnSearch, e1.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
				}
            }
        });
    }

    private void setEmptyTable() {
        String[] columnNames = {"Zemlje", "Datum prijave", "Datum ulaska", "Datum izlaska", "Nacin transporta", "Placa se", "Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(model);
    }

    private void loadTable() {
    	User user = new User();
    	user.setBroj_pasosa(currentPasos);
    	user.setJmbg(currentJmbg);
    	user.setId(Integer.MIN_VALUE); // for backend to differentiate 
        LinkedList<Putovanje> putovanja = null;
		try {
			putovanja = Connection.getPutovanja(user);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(table, "Greska u ocitavanju putovanja: " + e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
		}
        String[] columnNames = {"Zemlje", "Datum prijave", "Datum ulaska", "Datum izlaska", "Nacin transporta", "Placa se", "Status"};
        Object[][] data;
        if (putovanja == null || putovanja.isEmpty()) {
        	data = new Object[0][7];
        	DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            table.setModel(model);
            return;
        } else data = new Object[putovanja.size()][7];
        
        int red = 0;
        for (Putovanje putovanje : putovanja) {
            data[red][0] = getZemljeString(putovanje.getZemlja());
            data[red][1] = putovanje.getDatum_prijave();
            data[red][2] = putovanje.getDatum_ulaska();
            data[red][3] = putovanje.getDatum_izlaska();
            data[red][4] = putovanje.getTransport().getTip();
            data[red][5] = putovanje.isPlaca_se() ? "Da" : "Ne";
            data[red][6] = getStatus(putovanje);
            red++;
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setModel(model);
    }

    private Object getStatus(Putovanje putovanje) {
        if (putovanje.getDatum_ulaska().isBefore(LocalDate.now())) {
            return "Zavrsena";
        } else if (putovanje.getDatum_ulaska().isBefore(LocalDate.now().plusDays(2))) {
            return "Zakljucana";
        } else return "U obradi";
    }

    private Object getZemljeString(LinkedList<Zemlja> zemlja) {
        StringBuilder ret = new StringBuilder();
        for (Zemlja zemlja2 : zemlja) {
            ret.append(zemlja2.getNaziv()).append(", ");
        }
        return ret.length() > 0 ? ret.substring(0, ret.length() - 2) : "";
    }
}
