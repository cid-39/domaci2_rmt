package ui;

import model.Putovanje;
import model.Transport;
import model.Zemlja;

import javax.swing.*;

import client_communication.Connection;

import java.awt.*;
import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class PutovanjeEditDialog extends JDialog {
	private Putovanje editingPutovanje;
	
    private JTextField txtZemlje;
    private JTextField txtDatumPrijave;
    private JTextField txtDatumUlaska;
    private JTextField txtDatumIzlaska;
    private JComboBox<String> transportDropdown;
    private JCheckBox chkPlacaSe;
    private JLabel lblStatus;

    public PutovanjeEditDialog(Putovanje putovanje) {
    	editingPutovanje = putovanje;
        setTitle("Edit Putovanje");
        setModal(true);
        setSize(400, 300);
        getContentPane().setLayout(new GridLayout(8, 2, 5, 5));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        getContentPane().add(new JLabel("Zemlje:"));

        LinkedList<String> euCountries = new LinkedList<String>(Arrays.asList(
                "AUSTRIA", "BELGIUM", "BULGARIA", "CROATIA", "CYPRUS", "CZECHIA",
                "DENMARK", "ESTONIA", "FINLAND", "FRANCE", "GERMANY", "GREECE",
                "HUNGARY", "IRELAND", "ITALY", "LATVIA", "LITHUANIA", "LUXEMBOURG",
                "MALTA", "NETHERLANDS", "POLAND", "PORTUGAL", "ROMANIA", "SLOVAKIA",
                "SLOVENIA", "SPAIN", "SWEDEN"
            ));
        
        MultiSelectDropdown countryDropdown = new MultiSelectDropdown(euCountries);
        getContentPane().add(countryDropdown);
        
        LinkedList<String> selectedZemlje = new LinkedList<>();
        for (Zemlja z : putovanje.getZemlja()) {
            selectedZemlje.add(z.getNaziv().toUpperCase());
        }
        countryDropdown.setSelectedItems(selectedZemlje);


        getContentPane().add(new JLabel("Datum prijave:"));
        txtDatumPrijave = new JTextField(putovanje.getDatum_prijave().format(df));
        getContentPane().add(txtDatumPrijave);

        getContentPane().add(new JLabel("Datum ulaska:"));
        txtDatumUlaska = new JTextField(putovanje.getDatum_ulaska().format(df));
        getContentPane().add(txtDatumUlaska);

        getContentPane().add(new JLabel("Datum izlaska:"));
        txtDatumIzlaska = new JTextField(putovanje.getDatum_izlaska().format(df));
        getContentPane().add(txtDatumIzlaska);

        getContentPane().add(new JLabel("Transport:"));
        String[] transportOptions = { "CAR", "TRAIN", "MOTORCYCLE", "BUS", "PLANE" };
        transportDropdown = new JComboBox<>(transportOptions);
        add(transportDropdown);
        
        if (putovanje.getTransport() != null) {
            transportDropdown.setSelectedItem(putovanje.getTransport().getTip().toUpperCase());
        }

        getContentPane().add(new JLabel("Placa se:"));
        chkPlacaSe = new JCheckBox();
        chkPlacaSe.setEnabled(false);
        chkPlacaSe.setSelected(putovanje.isPlaca_se());
        getContentPane().add(chkPlacaSe);

        getContentPane().add(new JLabel("Status:"));
        lblStatus = new JLabel(getStatus(putovanje));
        getContentPane().add(lblStatus);

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(e -> {
        	editingPutovanje.setDatum_prijave(Date.valueOf(txtDatumPrijave.getText().strip()).toLocalDate());
        	editingPutovanje.setDatum_ulaska(Date.valueOf(txtDatumUlaska.getText().strip()).toLocalDate());
        	editingPutovanje.setDatum_izlaska(Date.valueOf(txtDatumIzlaska.getText().strip()).toLocalDate());
        	updateTransport((String) transportDropdown.getSelectedItem());
        	updateZemlje(countryDropdown.getSelectedItems());
        	
        	Connection.updatePutovanje(editingPutovanje);
            JOptionPane.showMessageDialog(this, "Changes saved.");
            dispose();
        });
        getContentPane().add(btnSave);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dispose());
        getContentPane().add(btnCancel);
    }

    private void updateZemlje(LinkedList<String> selectedItems) {
		LinkedList<Zemlja> zemlje = new LinkedList<Zemlja>();
    	for (String string : selectedItems) {
			Zemlja zemlja = Connection.get_zemlja(string);
			zemlje.add(zemlja);
		}
    	editingPutovanje.setZemlja(zemlje);
		
	}

	private void updateTransport(String selectedItem) {
		Transport transport = Connection.get_transport(selectedItem);
		editingPutovanje.setTransport(transport);
		
	}

	private String getZemljeString(LinkedList<Zemlja> zemlje) {
        StringBuilder sb = new StringBuilder();
        for (Zemlja z : zemlje) {
            sb.append(z.getNaziv()).append(", ");
        }
        return sb.length() > 0 ? sb.substring(0, sb.length() - 2) : "";
    }

    private String getStatus(Putovanje putovanje) {
        if (putovanje.getDatum_ulaska().isBefore(java.time.LocalDate.now())) {
            return "Zavrsena";
        } else if (putovanje.getDatum_ulaska().isBefore(java.time.LocalDate.now().plusDays(2))) {
            return "Zakljucana";
        } else {
            return "U obradi";
        }
    }
}