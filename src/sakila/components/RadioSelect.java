package sakila.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class RadioSelect extends JPanel {
    ButtonGroup group = new ButtonGroup();
    String current = null;
    ArrayList<ActionListener> actionListener = new ArrayList<>();

    public RadioSelect(String title, String[][] labels) {
        super();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        ActionListener fn = actionEvent -> {
            if (actionEvent.getActionCommand().equals(current)) return;
            current = actionEvent.getActionCommand();
            for(ActionListener l : actionListener) l.actionPerformed(actionEvent);
        };

        add(new JLabel(title));
        for (String[] label : labels) {
            JRadioButton button = new JRadioButton(label[1]);
            Insets insets = button.getBorder().getBorderInsets(button);
            button.setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left + 10, insets.bottom, insets.right));
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
