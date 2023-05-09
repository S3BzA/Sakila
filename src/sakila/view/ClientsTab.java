package sakila.view;

import sakila.model.AddressModel;
import sakila.model.CustomerModel;
import sakila.model.ResultSetTableModel;
import sakila.model.StoreModel;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientsTab extends JPanel {
    CustomerModel customerModel;
    AddressModel addressModel;
    StoreModel storeModel;
    ResultSetTableModel tableModel;

    public ClientsTab(CustomerModel customerModel, AddressModel addressModel, StoreModel storeModel) {
        super();
        this.customerModel = customerModel;
        this.addressModel = addressModel;
        this.storeModel = storeModel;

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

            int client = customerModel.getCustomerFromRow(row);
            if(client == -1) return;
            try {
                customerModel.removeUser(client);
                updateResults();
            }
            catch(SQLException ex) {
                exceptionError(ex, "Error deleting customer");
            }
        });
        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row == -1) return;

            int id = customerModel.getCustomerFromRow(row);
            if(id == -1) return;

            try {
                CustomerModel.Customer customer = customerModel.getByID(id);
                AddressModel.Address address = addressModel.getByID(customer.address());
                StoreModel.Store store = storeModel.getByID(customer.store());

                CustomerEditor editor = new CustomerEditor(SwingUtilities.getWindowAncestor(this), false);
                editor.setCustomer(customer);
                editor.setAddress(address);
                editor.setStore(store);
                editor.setVisible(true);
                if(!editor.hasCommited()) return;
                System.out.println("Done");
            }
            catch(SQLException ex) {
                exceptionError(ex, "Error editing Customer");
            }

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
            ResultSet set = customerModel.getAll();
            tableModel.setResultSet(set);
        }
        catch(SQLException e) {
            exceptionError(e, "Error updating results");
        }
    }
}
