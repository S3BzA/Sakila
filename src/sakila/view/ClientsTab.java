package sakila.view;

import sakila.model.CustomerModel;
import sakila.model.ResultSetTableModel;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientsTab extends JPanel {
    CustomerModel model;
    ResultSetTableModel tableModel;

    public ClientsTab(CustomerModel model) {
        super();
        this.model = model;
        this.tableModel = new ResultSetTableModel();

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout(10, 10));

        JTable table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.LINE_AXIS));

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row == -1) return;

            int client = model.getCustomerFromRow(row);
            if(client == -1) return;
            try {
                model.removeUser(client);
                updateResults();
            }
            catch(SQLException ex) {
                exceptionError(ex, "Error deleting customer");
            }
        });
        CustomerEditor editor = new CustomerEditor(SwingUtilities.getWindowAncestor(this), false);
        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> {
            editor.setVisible(true);
        });
        sidebar.add(deleteButton);
        sidebar.add(editButton);

        add(scrollPane, BorderLayout.CENTER);
        add(sidebar, BorderLayout.PAGE_END);
        updateResults();
    }
    void exceptionError(SQLException exception, String title) {
        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                exception.getMessage(), title, JOptionPane.ERROR_MESSAGE);
    }
    private void updateResults() {
        try {
            ResultSet set = model.getAll();
            tableModel.setResultSet(set);
        }
        catch(SQLException e) {
            exceptionError(e, "Error updating results");
        }
    }
}
