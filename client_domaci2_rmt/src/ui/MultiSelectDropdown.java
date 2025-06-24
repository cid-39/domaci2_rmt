package ui;

import javax.swing.*;

import java.awt.*;
import java.util.*;

public class MultiSelectDropdown extends JPanel {
    private final JButton button = new JButton("Select Zemlja");
    private final JPopupMenu menu = new JPopupMenu();
    private final Map<String, JCheckBox> items = new LinkedHashMap<>();

    public MultiSelectDropdown(LinkedList<String> options) {
        setLayout(new BorderLayout());
        add(button, BorderLayout.CENTER);

        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(checkboxPanel);
        scrollPane.setPreferredSize(new Dimension(200, 150));
        menu.add(scrollPane);

        for (String option : options) {
            JCheckBox checkBox = new JCheckBox(option);
            checkboxPanel.add(checkBox);
            items.put(option, checkBox);
        }

        button.addActionListener(e -> {
            menu.show(button, 0, button.getHeight());
        });
    }

    public LinkedList<String> getSelectedItems() {
    	LinkedList<String> selected = new LinkedList<>();
        for (Map.Entry<String, JCheckBox> entry : items.entrySet()) {
            if (entry.getValue().isSelected()) {
                selected.add(entry.getKey());
            }
        }
        return selected;
    }

    public void setSelectedItems(LinkedList<String> toSelect) {
        for (String key : items.keySet()) {
            items.get(key).setSelected(toSelect.contains(key));
        }
    }
}
