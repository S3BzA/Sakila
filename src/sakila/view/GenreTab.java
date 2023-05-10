package sakila.view;

import sakila.model.GenreModel;
import sakila.model.ResultSetTableModel;


import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class GenreTab extends JPanel{

    GenreModel model;
    ResultSetTableModel tableModel;

    public GenreTab(GenreModel model)
    {
        super();
        this.model = model;
        this.tableModel = new ResultSetTableModel();

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        panel.add(scrollPane);

        add(panel, BorderLayout.LINE_END);
        updateResults();
    }

    private void updateResults() {
        try {
            ResultSet set = model.fetchResults();
            tableModel.setResultSet(set);
        }
        catch(SQLException ignored) {
        }
    }
}
