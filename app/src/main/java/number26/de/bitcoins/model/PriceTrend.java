package number26.de.bitcoins.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by emanuele on 02.06.16.
 */
public class PriceTrend implements Point {

    @SerializedName("y")
    private float mPrice;
    @SerializedName("x")
    private long mTime;

    public PriceTrend(float price, long time) {
        mPrice = price;
        mTime = time;
    }

    public long getTime() {
        return mTime;
    }

    public float getPrice() {
        return mPrice;
    }

    @Override
    public float getX() {
        return mTime;
    }

    @Override
    public float getY() {
        return mPrice;
    }
}
