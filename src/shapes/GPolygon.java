package shapes;

import java.awt.*;

public class GPolygon extends GShape{
    @Override
    public GShape createShape() {
        return new GPolygon();
    }

    @Override
    public void setShape(int x1, int y1, int x2, int y2) {
        this.shape = new Polygon();
        Polygon polygon = (Polygon) shape;
        polygon.addPoint(x1, y1);
        polygon.addPoint(x2, y2);
    }

    public void addPoint(int x, int y) {
        Polygon polygon = (Polygon) shape;
        polygon.addPoint(x, y);
    }

    @Override
    public void resizePoint(int x, int y) {
        Polygon polygon = (Polygon) shape;
        polygon.xpoints[polygon.npoints - 1] = x;
        polygon.ypoints[polygon.npoints - 1] = y;
    }
}
