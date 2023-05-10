package sakila;


import sakila.model.*;
import sakila.view.FilmTab;
import sakila.view.GenreTab;
import sakila.view.NotificationsTab;
import sakila.view.StaffTab;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws Exception {

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JFrame win = new JFrame("Sakila");
        win.setSize(800, 600);
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        StaffModel model = new StaffModel();
        FilmModel filmModel = new FilmModel();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Notifications", new NotificationsTab());
        tabbedPane.add("Staff", new StaffTab(model));
        tabbedPane.add("Film", new FilmTab(filmModel));
        tabbedPane.add("Report", new GenreTab());

        win.setContentPane(tabbedPane);
        win.setVisible(true);
    }
}
