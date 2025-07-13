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

public class NewPutovanjeDialog extends JDialog {
    private JTextField txtDatumPrijave;
    private JTextField txtDatumUlaska;
    private JTextField txtDatumIzlaska;
    private JComboBox<String> transportDropdown;
    private JCheckBox chkPlacaSe;
    private MultiSelectDropdown countryDropdown;

    public NewPutovanjeDialog(User user) {
        setTitle("New Putovanje");
        setModal(true);
        setSize(400, 300);
        getContentPane().setLayout(new GridLayout(7, 2, 5, 5));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

        getContentPane().add(new JLabel("Placa se:"));
        chkPlacaSe = new JCheckBox();
        chkPlacaSe.setEnabled(false);
        if (user.getDatum_rodjenja().isAfter(LocalDate.now().minusYears(18)) || user.getDatum_rodjenja().isBefore(LocalDate.now().minusYears(70)))
        	chkPlacaSe.setSelected(false);
        else chkPlacaSe.setSelected(true);
        
        getContentPane().add(chkPlacaSe);

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(e -> {
            try {
            	LinkedList<Zemlja> zemlje = new LinkedList<>();
                for (String naziv : countryDropdown.getSelectedItems()) {
                    Zemlja zemlja = Connection.get_zemlja(naziv);
                    zemlje.add(zemlja);
                }
                Transport transport = Connection.get_transport((String) transportDropdown.getSelectedItem());
                LocalDate dat_prijave=null;
                LocalDate dat_ulaska=null;
                LocalDate dat_izlaska=null;
                try {
                dat_prijave = Date.valueOf(txtDatumPrijave.getText().strip()).toLocalDate();
                dat_ulaska = Date.valueOf(txtDatumUlaska.getText().strip()).toLocalDate();
        		dat_izlaska =  Date.valueOf(txtDatumIzlaska.getText().strip()).toLocalDate();
                } catch (Exception e1) {
                	JOptionPane.showMessageDialog(btnSave, "Dates are not valid", "Error", JOptionPane.WARNING_MESSAGE);
                	return;
                }
                Putovanje newPutovanje = new Putovanje(user, zemlje, dat_prijave, dat_ulaska, dat_izlaska, transport, chkPlacaSe.isSelected());

                Connection.insertPutovanje(newPutovanje);
                JOptionPane.showMessageDialog(this, "Putovanje created.");
                writePutovanjeToFile(newPutovanje);
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        getContentPane().add(btnSave);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dispose());
        getContentPane().add(btnCancel);
    }
    
    private void writePutovanjeToFile(Putovanje putovanje) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("putovanje_"+putovanje.getDatum_prijave().toString()+".txt", true))) {
        	 writer.write("===== PUTOVANJE =====");
             writer.newLine();

             writer.write("Ime: " + putovanje.getPutnik().getIme());
             writer.newLine();
             writer.write("Prezime: " + putovanje.getPutnik().getPrezime());
             writer.newLine();
             writer.write("Email: " + putovanje.getPutnik().getEmail());
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
