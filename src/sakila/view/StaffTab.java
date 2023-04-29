package sakila.view;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

public class StaffTab extends JPanel {
    public StaffTab(AbstractTableModel tableModel) {
        super();

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout(10, 10));

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new Label("Sidebar"));
        panel.setMinimumSize(new Dimension(200, 300));

        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.LINE_END);

    }

}
