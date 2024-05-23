package main;

import frames.GDrawingPanel;
import frames.GEditor;
import frames.GMenuBar;
import frames.GToolBar;

import javax.swing.JFrame;
import java.awt.*;

public class GMainFrame extends JFrame {
    private GMenuBar gMenuBar;
    private GToolBar gToolBar;
    private GDrawingPanel gDrawingPanel;

    public GMainFrame() {
        this.setLocation(GConstants.GMainFrame.x, GConstants.GMainFrame.y);
        this.setSize(GConstants.GMainFrame.w, GConstants.GMainFrame.h);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BorderLayout borderLayout = new BorderLayout();
        this.setLayout(borderLayout);

        this.gMenuBar = new GMenuBar();
        this.setJMenuBar(gMenuBar);

        this.gToolBar = new GToolBar();
        this.add(this.gToolBar, BorderLayout.NORTH);
        this.gToolBar.setFloatable(false);

        this.gDrawingPanel = new GDrawingPanel();
        this.add(gDrawingPanel, BorderLayout.CENTER);

        this.gDrawingPanel.associate(gToolBar, gMenuBar);
        this.gToolBar.associate(gDrawingPanel);
    }

    public void initialize() {
        this.gDrawingPanel.initialize();
    }
}