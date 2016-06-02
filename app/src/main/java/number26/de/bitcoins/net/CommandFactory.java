package number26.de.bitcoins.net;

/**
 * Created by emanuele on 02.06.16.
 */
public class CommandFactory {

    public static DataDownloaderCommand fetchBitCoinsPriceTrend(final String url, CommandListener listener) {
        return new DataDownloaderCommand(url, listener);
    }

    private CommandFactory() {
    }
}
