package frames;

import javax.swing.*;

public class GFileMenu extends JMenu {
    private JMenuItem itemNew;
    public GFileMenu(String title) {
        super(title);

        itemNew = new JMenuItem("New");
        this.add(itemNew);
    }
}
