package ui;

import model.Putovanje;
import model.Transport;
import model.User;
import model.Zemlja;

import javax.swing.*;

import client_communication.Connection;

import java.awt.*;
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
        if (user.getDatum_rodjenja().plusYears(18).isAfter(LocalDate.now()))
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
            	
                Putovanje newPutovanje = new Putovanje(user, zemlje, Date.valueOf(txtDatumPrijave.getText().strip()).toLocalDate(),
                		Date.valueOf(txtDatumUlaska.getText().strip()).toLocalDate(), 
                		Date.valueOf(txtDatumIzlaska.getText().strip()).toLocalDate(), transport, chkPlacaSe.isSelected());

                Connection.insertPutovanje(newPutovanje);
                JOptionPane.showMessageDialog(this, "Putovanje created.");
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
}
