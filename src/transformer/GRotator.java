package transformer;

import shapes.GShape;

import java.awt.*;

public class GRotator extends GTransformer {
    public GRotator(GShape currentShape) {
        super(currentShape);
    }

    @Override
    public void initTransform(int x, int y) {
        this.px = x;
        this.py = y;
    }

    @Override
    public void keepTransform(int x, int y) {
        if (this.px == x && this.py == y) return;

        double xx = this.shape.getShape().getBounds2D().getCenterX();
        double yy = this.shape.getShape().getBounds2D().getCenterY();
        Point prePoint = new Point(this.px, this.py);
        Point center = new Point((int) xx, (int) yy);
        Point newPoint = new Point(x, y);

        double radian = Math.toRadians(getAngleFromThreePoints(prePoint, center, newPoint));

        this.affineTransform.translate(xx, yy);
        this.affineTransform.rotate(radian);
        this.affineTransform.translate(-xx, -yy);
        this.px = x;
        this.py = y;
    }

    private double getAngleFromThreePoints(Point startP, Point centerP, Point endP) {
        double startAngle = Math.toDegrees(Math.atan2(startP.getY() - centerP.getY(), startP.getX() - centerP.getX()));
        double endAngle = Math.toDegrees(Math.atan2(endP.getY() - centerP.getY(), endP.getX() - centerP.getX()));
        double angle = endAngle - startAngle;

        if (angle < 0) {
            angle += 360;
        }

        return angle;
    }
}
