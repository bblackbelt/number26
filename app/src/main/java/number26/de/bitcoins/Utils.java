package number26.de.bitcoins;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by emanuele on 31.05.16.
 */
public class Utils {

    public static float pxToDp(final Context context, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

    public static float spToDp(final Context context, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, context.getResources().getDisplayMetrics());
    }

    private Utils() {
    }
}
