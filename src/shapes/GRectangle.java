package shapes;

import java.awt.*;

public class GRectangle extends GShape {
    public GRectangle() {this.shape = new Rectangle();}

    @Override
    public GShape createShape() {
        return new GRectangle();
    }

    @Override
    public void setShape(int x1, int y1, int x2, int y2) {
        this.shape = new Rectangle(x1, y1, x2 - x1, y2 - y1);
    }

    @Override
    public void resizePoint(int x, int y) {
        Rectangle rectangle = (Rectangle) shape;
        rectangle.setSize(x - rectangle.x, y - rectangle.y);
    }
}