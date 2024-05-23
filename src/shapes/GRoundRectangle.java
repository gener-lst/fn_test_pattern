package shapes;

import java.awt.geom.RoundRectangle2D;

public class GRoundRectangle extends GShape{
    @Override
    public GShape createShape() {
        return new GRoundRectangle();
    }

    @Override
    public void setShape(int x1, int y1, int x2, int y2) {
        this.shape = new RoundRectangle2D.Double(x1, y1, x2 - x1, y2 - y1, 0, 0);
    }

    @Override
    public void resizePoint(int x, int y) {
        RoundRectangle2D roundRectangle2D = (RoundRectangle2D) shape;
        roundRectangle2D.setRoundRect(roundRectangle2D.getX(), roundRectangle2D.getY(), x - roundRectangle2D.getX(),
                y - roundRectangle2D.getY(), (x - roundRectangle2D.getX())/4, (y - roundRectangle2D.getY())/4);
    }
}
