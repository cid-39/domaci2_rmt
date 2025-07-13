package ui;

import model.Putovanje;
import model.Transport;
import model.User;
import model.Zemlja;

import javax.swing.*;

import client_communication.Connection;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedList;

public class NewPutovanjeGuestDialog extends JDialog {
    private static final long serialVersionUID = 1L;
	private JTextField txtIme;
    private JTextField txtPrezime;
    private JTextField txtBrojPasosa;
    private JTextField txtJmbg;
    private JTextField txtDatumPrijave;
    private JTextField txtDatumUlaska;
    private JTextField txtDatumIzlaska;
    private JComboBox<String> transportDropdown;
    private MultiSelectDropdown countryDropdown;

    public NewPutovanjeGuestDialog(String jmbg, String pasos) {

        setTitle("New Putovanje (Guest)");
        setModal(true);
        setSize(400, 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(10, 2, 5, 5));

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        getContentPane().add(new JLabel("Ime:"));
        txtIme = new JTextField();
        getContentPane().add(txtIme);

        getContentPane().add(new JLabel("Prezime:"));
        txtPrezime = new JTextField();
        getContentPane().add(txtPrezime);

        getContentPane().add(new JLabel("Broj Pasosa:"));
        txtBrojPasosa = new JTextField(pasos != null ? pasos : "");
        getContentPane().add(txtBrojPasosa);

        getContentPane().add(new JLabel("JMBG:"));
        txtJmbg = new JTextField(jmbg != null ? jmbg : "");
        getContentPane().add(txtJmbg);

        getContentPane().add(new JLabel("Zemlje:"));
        LinkedList<String> euCountries = new LinkedList<>(Arrays.asList(
            "AUSTRIA", "BELGIUM", "BULGARIA", "CROATIA", "CYPRUS", "CZECHIA",
            "DENMARK", "ESTONIA", "FINLAND", "FRANCE", "GERMANY", "GREECE",
            "HUNGARY", "IRELAND", "ITALY", "LATVIA", "LITHUANIA", "LUXEMBOURG",
            "MALTA", "NETHERLANDS", "POLAND", "PORTUGAL", "ROMANIA", "SLOVAKIA",
            "SLOVENIA", "SPAIN", "SWEDEN"
        ));
        countryDropdown = new MultiSelectDropdown(euCountries);
        getContentPane().add(countryDropdown);

        getContentPane().add(new JLabel("Datum prijave:"));
        txtDatumPrijave = new JTextField(df.format(LocalDate.now()));
        getContentPane().add(txtDatumPrijave);

        getContentPane().add(new JLabel("Datum ulaska:"));
        txtDatumUlaska = new JTextField();
        getContentPane().add(txtDatumUlaska);

        getContentPane().add(new JLabel("Datum izlaska:"));
        txtDatumIzlaska = new JTextField();
        getContentPane().add(txtDatumIzlaska);

        getContentPane().add(new JLabel("Transport:"));
        String[] transportOptions = { "", "CAR", "TRAIN", "MOTORCYCLE", "BUS", "PLANE" };
        transportDropdown = new JComboBox<>(transportOptions);
        getContentPane().add(transportDropdown);

        txtIme.setText("Uroš");
        txtPrezime.setText("Živković");
        txtBrojPasosa.setText("891234567");
        txtJmbg.setText("0306981223334");
        txtDatumUlaska.setText("2025-09-11");
        txtDatumIzlaska.setText("2025-09-20");
        transportDropdown.setSelectedItem("CAR");
        countryDropdown.setSelectedItems(new LinkedList<>(Arrays.asList("AUSTRIA", "GERMANY", "FRANCE")));

        
        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(e -> {
            try {
                String ime = txtIme.getText().strip();
                String prezime = txtPrezime.getText().strip();
                String pasosNew = txtBrojPasosa.getText().strip();
                String jmbgNew = txtJmbg.getText().strip();

                if (ime.isEmpty() || prezime.isEmpty() || pasosNew.isEmpty() || jmbgNew.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Sva polja moraju biti popunjena.");
                    return;
                }

                User nonRegUser = new User();
                Transport transport = new Transport(0, "");
                Putovanje newPutovanje;
                try {
                nonRegUser.setBroj_pasosa(pasosNew);
                nonRegUser.setJmbg(jmbgNew);
                nonRegUser.setIme(ime);
                nonRegUser.setPrezime(prezime); 

                nonRegUser.setDatum_rodjenja(datumRodjenjaFromJMBG(jmbgNew));
                
                transport = Connection.get_transport((String) transportDropdown.getSelectedItem());
                
                LinkedList<Zemlja> zemlje = new LinkedList<>();
                for (String naziv : countryDropdown.getSelectedItems()) {
                    Zemlja zemlja = Connection.get_zemlja(naziv);
                    zemlje.add(zemlja);
                }
                LocalDate datum_prijave = null;
                LocalDate datum_ulaska= null;
                LocalDate datum_izlaska=null;
                try {
                datum_prijave = Date.valueOf(txtDatumPrijave.getText().strip()).toLocalDate();
                datum_ulaska = Date.valueOf(txtDatumUlaska.getText().strip()).toLocalDate();
                datum_izlaska = Date.valueOf(txtDatumIzlaska.getText().strip()).toLocalDate();
                } catch (Exception ee) {
                	JOptionPane.showMessageDialog(btnSave, "Dates are not valid", "Error", JOptionPane.WARNING_MESSAGE);
                	return;
				}
                
                newPutovanje = new Putovanje(nonRegUser, zemlje, datum_prijave, 
                		datum_ulaska, datum_izlaska, 
                		transport, checkPlacaSe(nonRegUser.getDatum_rodjenja()));

                } catch (Exception e1) {
                	JOptionPane.showMessageDialog(btnSave, e1.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
                	return;
                }
                
                try { 
                	Connection.insertGuestPutovanje(newPutovanje);
                } catch (Exception e2) {
                	JOptionPane.showMessageDialog(btnSave, e2.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
                	return;
				}
                
                JOptionPane.showMessageDialog(this, "Putovanje created.");
                writePutovanjeToFile(newPutovanje);
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Greška: " + ex.getMessage());
            }
        });
        getContentPane().add(btnSave);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dispose());
        getContentPane().add(btnCancel);
    }

    private boolean checkPlacaSe(LocalDate datum_rodjenja) {
        return !(datum_rodjenja.isAfter(LocalDate.now().minusYears(18)) || datum_rodjenja.isBefore(LocalDate.now().minusYears(70)));
	}

	private LocalDate datumRodjenjaFromJMBG(String jmbg) {
		String date = jmbg.substring(4,7) + "-" + jmbg.substring(2,4) + "-" + jmbg.substring(0, 2);
		if (date.charAt(0) == '0') {
			date = "2" + date;
		} else date = "1" + date;
		return Date.valueOf(date).toLocalDate();
	}
	
	private void writePutovanjeToFile(Putovanje putovanje) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("putovanje_"+putovanje.getDatum_prijave().toString()+".txt", true))) {
        	 writer.write("===== PUTOVANJE =====");
             writer.newLine();

             writer.write("Ime: " + putovanje.getPutnik().getIme());
             writer.newLine();
             writer.write("Prezime: " + putovanje.getPutnik().getPrezime());
             writer.newLine();
             writer.write("JMBG: " + putovanje.getPutnik().getJmbg());
             writer.newLine();
             writer.write("Broj pasosa: " + putovanje.getPutnik().getBroj_pasosa());
             writer.newLine();
             writer.write("Datum rodjenja: " + putovanje.getPutnik().getDatum_rodjenja());
             writer.newLine();

             writer.write("----------------------");
             writer.newLine();

             writer.write("Datum prijave: " + putovanje.getDatum_prijave());
             writer.newLine();
             writer.write("Datum ulaska: " + putovanje.getDatum_ulaska());
             writer.newLine();
             writer.write("Broj dana boravka: " + (putovanje.getDatum_izlaska().toEpochDay() - putovanje.getDatum_ulaska().toEpochDay()));
             writer.newLine();

             writer.write("Zemlje: ");
             for (int i = 0; i < putovanje.getZemlja().size(); i++) {
                 writer.write(putovanje.getZemlja().get(i).getNaziv());
                 if (i < putovanje.getZemlja().size() - 1) {
                     writer.write(", ");
                 }
             }
             writer.newLine();
             writer.write("Transport: " + putovanje.getTransport().getTip());
             writer.newLine();
             writer.write("Placa se: " + (putovanje.isPlaca_se() ? "DA" : "NE"));
             writer.newLine();
             writer.write("=======================");
             writer.newLine();
             
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to write to file: " + e.getMessage());
        }
    }
}
