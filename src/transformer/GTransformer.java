package transformer;

import shapes.GAnchors;
import shapes.GShape;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Vector;

public abstract class GTransformer {
    protected GShape shape;
    protected AffineTransform affineTransform;
    protected GAnchors anchors;
    protected int px, py;

    public GTransformer(GShape shape) {
        this.shape = shape;
        this.affineTransform = this.shape.getAffineTransform();
        this.anchors = this.shape.getAnchors();
    }

    abstract public void initTransform(int x, int y);
    abstract public void keepTransform(int x, int y);
    public void continueTransform(int x, int y) {}
    public void finalizeTransform(Graphics2D graphics2D, Vector<GShape> shapes){
        Shape shape = this.affineTransform.createTransformedShape(this.shape.getShape());
        this.shape.setShape(shape);
        this.shape.setSelected(true);
        this.affineTransform.setToIdentity();
    }
}
