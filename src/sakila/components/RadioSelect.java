package sakila.components;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class RadioSelect extends JPanel {
    ButtonGroup group = new ButtonGroup();
    String current = null;
    ArrayList<ActionListener> actionListener = new ArrayList<>();

    public RadioSelect(String[][] labels) {
        super();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        ActionListener fn = actionEvent -> {
            if (actionEvent.getActionCommand().equals(current)) return;
            current = actionEvent.getActionCommand();
            for(ActionListener l : actionListener) l.actionPerformed(actionEvent);
        };

        for (String[] label : labels) {
            JRadioButton button = new JRadioButton(label[1]);
            button.setActionCommand(label[0]);
            button.addActionListener(fn);
            add(button);
            group.add(button);
        }
    }

    public void addActionListener(ActionListener listener) {
        if(listener == null) return;
        actionListener.add(listener);
    }
}
