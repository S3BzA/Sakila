package sakila.view;

import sakila.model.AddressModel;
import sakila.model.DropModel;
import sakila.model.ResultSetTableModel;
import sakila.model.StoreModel;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DropsTab extends JPanel {
    DropModel dropModel;
    StoreModel storeModel;
    AddressModel addressModel;

    ResultSetTableModel tableModel;

    JTextField name;
    JComboBox<AddressModel.Country> country;
    JComboBox<StoreModel.Store> store;

    public DropsTab() throws SQLException {

        super();
        dropModel = new DropModel();
        storeModel = new StoreModel();
        addressModel = new AddressModel();

        name = new JTextField();
        country = new JComboBox<>(addressModel.getCountries());
        store = new JComboBox<>(storeModel.getDisplayNames());
        tableModel = new ResultSetTableModel();

        name.addActionListener(e -> {
            if(name.getText().isEmpty()) dropModel.searchName(null);
            else dropModel.searchName(name.getText());

            updateResults();
        });
        country.addActionListener(e -> {
            AddressModel.Country c = (AddressModel.Country) country.getSelectedItem();

            if(c == null) dropModel.searchCountry(null);
            else if(c.id() == -1) dropModel.searchCountry(null);
            else dropModel.searchCountry(c.name());

            updateResults();
        });
        store.addActionListener(e -> {
            StoreModel.Store s = (StoreModel.Store) store.getSelectedItem();

            if(s == null) dropModel.searchCountry(null);
            else if(s.id() == -1) dropModel.searchCountry(null);
            else dropModel.searchStore(Integer.toString(s.id()));

            updateResults();
        });

        country.addItem(new AddressModel.Country(-1, "Any"));
        country.setSelectedIndex(country.getItemCount()-1);

        store.addItem(new StoreModel.Store(-1, "Any"));
        store.setSelectedIndex(store.getItemCount()-1);

        this.tableModel = new ResultSetTableModel();

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout(10, 10));

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        JPanel sidebar = new JPanel();

        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.PAGE_AXIS));

        sidebar.add(new JLabel("Name"));
        sidebar.add(name);

        sidebar.add(new JLabel("Country"));
        sidebar.add(country);

        sidebar.add(new JLabel("Store"));
        sidebar.add(store);

        add(scrollPane, BorderLayout.PAGE_START);
        add(scrollPane, BorderLayout.CENTER);
        add(sidebar, BorderLayout.LINE_END);
        updateResults();
    }
    private void updateResults() {
        try {
            ResultSet set = dropModel.fetchAll();
            tableModel.setResultSet(set);
        }
        catch(SQLException ignored) {
        }
    }
}
