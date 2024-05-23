package shapes;

import main.GConstants;

import java.awt.geom.Line2D;

public class GLine extends GShape {
    @Override
    public GShape createShape() {
        return new GLine();
    }

    @Override
    public void setShape(int x1, int y1, int x2, int y2) {
        this.shape = new Line2D.Double(x1, y1, x2, y2);
    }

    @Override
    public void resizePoint(int x, int y) {
        Line2D line2D = (Line2D) shape;
        line2D.setLine(line2D.getX1(), line2D.getY1(), x, y);
    }
//
//    @Override
//    public boolean onShape(int x, int y) {
//        Line2D line2D = (Line2D) shape;
//        if(this.getSelected()) {
//            if (this.gAnchors.onShape(x, y)) {
//                return true;
//            }
//        }
//
//        if(line2D.ptLineDist(x, y) < 3) {
//            this.gAnchors.setSelectedAnchor(GConstants.EAnchors.MM);
//            return true;
//        }
//        return false;
//    }
}