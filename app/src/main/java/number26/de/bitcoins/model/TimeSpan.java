package number26.de.bitcoins.model;

/**
 * Created by emanuele on 02.06.16.
 */
public class TimeSpan {

    private final String mName;
    private final String mValue;

    public TimeSpan(String name, String value) {
        mName = name;
        mValue = value;
    }

    public String getName() {
        return mName;
    }

    public String getValue() {
        return mValue;
    }

    @Override
    public String toString() {
        return mName;
    }
}
