package number26.de.bitcoins.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by emanuele on 02.06.16.
 */
public class DataDownloaderCommand implements Runnable {

    private final String mUrl;
    private final CommandListener<String> mListener;

    public DataDownloaderCommand(String url, CommandListener<String> listener) {
        mUrl = url;
        mListener = listener;
    }

    @Override
    public void run() {
        HttpURLConnection urlConnection = null;
        try {

            urlConnection = (HttpURLConnection) new URL(mUrl).openConnection();
            urlConnection.setRequestProperty("Accept", "application/json");
            InputStream in = null;
            if (urlConnection.getResponseCode() == 200) {
                in = urlConnection.getInputStream();
                if (mListener != null) {
                    mListener.onCommandFinished(readStream(new InputStreamReader(in)));
                }
            } else {
                in = urlConnection.getErrorStream();
                if (mListener != null) {
                    mListener.onCommandFailed(readStream(new InputStreamReader(in)), null);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (mListener != null) {
                mListener.onCommandFailed(e.getMessage(), e);
            }
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    private String readStream(Reader in) throws IOException {
        char[] buffer = new char[8096];
        StringBuilder builder = new StringBuilder();
        int read = 0;
        while ((read = in.read(buffer)) > 0) {
            builder.append(buffer, 0, read);
        }
        in.close();
        return builder.toString();
    }
}
