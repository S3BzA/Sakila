package sakila.view;

import java.awt.*;

public class GridUtils {
    public static GridBagConstraints constraint(int gridx, int gridy, int spanx, int spany) {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridwidth = spanx;
        c.gridheight = spany;
        c.gridx = gridx;
        c.gridy = gridy;
        return c;
    }
    public static GridBagConstraints constraint(int gridx, int gridy) {
        return constraint(gridx, gridy, 1, 1);
    }
    public static GridBagConstraints horizontalFiller(int gridy) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.VERTICAL;
        c.weightx = 0;
        c.weighty = 1;
        c.gridwidth = 2;
        c.gridy = gridy;
        return c;

    }
}
