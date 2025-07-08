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
                nonRegUser.setBroj_pasosa(pasosNew);
                nonRegUser.setJmbg(jmbgNew);
                nonRegUser.setIme(ime);
                nonRegUser.setPrezime(prezime); 

                nonRegUser.setDatum_rodjenja(datumRodjenjaFromJMBG(jmbgNew));
                
                Putovanje newPutovanje = new Putovanje(null, null, null, null, null, null, true);

                newPutovanje.setDatum_prijave(Date.valueOf(txtDatumPrijave.getText().strip()).toLocalDate());
                newPutovanje.setDatum_ulaska(Date.valueOf(txtDatumUlaska.getText().strip()).toLocalDate());
                newPutovanje.setDatum_izlaska(Date.valueOf(txtDatumIzlaska.getText().strip()).toLocalDate());

                updateTransport(newPutovanje, (String) transportDropdown.getSelectedItem());
                updateZemlje(newPutovanje, countryDropdown.getSelectedItems());

        		newPutovanje.setPlaca_se(checkPlacaSe(nonRegUser.getDatum_rodjenja()));
        		newPutovanje.setPutnik(nonRegUser);

                Connection.insertGuestPutovanje(newPutovanje);
                JOptionPane.showMessageDialog(this, "Putovanje created.");
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
        return !datum_rodjenja.isAfter(LocalDate.now().minusYears(18));
	}

	private LocalDate datumRodjenjaFromJMBG(String jmbg) {
		String date = jmbg.substring(4,7) + "-" + jmbg.substring(2,4) + "-" + jmbg.substring(0, 2);
		if (date.charAt(0) == '0') {
			date = "2" + date;
		} else date = "1" + date;
		return Date.valueOf(date).toLocalDate();
	}

	private void updateZemlje(Putovanje p, LinkedList<String> selectedItems) {
        LinkedList<Zemlja> zemlje = new LinkedList<>();
        for (String naziv : selectedItems) {
            Zemlja zemlja = Connection.get_zemlja(naziv);
            zemlje.add(zemlja);
        }
        p.setZemlja(zemlje);
    }

    private void updateTransport(Putovanje p, String selectedItem) {
        Transport transport = Connection.get_transport(selectedItem);
        p.setTransport(transport);
    }
}
