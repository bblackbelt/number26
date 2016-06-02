package number26.de.bitcoins.model;


public class XYPoint implements Point {

    private final float mXValue;
    private final float mYValue;

    public XYPoint(float x, float y) {
        mXValue = x;
        mYValue = y;
    }


    @Override
    public float getX() {
        return mXValue;
    }

    @Override
    public float getY() {
        return mYValue;
    }
}
