package shapes;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class GOval extends GShape {
    @Override
    public GShape createShape() {
        return new GOval();
    }

    @Override
    public void setShape(int x1, int y1, int x2, int y2) {
        this.shape = new Ellipse2D.Double(x1, y1, x2 - x1, y2 - y1);
    }

    @Override
    public void resizePoint(int x, int y) {
        Ellipse2D ellipse2D = (Ellipse2D) shape;
        ellipse2D.setFrame(ellipse2D.getX(), ellipse2D.getY(), x - ellipse2D.getX(), y - ellipse2D.getY());
    }
}