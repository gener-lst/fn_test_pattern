package shapes;

import java.awt.*;

public class GTriangle extends GShape{
    @Override
    public GShape createShape() {
        return new GTriangle();
    }

    @Override
    public void setShape(int x1, int y1, int x2, int y2) {
        this.shape = new Polygon();
        Polygon polygon = (Polygon) shape;
        polygon.addPoint(x1, y1);
        polygon.addPoint(x2, y2);
        polygon.addPoint(x2, y2);
    }

    public void addPoint(int x, int y) {
        Polygon polygon = (Polygon) shape;
        polygon.addPoint(x, y);
    }

    @Override
    public void resizePoint(int x, int y) {
        Polygon polygon = (Polygon) shape;
        polygon.xpoints[0] = (int) (polygon.getBounds().getX() + x) / 2;
        polygon.xpoints[1] = (int) polygon.getBounds().getX();
        polygon.ypoints[1] = y;
        polygon.xpoints[2] = x;
        polygon.ypoints[2] = y;
    }
}