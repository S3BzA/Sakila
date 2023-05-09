package sakila.view;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class GenreTab extends JPanel{

    public GenreTab()
    {
        super();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("GenreTab"));
        add(panel, BorderLayout.LINE_END);
    }
}
