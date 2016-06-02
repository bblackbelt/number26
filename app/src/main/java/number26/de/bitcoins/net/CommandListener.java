package number26.de.bitcoins.net;

/**
 * Created by emanuele on 02.06.16.
 */
public interface CommandListener<T> {
    void onCommandFinished(T result);

    void onCommandFailed(String message, Throwable throwable);
}
