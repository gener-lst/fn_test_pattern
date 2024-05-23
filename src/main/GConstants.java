package main;

import frames.GDrawingPanel;
import frames.GEditor;
import shapes.*;
import transformer.GMover;
import transformer.GTransformer;

import java.awt.*;
import java.util.Vector;

public class GConstants {
    public class GMainFrame {
        static final int x = 200;
        static final int y = 100;
        static final int w = 600;
        static final int h = 400;
    }

    public enum EUserAction {
        e2Point,
        eNPoint,
        ePaint
    }

    public enum EShape {
        eSelect("Select", new GSelect(), EUserAction.e2Point),
        eRectangle("Rectangle", new GRectangle(), EUserAction.e2Point),
        eOval("Oval", new GOval(), EUserAction.e2Point),
        eLine("Line", new GLine(), EUserAction.e2Point),
        ePolygon("Polygon", new GPolygon(), EUserAction.eNPoint),
        eTriangle("Triangle", new GTriangle(), EUserAction.e2Point),
        eRoundRect("RoundRect", new GRoundRectangle(), EUserAction.e2Point),
        eColor("Coloring", null, EUserAction.ePaint);

        private String name;
        private GShape gShape;
        private EUserAction eUserAction;

        private EShape(String name, GShape gShape, EUserAction eUserAction) {
            this.name = name;
            this.gShape = gShape;
            this.eUserAction = eUserAction;
        }

        public String getName() {
            return this.name;
        }
        public GShape getGShape() {
            return this.gShape;
        }

        public EUserAction geteUserAction() {
            return eUserAction;
        }
    }

    public enum EAnchors {
        NW(new Cursor(Cursor.NW_RESIZE_CURSOR)),
        NN(new Cursor(Cursor.N_RESIZE_CURSOR)),
        NE(new Cursor(Cursor.NE_RESIZE_CURSOR)),
        SE(new Cursor(Cursor.SE_RESIZE_CURSOR)),
        SS(new Cursor(Cursor.S_RESIZE_CURSOR)),
        SW(new Cursor(Cursor.SW_RESIZE_CURSOR)),
        WW(new Cursor(Cursor.W_RESIZE_CURSOR)),
        EE(new Cursor(Cursor.E_RESIZE_CURSOR)),
        RR(new Cursor(Cursor.HAND_CURSOR)),
        MM(new Cursor(Cursor.MOVE_CURSOR));

        private Cursor cursor;
        private EAnchors(Cursor cursor) {
            this.cursor = cursor;
        }

        public Cursor getCursor() {
            return cursor;
        }
    }

    public enum EEditMenu {
        eUndo("Undo"){
            @Override
            public void action(GEditor gEditor) {
                GDrawingPanel drawingPanel = gEditor.getDrawingPanel();
                Vector<GShape> shapes = new Vector<>();
                drawingPanel.getRedoHistory().addFirst(drawingPanel.getUndoHistory().pollLast());
                if (!drawingPanel.getUndoHistory().isEmpty()) {
                    shapes = drawingPanel.getUndoHistory().peekLast();
                }
                drawingPanel.load(shapes);
            }
        },

        eRedo("Do") {
            @Override
            public void action(GEditor gEditor) {
                GDrawingPanel drawingPanel = gEditor.getDrawingPanel();
                Vector<GShape> shapes = (drawingPanel.getRedoHistory().isEmpty()) ? new Vector<>() : drawingPanel.getRedoHistory().poll();
                shapes = (shapes == null) ? new Vector<>() : shapes;
                if(!shapes.isEmpty()){
                    drawingPanel.getUndoHistory().add(shapes);
                }
                drawingPanel.load(shapes);
            }
        },
        eCut("Cut") {
            @Override
            public void action(GEditor gEditor) {
                try {
                    GDrawingPanel drawingPanel = gEditor.getDrawingPanel();
                    GShape selectedShape = drawingPanel.getSelectedShape();
                    drawingPanel.setCopiedShape((GShape)selectedShape.clone());
                    Vector<GShape> shapes = (Vector<GShape>)drawingPanel.getShapes();
                    Vector<GShape> newShapes = new Vector<>();
                    for (GShape shape : shapes) {
                        if (!shape.equals(selectedShape)) {
                            newShapes.add(shape);
                        }
                    }

                    drawingPanel.setCurrentShape(null);
                    drawingPanel.setSelectedShape(null);
                    drawingPanel.getUndoHistory().add(newShapes);
                    drawingPanel.load(newShapes);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        },
        eCopy("Copy") {
            @Override
            public void action(GEditor gEditor) {
                try {
                    GDrawingPanel drawingPanel = gEditor.getDrawingPanel();
                    drawingPanel.setCopiedShape((GShape)drawingPanel.getSelectedShape().clone());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        },
        ePaste("Paste") {
            @Override
            public void action(GEditor gEditor) {
                GDrawingPanel drawingPanel = gEditor.getDrawingPanel();
                GShape copiedShape = drawingPanel.getCopiedShape();
                GTransformer transformer = new GMover(copiedShape);
                drawingPanel.setTransformer(transformer);
                drawingPanel.setCurrentShape(copiedShape);
                transformer.keepTransform(5, 5);
                drawingPanel.finalizeTransforming();
            }
        },
        eDelete("Delete") {
            @Override
            public void action(GEditor gEditor) {
                GDrawingPanel drawingPanel = gEditor.getDrawingPanel();
                GShape selectedShape = drawingPanel.getSelectedShape();
                Vector<GShape> shapes = (Vector<GShape>)drawingPanel.getShapes();
                Vector<GShape> newShapes = new Vector<>();
                for (GShape shape : shapes) {
                    if (!shape.equals(selectedShape)) {
                        newShapes.add(shape);
                    }
                }

                drawingPanel.setCurrentShape(null);
                drawingPanel.setSelectedShape(null);
                drawingPanel.getUndoHistory().add(newShapes);
                drawingPanel.load(newShapes);
            }
        };

        private String label;

        private EEditMenu(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public abstract void action(GEditor gEditor);
    }
}
