package shapes;

import main.GConstants;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.Serializable;

public class GAnchors implements Serializable, Cloneable{
    static final int W = 10;
    static final int H = 10;

    private Ellipse2D[] anchors;
    private GConstants.EAnchors selectedAnchor;

    private GConstants.EAnchors resizeAnchor;

    public GConstants.EAnchors getSelectedAnchor() {
        return selectedAnchor;
    }

    public void setSelectedAnchor(GConstants.EAnchors selectedAnchor) {
        this.selectedAnchor = selectedAnchor;
    }

    public GConstants.EAnchors getResizeAnchor() {
        return resizeAnchor;
    }

    @Override
    public GAnchors clone() {
        try {
            GAnchors clone = (GAnchors)super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public GAnchors() {
        anchors = new Ellipse2D[GConstants.EAnchors.values().length - 1];
        for (int i = 0; i < anchors.length; i++) {
            anchors[i] = new Ellipse2D.Float(0, 0, W, H);
        }
    }

    public void setPosition(Rectangle r) {
        int x = r.x - W/2;
        int y = r.y - H/2;
        anchors[GConstants.EAnchors.NW.ordinal()].setFrame(x, y , W, H);
        anchors[GConstants.EAnchors.NN.ordinal()].setFrame(x + r.width/2, y, W, H);
        anchors[GConstants.EAnchors.NE.ordinal()].setFrame(x + r.width, y, W, H);
        anchors[GConstants.EAnchors.EE.ordinal()].setFrame(x + r.width, y + r.height/2, W, H);
        anchors[GConstants.EAnchors.SE.ordinal()].setFrame(x + r.width, y + r.height, W, H);
        anchors[GConstants.EAnchors.SS.ordinal()].setFrame(x + r.width/2, y + r.height, W, H);
        anchors[GConstants.EAnchors.SW.ordinal()].setFrame(x, y + r.height, W, H);
        anchors[GConstants.EAnchors.WW.ordinal()].setFrame(x, y + r.height/2, W, H);
        anchors[GConstants.EAnchors.RR.ordinal()].setFrame(x + r.width/2, y - 30, W, H);
    }

    public void draw(Graphics2D graphics2D, Rectangle rectangle) {
        setPosition(rectangle);
        for (Ellipse2D anchor : anchors) {
            graphics2D.setPaint(Color.WHITE);
            graphics2D.fill(anchor);
            graphics2D.setPaint(Color.BLACK);
            graphics2D.draw(anchor);
        }
    }

    public boolean onShape(int x, int y) {
        for (int i = 0; i < GConstants.EAnchors.values().length - 1; i++) {
            if (this.anchors[i].contains(x, y)) {
                this.selectedAnchor = GConstants.EAnchors.values()[i];
                return true;
            }
        }
        return false;
    }

    public Point2D getResizeAnchorPoint(int x, int y) {
        switch (this.selectedAnchor) {
            case NW: this.resizeAnchor = GConstants.EAnchors.SE; break;
            case WW: this.resizeAnchor = GConstants.EAnchors.EE; break;
            case SW: this.resizeAnchor = GConstants.EAnchors.NE; break;
            case SS: this.resizeAnchor = GConstants.EAnchors.NN; break;
            case SE: this.resizeAnchor = GConstants.EAnchors.NW; break;
            case EE: this.resizeAnchor = GConstants.EAnchors.WW; break;
            case NE: this.resizeAnchor = GConstants.EAnchors.SW; break;
            case NN: this.resizeAnchor = GConstants.EAnchors.SS; break;
            default: break;
        }

        return new Point2D.Double(
                this.anchors[resizeAnchor.ordinal()].getCenterX(),
                this.anchors[resizeAnchor.ordinal()].getCenterY()
        );
    }
}
