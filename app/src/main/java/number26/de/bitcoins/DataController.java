package number26.de.bitcoins;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import number26.de.bitcoins.model.PriceTrend;
import number26.de.bitcoins.model.TimeSpan;
import number26.de.bitcoins.net.CommandExecutor;
import number26.de.bitcoins.net.CommandFactory;
import number26.de.bitcoins.net.CommandListener;
import number26.de.bitcoins.net.PriceTrendDeserializer;
import number26.de.bitcoins.net.RestClient;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by emanuele on 02.06.16.
 */
public class DataController {

    public interface DataListener {
        void onDataSetChanged(List<PriceTrend> priceTrend);
    }

    private Subscription mSubscription = null;
    private final List<DataListener> listeners;

    public DataController() {
        listeners = new ArrayList<>();
    }

    public void onDestroy() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        listeners.clear();
    }

    public void addDataListener(DataListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public boolean remoteDataListener(DataListener listener) {
        return listeners.remove(listener);
    }

    public void updateData(TimeSpan timeSpan) {
        Map<String, String> query = new HashMap<>();
        if (timeSpan != null && !TextUtils.isEmpty(timeSpan.getValue())) {
            query.put("timespan", timeSpan.getValue());
        }
        query.put("format", "json");
        updateDataExecutor(query);
    }

    private void updateDataExecutor(final Map<String, String> query) {
        CommandExecutor.getInstance().addCommand(CommandFactory.fetchBitCoinsPriceTrend(query, new CommandListener<String>() {
            @Override
            public void onCommandFinished(final String result) {
                Type listType = new TypeToken<List<PriceTrend>>() {}.getType();
                Gson gson = new GsonBuilder().registerTypeAdapter(listType, new PriceTrendDeserializer()).create();
                notifyDataSetChanged((List<PriceTrend>) gson.fromJson(result, listType));
            }

            @Override
            public void onCommandFailed(String message, Throwable throwable) {
            }
        }));
    }

    private void updateDataRx(final Map<String, String> query) {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        mSubscription = RestClient.getInstance().fetchBitCoinsPriceTrend(query)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<PriceTrend>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<PriceTrend> priceTrend) {
                        notifyDataSetChanged(priceTrend);
                    }
                });
    }

    private void notifyDataSetChanged(List<PriceTrend> priceTrend) {
        for (DataListener listener : listeners) {
            listener.onDataSetChanged(Collections.unmodifiableList(priceTrend));
        }
    }
}
