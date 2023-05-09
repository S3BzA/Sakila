package sakila.view;

import sakila.components.RadioSelect;
import sakila.model.ResultSetTableModel;
import sakila.model.StaffModel;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StaffTab extends JPanel {
    StaffModel model;
    ResultSetTableModel tableModel;

    public StaffTab(StaffModel model) {
        super();
        this.model = model;
        this.tableModel = new ResultSetTableModel();

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout(10, 10));

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        RadioSelect select = new RadioSelect(new String[][]{
                {"any", "Any"} ,
                {"active", "Active"} ,
                {"inactive", "Inactive"}
        });
        select.addActionListener(e -> {
            String action = e.getActionCommand();
            switch (action) {
                case "any" -> model.setSearchActive(null);
                case "active" -> model.setSearchActive(true);
                case "inactive" -> model.setSearchActive(false);
            }
            updateResults();
        });

        JTextField textField = new JTextField();
        textField.addActionListener(e -> {
            model.setSearchName(e.getActionCommand());
            updateResults();
        });
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        panel.add(new JLabel("Activity"));
        panel.add(select);

        panel.add(new JLabel("Name"));
        panel.add(textField);

        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.LINE_END);
        updateResults();
    }
    private void updateResults() {
        try {
            ResultSet set = model.getAll();
            tableModel.setResultSet(set);
        }
        catch(SQLException ignored) {
        }
    }

}
