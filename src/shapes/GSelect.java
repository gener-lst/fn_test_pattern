package shapes;

public class GSelect extends GRectangle{
    @Override
    public GShape createShape() {
        return new GSelect();
    }
}
