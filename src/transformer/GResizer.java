package transformer;

import main.GConstants;
import shapes.GShape;

import java.awt.*;
import java.awt.geom.Point2D;

public class GResizer extends GTransformer {
    protected double cx, cy;
    protected double sx, sy;

    public GResizer(GShape currentShape) {
        super(currentShape);
    }

    @Override
    public void initTransform(int x, int y) {
        this.px = x;
        this.py = y;
        Point2D resizeAnchorPoint = this.anchors.getResizeAnchorPoint(x, y);
        this.cx = resizeAnchorPoint.getX();
        this.cy = resizeAnchorPoint.getY();
    }

    @Override
    public void keepTransform(int x, int y) {
        this.getResizeScale(x, y);
        this.affineTransform.translate(this.cx, this.cy);
        this.affineTransform.scale(this.sx, this.sy);
        this.affineTransform.translate(-this.cx, -this.cy);
        this.px = x;
        this.py = y;
    }

    private void getResizeScale(int x, int y) {
        GConstants.EAnchors resizeAnchor = this.anchors.getResizeAnchor();
        double w1 = px - cx;
        double w2 = x - cx;
        double h1 = py - cy;
        double h2 = y - cy;

        switch (resizeAnchor) {
            case NW: sx = w2/w1; sy = h2/h1; break;
            case WW: sx = w2/w1; sy = 1; break;
            case SW: sx = w2/w1; sy = h2/h1; break;
            case SS: sx = 1; sy = h2/h1; break;
            case SE: sx = w2/w1; sy = h2/h1; break;
            case EE: sx = w2/w1; sy = 1; break;
            case NE: sx = w2/w1; sy = h2/h1; break;
            case NN: sx = 1; sy = h2/h1; break;
            default:
                break;
        }
    }
}
