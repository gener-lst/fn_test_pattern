package frames;

import main.GConstants;
import shapes.*;
import transformer.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Vector;
import javax.swing.*;

public class GDrawingPanel extends JPanel {
    private enum EDrawingState {
        eIdle,
        eTransforming
    }
    private EDrawingState eDrawingState;
    private Vector<GShape> shapes;
    private LinkedList<Vector<GShape>> undoHistory;
    private LinkedList<Vector<GShape>> redoHistory;
    private GShape currentShape;
    private GShape selectedShape;
    private GShape copiedShape;
    private Cursor cursor;
    private GToolBar toolBar;
    private GMenuBar menuBar;
    private GTransformer transformer;
    private BufferedImage bufferedImage;
    private Graphics2D graphics2DBufferedImage;
    private Color color;
    private GEditor popupMenu;

    public void associate(GToolBar toolBar, GMenuBar gMenuBar) {
        this.toolBar = toolBar;
        this.menuBar = menuBar;
        popupMenu.associate(this);
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public Object getShapes() {
        return this.shapes;
    }
    public void setShapes(Object shapes) {
        this.shapes = (Vector<GShape>) shapes;
        this.repaint();
    }

    public void setTransformer(GTransformer transformer) {
        this.transformer = transformer;
    }
    public void setCurrentShape(GShape currentShape) {
        this.currentShape = currentShape;
    }
    public GShape getSelectedShape() {
        return selectedShape;
    }
    public void setCopiedShape(GShape copiedShape) {
        this.copiedShape = copiedShape;
    }
    public void setSelectedShape(GShape selectedShape) {
        this.selectedShape = selectedShape;
    }
    public LinkedList<Vector<GShape>> getUndoHistory() {
        return undoHistory;
    }
    public LinkedList<Vector<GShape>> getRedoHistory() {
        return redoHistory;
    }
    public GShape getCopiedShape() {
        return copiedShape;
    }

    public GDrawingPanel() {
        super();
        this.eDrawingState = EDrawingState.eIdle;
        this.shapes = new Vector<GShape>();
        this.currentShape = null;
        this.color = this.getBackground();

        this.undoHistory = new LinkedList<>();
        this.redoHistory = new LinkedList<>();

        this.setBackground(Color.WHITE);
        MouseEventHandler mouseEventHandler = new MouseEventHandler();
        this.addMouseListener(mouseEventHandler);
        this.addMouseMotionListener(mouseEventHandler);

        this.popupMenu = new GEditor("Edit");
        this.add(popupMenu);
    }

    public void initialize() {
        this.bufferedImage = (BufferedImage)this.createImage(getWidth(), getHeight());
        this.graphics2DBufferedImage = (Graphics2D)this.bufferedImage.getGraphics();
    }

    public void paint(Graphics graphics) {
        super.paint(graphics);

        this.graphics2DBufferedImage.clearRect(0, 0, this.getWidth(), this.getHeight());
        for(GShape shape: this.shapes) {
            shape.draw(this.graphics2DBufferedImage);
        }

        graphics.drawImage(this.bufferedImage, 0, 0, this);
    }

    public void paintShape(int x, int y) {
        this.selectedShape = this.onShape(x, y);
        if (this.selectedShape != null) {
            this.selectedShape.setColor(color);
            changeSelection(x, y);
            this.repaint();
        }
    }

    public void load(Object shapes){
        this.setShapes(shapes);
        this.graphics2DBufferedImage.clearRect(0, 0, this.getWidth(), this.getHeight());
        for (GShape shape:this.shapes) {
            shape.draw(this.graphics2DBufferedImage);
            if(shape.getSelected()){
                this.selectedShape = shape;
                this.currentShape = shape;
            }
        }
    }

    private GShape onShape(int x, int y) {
        for (GShape gShape : shapes) {
            if(gShape.onShape(x, y)) {
                return gShape;
            }
        }
        return null;
    }

    public void initTransforming(int x, int y) {
        if(this.toolBar.getESelectedShape() == GConstants.EShape.eSelect) {
            currentShape = onShape(x, y);
            if(currentShape == null) {
                this.currentShape = this.toolBar.getESelectedShape().getGShape().createShape();
                this.transformer = new GSelector(this.currentShape);
                this.transformer.initTransform(x, y);
            } else {
                GConstants.EAnchors eAnchors = currentShape.getSelectedAnchor();
                switch (eAnchors) {
                    case MM:
                        this.transformer = new GMover(this.currentShape);
                        this.transformer.initTransform(x, y);
                        break;
                    case RR:
                        this.transformer = new GRotator(this.currentShape);
                        this.transformer.initTransform(x, y);
                        break;
                    default:
                        this.transformer = new GResizer(this.currentShape);
                        this.transformer.initTransform(x, y);
                        break;
                }
            }
        } else {
            this.currentShape = this.toolBar.getESelectedShape().getGShape().createShape();
            this.transformer = new GDrawer(this.currentShape);
            this.transformer.initTransform(x, y);
        }
        this.changeSelection(x, y);
    }

    public void keepTransforming(int x, int y) {
        this.graphics2DBufferedImage.setXORMode(this.getBackground());
        this.currentShape.draw(this.graphics2DBufferedImage);
        this.getGraphics().drawImage(this.bufferedImage, 0, 0, this);
        this.transformer.keepTransform(x, y);
        this.currentShape.draw(this.graphics2DBufferedImage);
        this.getGraphics().drawImage(this.bufferedImage, 0, 0, this);
    }

    public void continueTransforming(int x, int y) {
        this.transformer.continueTransform(x, y);
    }

    public void finalizeTransforming() {
        this.graphics2DBufferedImage.setPaintMode();
        Graphics2D graphics2D = (Graphics2D) this.getGraphics();
        this.transformer.finalizeTransform(graphics2D, this.shapes);

        if(!(this.currentShape instanceof GSelect)) {
            this.shapes.add(this.currentShape);
            this.selectedShape = this.currentShape;
            this.selectedShape.setSelected(true);
        }

        Vector<GShape> curShapes = CopyShapes();
        this.undoHistory.add(curShapes);

        this.repaint();
        this.toolBar.resetSelectedShape();
    }

    private Vector<GShape> CopyShapes(){
        Vector<GShape> curShapes = new Vector<>();
        for (GShape gShape : this.shapes) {
            GShape shape = null;
            try {
                shape = (GShape) gShape.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            curShapes.add(shape);
        }

        return curShapes;
    }

    private void changeSelection(int x, int y) {
        clearSelection();
        this.repaint();
        this.selectedShape = this.onShape(x, y);
        if (this.selectedShape != null) {
            this.selectedShape.setSelected(true);
            this.selectedShape.draw((Graphics2D) this.getGraphics());
        }
    }

    private void clearSelection() {
        int minX = this.getX();
        int minY = this.getY();
        int maxX = this.getX() + this.getWidth();
        int maxY = this.getY() + this.getHeight();
        for(int y = maxY; y >= minY; y--){
            for(int x = maxX; x >= minX; x--){
                this.selectedShape = onShape(x, y);
                if(this.selectedShape != null) this.selectedShape.setSelected(false);
            }
        }
    }

    private void changeCursor(int x, int y) {
        cursor = new Cursor(Cursor.DEFAULT_CURSOR);
        if(toolBar.getESelectedShape() == GToolBar.EShape.eColor) {
            cursor = new Cursor(Cursor.HAND_CURSOR);
        } else if(toolBar.getESelectedShape() != GToolBar.EShape.eSelect) {
            cursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
        } else {
            this.currentShape = this.onShape(x, y);
            if(currentShape != null) {
                GConstants.EAnchors eAnchors = this.currentShape.getSelectedAnchor();
                cursor = eAnchors.getCursor();
            }
        }
        this.setCursor(cursor);
    }  // enum으로 맵핑 테이블 만들어서 코드 간략화시 추가 점수 --> 완

    private class MouseEventHandler implements MouseListener, MouseMotionListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1) {
                if(e.getClickCount() == 1) {
                    mouseL1Clicked(e);
                } else if (e.getClickCount() == 2) {
                    mouseL2Clicked(e);
                }
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                mouseRClicked(e);
            }

        }

        private void mouseL1Clicked(MouseEvent e) {
            if(eDrawingState == EDrawingState.eIdle) {
                if(toolBar.getESelectedShape().geteUserAction() == GConstants.EUserAction.eNPoint) {
                    eDrawingState = EDrawingState.eTransforming;
                    initTransforming(e.getX(), e.getY());
                } else if(toolBar.getESelectedShape().geteUserAction() == GConstants.EUserAction.ePaint) {
                    paintShape(e.getX(), e.getY());
                } else {
                    changeSelection(e.getX(), e.getY());
                }
            } else if (eDrawingState == EDrawingState.eTransforming) {
                if (toolBar.getESelectedShape().geteUserAction() == GConstants.EUserAction.eNPoint) {
                    continueTransforming(e.getX(), e.getY());
                }
            }
        }

        private void mouseL2Clicked(MouseEvent e) {
            if(eDrawingState == EDrawingState.eTransforming) {
                finalizeTransforming();
                eDrawingState = EDrawingState.eIdle;
            }
        }

        private void mouseRClicked(MouseEvent e) {
            popupMenu.show(e.getComponent(), e.getX(), e.getY());
        }


        @Override
        public void mouseMoved(MouseEvent e) {
            if (eDrawingState == EDrawingState.eTransforming) {
                if (toolBar.getESelectedShape().geteUserAction() == GConstants.EUserAction.eNPoint) {
                    keepTransforming(e.getX(), e.getY());
                }
            } else if (eDrawingState == EDrawingState.eIdle) {
                changeCursor(e.getX(), e.getY());
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(eDrawingState == EDrawingState.eIdle) {
                if(toolBar.getESelectedShape().geteUserAction() == GConstants.EUserAction.e2Point && e.getButton() != MouseEvent.BUTTON3) {
                    initTransforming(e.getX(),e.getY());
                    eDrawingState = EDrawingState.eTransforming;
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if(eDrawingState == EDrawingState.eTransforming) {
                keepTransforming(e.getX(), e.getY());
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(eDrawingState == EDrawingState.eTransforming) {
                if (toolBar.getESelectedShape().geteUserAction() == GConstants.EUserAction.e2Point) {
                    finalizeTransforming();
                    eDrawingState = EDrawingState.eIdle;
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}