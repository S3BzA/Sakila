package sakila.components;

import sakila.model.AddressModel;
import sakila.view.GridUtils;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class AddressEditor extends JPanel {

    JTextField addr1 = new JTextField();
    JTextField addr2 = new JTextField();
    JTextField district = new JTextField();
    JTextField postalCode = new JTextField();
    JTextField phone = new JTextField();
    JComboBox<AddressModel.City> city;
    JComboBox<AddressModel.Country> country;

    public AddressEditor(AddressModel model) {
        super();
        setBorder(BorderFactory.createTitledBorder("Address"));
        setLayout(new GridBagLayout());

        add(new JLabel("Address 1"), GridUtils.constraint(0, 0, 2, 1));
        add(addr1, GridUtils.constraint(0, 1, 2, 1));

        add(new JLabel("Address 2"), GridUtils.constraint(0, 3, 2, 1));
        add(addr2, GridUtils.constraint(0, 4, 2, 1));

        try {
            city = new JComboBox<>();
            country = new JComboBox<>(model.getCountries());
        }
        catch(SQLException ignored){}

        add(new JLabel("City"), GridUtils.constraint(0, 5, 1, 1));
        add(city, GridUtils.constraint(0, 6, 1, 1));
        city.setEditable(true);

        add(new JLabel("Province/State"), GridUtils.constraint(1, 5, 1, 1));
        add(district, GridUtils.constraint(1, 6, 1, 1));

        add(new JLabel("Country"), GridUtils.constraint(0, 7, 1, 1));
        add(country, GridUtils.constraint(0, 8, 1, 1));
        country.setEditable(true);
        country.addActionListener(e -> {
            String countryFilter = getCountry();

            try {
                city.removeAllItems();
                for(AddressModel.City c : model.getCities(countryFilter)) {
                    city.addItem(c);
                }
            }
            catch(SQLException | NullPointerException ignored) {}
        });

        add(new JLabel("Postal Code"), GridUtils.constraint(1, 7, 1, 1));
        add(postalCode, GridUtils.constraint(1, 8, 1, 1));

        add(new JLabel("Telephone"), GridUtils.constraint(0, 9, 2, 1));
        add(phone, GridUtils.constraint(0, 10, 2, 1));
    }

    public void setAddress(AddressModel.Address address) {
        addr1.setText(address.addr1());
        addr2.setText(address.addr2());
        district.setText(address.district());
        postalCode.setText(address.postalCode());
        country.setSelectedItem(address.country());
        city.setSelectedItem(address.city());
        phone.setText(address.phone());
    }

    public AddressModel.Address getAddress() {
        return new AddressModel.Address(addr1.getText(), addr2.getText(),
            district.getText(), postalCode.getText(), phone.getText(),
            getCity(), getCountry());
    }

    private String getCity() {
        Object obj = city.getSelectedItem();
        if(obj == null) return null;
        if(obj instanceof String) return (String) obj;

        return ((AddressModel.City) obj).name();
    }
    private String getCountry() {
        Object obj = country.getSelectedItem();
        if(obj == null) return null;
        if(obj instanceof String) return (String) obj;

        return ((AddressModel.Country) obj).name();
    }
}