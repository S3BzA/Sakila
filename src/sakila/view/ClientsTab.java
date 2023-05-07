package sakila.view;

import sakila.model.ClientModel;
import sakila.model.ResultSetTableModel;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientsTab extends JPanel {
    ClientModel model;
    ResultSetTableModel tableModel;

    public ClientsTab(ClientModel model) {
        super();
        this.model = model;
        this.tableModel = new ResultSetTableModel();

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout(10, 10));

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        add(scrollPane, BorderLayout.CENTER);
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
