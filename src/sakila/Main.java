package sakila;

import sakila.model.DatabaseConfig;
import sakila.model.StaffModel;
import sakila.model.FilmModel;
import sakila.view.StaffTab;
import sakila.view.FilmTab;

import javax.swing.*;
import java.sql.*;

public class Main {
    public static void main(String[] args) throws Exception {

        JFrame frame = new JFrame("Film");
        JPanel panel = new FilmTab(new FilmModel());

        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,400);
        frame.setVisible(true);

//        System.out.println("closed! " + count);
//        frame.setContentPane(panel);
//        frame.setVisible(true);
    }
}
