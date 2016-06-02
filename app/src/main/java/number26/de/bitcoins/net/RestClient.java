package number26.de.bitcoins.net;


import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import number26.de.bitcoins.model.PriceTrend;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;
import rx.Observable;


/**
 * Created by emanuele on 02.06.16.
 */
public class RestClient {

    private interface BitCoinWebService {
        @Headers("Accept: application/json")
        @GET("charts/market-price")
        Observable<List<PriceTrend>> fetchBitCoinPrices(@QueryMap Map<String, String> query);
    }

    private final BitCoinWebService mWebService;

    private static RestClient sRestClient;

    private RestClient() {
        Type listType = new TypeToken<List<PriceTrend>>() {
        }.getType();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(listType, new PriceTrendDeserializer());
        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl("https://blockchain.info/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .build();
        mWebService = restAdapter.create(BitCoinWebService.class);
    }

    public static synchronized RestClient getInstance() {
        if (sRestClient == null) {
            sRestClient = new RestClient();
        }
        return sRestClient;
    }

    public Observable<List<PriceTrend>> fetchBitCoinsPriceTrend(@QueryMap Map<String, String> query) {
        return mWebService.fetchBitCoinPrices(query);
    }
}