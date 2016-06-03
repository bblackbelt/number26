package number26.de.bitcoins.net;

import java.util.Map;

/**
 * Created by emanuele on 02.06.16.
 */
public class CommandFactory {

    public static DataDownloaderCommand fetchBitCoinsPriceTrend(final Map<String, String> params, CommandListener listener) {
        return new DataDownloaderCommand("charts/market-price", params, listener);
    }

    private CommandFactory() {
    }
}
