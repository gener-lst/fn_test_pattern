package shapes;

import main.GConstants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.Serializable;

abstract public class GShape implements Serializable, Cloneable {
    protected Shape shape;
    protected AffineTransform affineTransform;
    protected GAnchors gAnchors;
    private boolean bSelected;
    private Color color;

    public Shape getShape() {
        return shape;
    }
    public void setShape(Shape shape) {
        this.shape = shape;
    }
    public AffineTransform getAffineTransform() {
        return affineTransform;
    }
    public GAnchors getAnchors() {
        return gAnchors;
    }
    public boolean getSelected() {
        return bSelected;
    }
    public void setSelected(boolean bSelected) {
        this.bSelected = bSelected;
    }
    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public GConstants.EAnchors getSelectedAnchor() {
        return this.gAnchors.getSelectedAnchor();
    }

    public GShape() {
        this.bSelected = false;
        this.affineTransform = new AffineTransform();
        this.affineTransform.setToIdentity();
        this.gAnchors = new GAnchors();
        this.color = null;
    }

    abstract public GShape createShape();

    public Object clone() throws CloneNotSupportedException {
        GShape shape = (GShape)super.clone();
        shape.gAnchors = (GAnchors)gAnchors.clone();
        return shape;
    }

    public void addPoint(int x, int y) {}

    public void draw(Graphics2D graphics2D) {
        Shape transformedShape = this.affineTransform.createTransformedShape(this.shape);
        graphics2D.draw(transformedShape);

        if (color != null) {
            graphics2D.setPaint(color);
            graphics2D.fill(transformedShape);
            graphics2D.setPaint(Color.BLACK);
            graphics2D.draw(transformedShape);
        }

        if(this.bSelected) {
            this.gAnchors.draw(graphics2D, this.shape.getBounds());
        }
    }

    public boolean onShape(int x, int y) {
        Shape transformShape = this.affineTransform.createTransformedShape(this.shape);
        if(this.getSelected()){
            if (this.gAnchors.onShape(x, y)) {
                return true;
            }
        }
        if(transformShape.contains(x, y)) {
            this.gAnchors.setSelectedAnchor(GConstants.EAnchors.MM);
            return true;
        }
        return false;
    }

    public abstract void setShape(int x1, int y1, int x2, int y2);
    public abstract void resizePoint(int x, int y);
}