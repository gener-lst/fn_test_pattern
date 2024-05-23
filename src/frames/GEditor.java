package frames;

import main.GConstants;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GEditor extends JPopupMenu {
    private GDrawingPanel gDrawingPanel;
    private GEditor that;

    public GEditor(String title) {
        super(title);
        that = this;

        ActionHandler actionHandler = new ActionHandler();
        for(GConstants.EEditMenu eMenuItem: GConstants.EEditMenu.values()) {
            JMenuItem menuItem = new JMenuItem(eMenuItem.getLabel());
            menuItem.addActionListener(actionHandler);
            menuItem.setActionCommand(eMenuItem.name());
            this.add(menuItem);
        }
    }

    public GDrawingPanel getDrawingPanel() {
        return gDrawingPanel;
    }

    public void associate(GDrawingPanel gDrawingPanel) {
        this.gDrawingPanel = gDrawingPanel;
    }

    class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            GConstants.EEditMenu.valueOf(e.getActionCommand()).action(that);
        }
    }
}
