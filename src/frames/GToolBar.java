package frames;

import main.GConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GToolBar extends JToolBar {
    public static GConstants.EShape EShape;
    ButtonGroup btnGroup;
    private GDrawingPanel gdrawingPanel;
    private GConstants.EShape eSelectedShape;
    public GConstants.EShape getESelectedShape() {
        return this.eSelectedShape;
    }

    public void resetSelectedShape() {
        JRadioButton selectButton = (JRadioButton) this.getComponent(GConstants.EShape.eSelect.ordinal());
        selectButton.doClick();
        this.eSelectedShape = GConstants.EShape.eSelect;
    }

    public void associate(GDrawingPanel gDrawingPanel) {
        this.gdrawingPanel = gDrawingPanel;
    }

    public GToolBar() {
        super();
        ActionHandler actionHandler = new ActionHandler();
        btnGroup = new ButtonGroup();

        for(GConstants.EShape eShape : GConstants.EShape.values()) {
            JRadioButton buttonShape = new JRadioButton(eShape.getName());
            btnGroup.add(buttonShape);
            this.add(buttonShape);
            buttonShape.setActionCommand(eShape.toString());
            buttonShape.addActionListener(actionHandler);
        }
        resetSelectedShape();
    }

    private class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            eSelectedShape = GConstants.EShape.valueOf(e.getActionCommand());
            if(eSelectedShape == GConstants.EShape.eColor){
                Color selectedColor = JColorChooser.showDialog(gdrawingPanel, "ColorChooser", Color.GRAY);
                gdrawingPanel.setColor(selectedColor);
            }
        }
    }
}