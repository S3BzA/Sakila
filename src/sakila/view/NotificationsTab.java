package sakila.view;

import javax.swing.*;
import java.sql.SQLException;

public class NotificationsTab extends JTabbedPane {
    public NotificationsTab() throws SQLException {
        super();

        ClientsTab clients = new ClientsTab();
        DropsTab drops = new DropsTab();
        addTab("Subscriptions", clients);
        addTab("Cancelled", drops);
        addChangeListener(e -> {
            clients.updateResults();
            drops.updateResults();

        });
    }

}
