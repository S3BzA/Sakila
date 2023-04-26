package sakila;

import javax.swing.*;
import java.sql.*;

public class Main {
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame();
        frame.setSize(800, 600);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        System.out.println("connecting...");
        Connection conn = DriverManager.getConnection("jdbc:mariadb://wheatley.cs.up.ac.za/u22563777", "u22563777", "OSRXLTJJCHWLYNT7K7NFL7SPMHEVDG3X");
        PreparedStatement statement = conn.prepareStatement("SELECT make, model FROM cars");
        ResultSet set = statement.executeQuery();
        int count = 0;
        while(set.next()) {
            count++;
            String make = set.getString(1);
            String model = set.getString(2);
            panel.add(new JLabel(make + " " + model));
        }

        conn.close();
        System.out.println("closed! " + count);
        frame.setContentPane(panel);
        frame.setVisible(true);


    }
}
