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
        this.model =


        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//        panel.add(new JLabel("GenreTab"));

        add(panel, BorderLayout.LINE_END);
    }
}
