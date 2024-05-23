package transformer;

import shapes.GShape;

import java.awt.*;

public class GMover extends GTransformer{
    public GMover(GShape gShape) {
        super(gShape);
    }

    @Override
    public void initTransform(int x, int y) {
        px = x;
        py = y;
    }

    @Override
    public void keepTransform(int x, int y) {
        this.affineTransform.setToTranslation(x - px, y - py);
        Shape transformedShape = this.affineTransform.createTransformedShape(this.shape.getShape());
        this.shape.setShape(transformedShape);
        px = x;
        py = y;
    }
}
