package sakila.view;

import sakila.model.FilmModel;
import sakila.model.ResultSetTableModel;
import sakila.model.StaffModel;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class FilmTab extends JPanel
{
    FilmModel model;
    ResultSetTableModel tableModel;

    public FilmTab(FilmModel model)
    {
        super();
        this.model = model;
        this.tableModel = new ResultSetTableModel();

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout(10, 10));

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        String[] placeholderTexts = {
                "Title",
                "Description",
                "Release Year",
                "Language",
                "Original Language",
                "Rental Duration",
                "Rental Rate",
                "Length",
                "Replacement Cost",
                "Rating",
                "Special Features"
        };

        JDialog inputPane = new JDialog();
        inputPane.getContentPane().setLayout(new GridBagLayout());

        GridBagConstraints bagConst = new GridBagConstraints();
        bagConst.fill = GridBagConstraints.HORIZONTAL;
        bagConst.weightx = 1;
        bagConst.insets = new Insets(6,3,6,3);
        bagConst.ipady = 8;
        bagConst.gridy = 0;
        HashMap<Integer, JTextField> txfFields = new HashMap<>();
        int index = 0;
        for (String text : placeholderTexts) {
            JTextField textField = new JTextField();
            textField.setSize(100,30);
            textField.setToolTipText(text);
            textField.setHorizontalAlignment(JTextField.CENTER);
            textField.setText(text);
            txfFields.put(index,textField);
            inputPane.add(textField,bagConst);
            index++;
            bagConst.gridy++;
        }
        JButton btnSubmit = new JButton("Insert");
        btnSubmit.addActionListener(e -> {
            model.setParameter("title",txfFields.get(0).getText());
            model.setParameter("description",txfFields.get(1).getText());
            model.setParameter("release_year",txfFields.get(2).getText());
            model.setParameter("rental_duration",txfFields.get(5).getText());
            model.setParameter("rental_rate",txfFields.get(6).getText());
            model.setParameter("length",txfFields.get(7).getText());
            model.setParameter("replacement_cost",txfFields.get(8).getText());
            model.setParameter("rating",txfFields.get(9).getText());
            model.setParameter("special_features",txfFields.get(10).getText());
            try
            {
                model.insertFilm(txfFields.get(3).getText(),txfFields.get(4).getText());
                System.out.println("Film Inserted Successfully");
            }
            catch(SQLException ignored)
            {
                System.out.println("Film Insertion Failed >> FilmTab.java");
            }
            updateResults();
        });

        inputPane.add(btnSubmit,bagConst);
        inputPane.pack();

        JButton btnInsert = new JButton("Insert");
        btnInsert.addActionListener(e -> {
            inputPane.setVisible(true);
        });


        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.LINE_END);
        panel.add(btnInsert);
        updateResults();
    }

    private void updateResults() {
        try {
            ResultSet set = model.getResults();
            tableModel.setResultSet(set);
        }
        catch(SQLException ignored) {
        }
    }
}
