package number26.de.bitcoins.net;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by emanuele on 02.06.16.
 */
public class CommandExecutor {


    private static CommandExecutor mCommandExecutor;

    private final ExecutorService mExecutor;

    private CommandExecutor() {
        mExecutor = Executors.newFixedThreadPool(1);
    }

    public static synchronized CommandExecutor getInstance() {
        if (mCommandExecutor == null) {
            mCommandExecutor = new CommandExecutor();
        }
        return mCommandExecutor;
    }

    public void addCommand(DataDownloaderCommand command) {
        mExecutor.execute(command);
    }
}