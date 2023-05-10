package sakila.view;

import sakila.model.GenreModel;
import sakila.model.ResultSetTableModel;


import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreTab extends JPanel{

    GenreModel model;
    ResultSetTableModel tableModel;

    public GenreTab(GenreModel model)
    {
        super();
        this.model = model;
        this.tableModel = new ResultSetTableModel();

        setLayout(new BorderLayout());

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);


        add(table.getTableHeader(), BorderLayout.PAGE_START);
        add(scrollPane, BorderLayout.CENTER);
        updateResults();
    }

    public void updateResults() {
        try {
            ResultSet set = model.fetchResults();
            tableModel.setResultSet(set);
        }
        catch(SQLException ignored) {
        }
    }
}
