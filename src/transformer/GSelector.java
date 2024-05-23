package transformer;

import shapes.GSelect;
import shapes.GShape;

import java.awt.*;
import java.util.Vector;

public class GSelector extends GTransformer {
    private GShape selectedShape;
    public GSelector(GShape gShape) {
        super(gShape);
    }

    @Override
    public void initTransform(int x, int y) {
        this.shape.setShape(x, y, x, y);
    }

    @Override
    public void keepTransform(int x, int y) {
        this.shape.resizePoint(x, y);
    }

    @Override
    public void finalizeTransform(Graphics2D graphics2D, Vector<GShape> shapes) {
        int minX = (int)this.shape.getShape().getBounds2D().getMinX();
        int minY = (int)this.shape.getShape().getBounds2D().getMinY();
        int maxX = (int)this.shape.getShape().getBounds2D().getMaxX();
        int maxY = (int)this.shape.getShape().getBounds2D().getMaxY();

        for(int y = maxY; y >= minY; y--){
            for(int x = maxX; x >= minX; x--){
                for (GShape gShape : shapes) {
                    if(gShape.onShape(x, y)) {
                        this.selectedShape = gShape;
                    }
                }
                if(this.selectedShape != null) {
                    this.selectedShape.setSelected(true);
                }
            }
        }
    }
}
