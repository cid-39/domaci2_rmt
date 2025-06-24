package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class CountrySelectDialog extends JDialog {
    private MultiSelectDropdown countryDropdown;
    private JButton btnSave;
    private JButton btnCancel;

    public CountrySelectDialog(JFrame parent) {
        super(parent, "Select EU Countries", true);
        setSize(400, 200);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // EU countries list
        LinkedList<String> euCountries = new LinkedList<String>(Arrays.asList(
            "AUSTRIA", "BELGIUM", "BULGARIA", "CROATIA", "CYPRUS", "CZECHIA",
            "DENMARK", "ESTONIA", "FINLAND", "FRANCE", "GERMANY", "GREECE",
            "HUNGARY", "IRELAND", "ITALY", "LATVIA", "LITHUANIA", "LUXEMBOURG",
            "MALTA", "NETHERLANDS", "POLAND", "PORTUGAL", "ROMANIA", "SLOVAKIA",
            "SLOVENIA", "SPAIN", "SWEDEN"
        ));

        // Dropdown component
        countryDropdown = new MultiSelectDropdown(euCountries);
        JPanel dropdownPanel = new JPanel();
        dropdownPanel.setBorder(BorderFactory.createTitledBorder("Select Countries"));
        dropdownPanel.add(countryDropdown);
        add(dropdownPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSave = new JButton("Save");
        btnCancel = new JButton("Cancel");

        btnSave.addActionListener(e -> {
        	LinkedList<String> selected = countryDropdown.getSelectedItems();
            JOptionPane.showMessageDialog(this,
                "You selected:\n" + String.join(", ", selected));
            dispose();
        });

        btnCancel.addActionListener(e -> dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CountrySelectDialog(null).setVisible(true);
        });
    }
}
