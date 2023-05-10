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

    public ClientsTab() throws SQLException {
        super();
        this.customerModel = new CustomerModel();
        this.addressModel = new AddressModel();
        this.storeModel = new StoreModel();

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

                CustomerModel.Customer editedCustomer = editor.getCustomer();
                AddressModel.Address editedAddress = editor.getAddress();
                if(editedAddress != null) {
                    int aid = addressModel.addAddress(editedAddress);
                    if(editedCustomer != null) editedCustomer = new CustomerModel.Customer(
                            editedCustomer.firstName(),
                            editedCustomer.lastName(),
                            editedCustomer.email(),
                            aid,
                            editedCustomer.store()
                    );
                }

                if(editedCustomer != null) customerModel.updateCustomer(id, editedCustomer);
                if(editedCustomer == null || editedAddress == null) updateResults();
            }
            catch(SQLException ex) {
                exceptionError(ex, "Error editing Customer");
            }

        });
        JButton addButton = new JButton("Add Customer");
        addButton.addActionListener(e -> {
            CustomerEditor editor = new CustomerEditor(SwingUtilities.getWindowAncestor(this), true);
            editor.setVisible(true);
            if(!editor.hasCommited()) return;

            CustomerModel.Customer newCustomer = editor.getCustomer();
            AddressModel.Address newAddress = editor.getAddress();


            System.out.println(newCustomer);
            System.out.println(newAddress);
            try {
                int id = addressModel.addAddress(newAddress);
                customerModel.addUser(newCustomer.firstName(), newCustomer.lastName(), newCustomer.lastName(), id, newCustomer.store());
                updateResults();
            }
            catch(SQLException ex) {
                exceptionError(ex, "Error creating Customer");
            }

        });

        sidebar.add(deleteButton);
        sidebar.add(editButton);
        sidebar.add(addButton);

        add(scrollPane, BorderLayout.CENTER);
        add(sidebar, BorderLayout.PAGE_END);
        updateResults();
    }
    void exceptionError(SQLException exception, String title) {
        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                exception.getMessage(), title, JOptionPane.ERROR_MESSAGE);
    }
    public void updateResults() {
        try {
            ResultSet set = customerModel.getAll();
            tableModel.setResultSet(set);
        }
        catch(SQLException e) {
            exceptionError(e, "Error updating results");
        }
    }
}
