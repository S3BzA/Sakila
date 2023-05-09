package sakila.components;

import sakila.model.StoreModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class StoreSelector extends JPanel {
    Integer store = null;

    JLabel storeLabel = new JLabel("No store selected");
    ArrayList<ActionListener> listeners = new ArrayList<>();

    public StoreSelector(StoreModel model, Integer store) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        chooseStore(store);

        JButton select = new JButton("Select");
        select.addActionListener(e -> {
            try {
                StoreModel.Store[] stores = model.getDisplayNames();
                StoreModel.Store choice = (StoreModel.Store) JOptionPane.showInputDialog(this, "Choose a store",
                "Choose Store", JOptionPane.PLAIN_MESSAGE,
                null, stores, null);
                chooseStore(choice.id());
            }
            catch(SQLException ex) {
                exceptionError(ex, "Failed to get stores");
                chooseStore(null);
            }
        });
        add(storeLabel);
        add(select);
    }
    void chooseStore(Integer store) {
        this.store = store;
        updateLabel();
        ActionEvent ev = new ActionEvent(this, 0, store == null ? null : store.toString());
        for(ActionListener l : listeners) {
            l.actionPerformed(ev);
        }
    }
    void exceptionError(SQLException exception, String title) {
        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                exception.getMessage(), title, JOptionPane.ERROR_MESSAGE);
    }
    void updateLabel() {
        if(store != null) {
            storeLabel.setText("Selected Store: " + store);
        }
        else storeLabel.setText("No store selected");
    }
    void addActionListener(ActionListener listener) {
        listeners.add(listener);
    }

}
