package frames;

import javax.swing.JMenuBar;

public class GMenuBar extends JMenuBar {
    private GFileMenu fileMenu;
    public GMenuBar() {
        fileMenu = new GFileMenu("File");
        this.add(fileMenu);
    }
}

