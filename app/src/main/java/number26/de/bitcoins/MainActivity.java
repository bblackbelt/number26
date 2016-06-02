package number26.de.bitcoins;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import number26.de.bitcoins.model.PriceTrend;
import number26.de.bitcoins.model.TimeSpan;
import number26.de.bitcoins.net.RestClient;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        ArrayAdapter<TimeSpan> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getTimeSpans());
        Spinner spinner = (Spinner) findViewById(R.id.spinner_nav);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fetchData((TimeSpan) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void fetchData(TimeSpan timeSpan) {
        Map<String, String> query = new HashMap<>();
        if (timeSpan != null && !TextUtils.isEmpty(timeSpan.getValue())) {
            query.put("timespan", timeSpan.getValue());
        }
        query.put("format", "json");
        RestClient.getInstance().fetchBitCoinsPriceTrend(query)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<PriceTrend>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(List<PriceTrend> priceTrend) {
                ((GraphView) findViewById(R.id.graph)).addPoints(priceTrend);
            }
        });
        ((GraphView) findViewById(R.id.graph)).clear();
    }

    private List<TimeSpan> getTimeSpans() {
        List<TimeSpan> timeSpan = new ArrayList<>();
        timeSpan.add(new TimeSpan("--", null));
        timeSpan.add(new TimeSpan(getString(R.string.days, 30), "30days"));
        timeSpan.add(new TimeSpan(getString(R.string.days, 60), "60days"));
        timeSpan.add(new TimeSpan(getString(R.string.days, 180), "180days"));
        timeSpan.add(new TimeSpan(getString(R.string.years, 1), "1year"));
        timeSpan.add(new TimeSpan(getString(R.string.years, 2), "2year"));
        timeSpan.add(new TimeSpan(getString(R.string.all), "all"));
        return timeSpan;
    }
}