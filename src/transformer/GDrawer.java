package transformer;

import shapes.GShape;

import java.awt.*;
import java.util.Vector;

public class GDrawer extends GTransformer {
    public GDrawer(GShape shape) {
        super(shape);
    }

    public void initTransform(int x, int y) {
        this.shape.setShape(x, y, x, y);
    }

    public void keepTransform(int x, int y) {
        this.shape.resizePoint(x, y);
    }

    public void continueTransform(int x, int y) {this.shape.addPoint(x, y);}

//    public void finalizeTransform(Graphics2D graphics2D, Vector<GShape> shapes) {
//        shapes.add(this.shape);
//        this.shape.setSelected(true);
//        this.shape.draw(graphics2D);
//    }
}
