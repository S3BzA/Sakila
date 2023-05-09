package sakila.view;

import sakila.components.AddressEditor;
import sakila.model.AddressModel;
import sakila.model.CustomerModel;
import sakila.model.StoreModel;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

import static sakila.view.GridUtils.constraint;
import static sakila.view.GridUtils.horizontalFiller;

public class CustomerEditor extends JDialog {
    JTextField firstName = new JTextField();
    JTextField lastName = new JTextField();
    JTextField email= new JTextField();
    JComboBox<StoreModel.Store> storeSelect;
    AddressEditor addressEditor;

    boolean creator;

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
        bottomBar.setLayout(new FlowLayout(FlowLayout.TRAILING));
        bottomBar.add(new JButton("OK"));
        bottomBar.add(new JButton("Cancel"));

        add(new JLabel("Store"), constraint(0, 4, 2, 1));
        try {
            StoreModel model = StoreModel.getInstance();
            storeSelect = new JComboBox<>(model.getDisplayNames());
            storeSelect.setEnabled(creator);

            add(storeSelect, constraint(0, 5, 2, 1));
            add(Box.createVerticalStrut(10), constraint(0, 6, 2, 1));

            AddressModel addressModel = AddressModel.getInstance();
            AddressEditor add = new AddressEditor(addressModel);
            add(add, constraint(0 ,7, 2, 1));
        }
        catch(SQLException ignored) {}

        add(Box.createVerticalGlue(), horizontalFiller(7));
        GridBagConstraints c = constraint(0, 8, 2, 1);
        add(bottomBar, c);
    }
    public void setCustomer(CustomerModel.Customer customer) {
        firstName.setText(customer.firstName());
        lastName.setText(customer.lastName());
        email.setText(customer.email());
    }
    public void setStore(StoreModel.Store store) {
        storeSelect.setSelectedItem(store);
    }
    public void setAddress(AddressModel.Address address) {
        addressEditor.setAddress(address);
    }
}