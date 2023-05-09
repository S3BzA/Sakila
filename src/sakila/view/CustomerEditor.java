package sakila.view;

import sakila.components.AddressEditor;
import sakila.model.AddressModel;
import sakila.model.CustomerModel;
import sakila.model.StoreModel;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Objects;

import static sakila.view.GridUtils.constraint;
import static sakila.view.GridUtils.horizontalFiller;

public class CustomerEditor extends JDialog {
    JTextField firstName = new JTextField();
    JTextField lastName = new JTextField();
    JTextField email= new JTextField();
    JComboBox<StoreModel.Store> storeSelect;
    AddressEditor addressEditor;

    boolean creator;

    boolean commit = false;
    public boolean hasCommited() { return commit; }
    CustomerModel.Customer curCustomer = null;
    AddressModel.Address curAddress = null;

    public CustomerEditor(Window owner, boolean creator) {
        super(owner, "Customer Editor", ModalityType.APPLICATION_MODAL);
        setSize(400, 600);
        this.creator = creator;

        JPanel main = new JPanel();
        setContentPane(main);
        setLayout(new GridBagLayout());

        add(new JLabel("First Name"), constraint(0, 0));
        add(firstName, constraint(0, 1));

        add(new JLabel("Last Name"), constraint(1, 0));
        add(lastName, constraint(1, 1));

        add(new JLabel("Email"), constraint(0, 2));
        add(email, constraint(0, 3, 2, 1));

        JPanel bottomBar = new JPanel();
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            if(!isValidClient()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled in", "Error creating customer", JOptionPane.ERROR_MESSAGE);
                return;
            }
            commit = true;
            setVisible(false);
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> setVisible(false));
        bottomBar.setLayout(new FlowLayout(FlowLayout.TRAILING));
        bottomBar.add(okButton);
        bottomBar.add(cancelButton);


        add(new JLabel("Store"), constraint(0, 4, 2, 1));
        try {
            StoreModel model = new StoreModel();
            storeSelect = new JComboBox<>(model.getDisplayNames());
            storeSelect.setEnabled(creator);

            add(storeSelect, constraint(0, 5, 2, 1));
            add(Box.createVerticalStrut(10), constraint(0, 6, 2, 1));

            AddressModel addressModel = new AddressModel();
            addressEditor = new AddressEditor(addressModel);
            add(addressEditor, constraint(0 ,7, 2, 1));
        }
        catch(SQLException ignored) {}

        add(Box.createVerticalGlue(), horizontalFiller(7));
        GridBagConstraints c = constraint(0, 8, 2, 1);
        add(bottomBar, c);
        pack();
    }
    public void setCustomer(CustomerModel.Customer customer) {
        firstName.setText(customer.firstName());
        lastName.setText(customer.lastName());
        email.setText(customer.email());
        curCustomer = customer;
    }
    public void setStore(StoreModel.Store store) {
        storeSelect.setSelectedItem(store);
    }
    public void setAddress(AddressModel.Address address) {
        addressEditor.setAddress(address);
        curAddress = address;
    }

    public AddressModel.Address getAddress() {
        AddressModel.Address edited = addressEditor.getAddress();
        if(creator) return edited;
        if(edited.equals(curAddress)) return null;
        return edited;
    }
    public CustomerModel.Customer getCustomer() {
        CustomerModel.Customer edited = new CustomerModel.Customer(
                firstName.getText(),
                lastName.getText(),
                email.getText(),
                creator ? -1 : curCustomer.address(),
                ((StoreModel.Store) Objects.requireNonNull(storeSelect.getSelectedItem())).id()
        );
        if(creator) return edited;
        if(edited.equals(curCustomer)) return null;
        return edited;
    }
    private boolean isValidClient() {
        if(!addressEditor.isValidAddress()) return false;
        if(firstName.getText().isEmpty()) return false;
        if(lastName.getText().isEmpty()) return false;
        if(email.getText().isEmpty()) return false;
        return true;
    }
}
