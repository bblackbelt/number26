package number26.de.bitcoins;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import number26.de.bitcoins.model.TimeSpan;

public class MainActivity extends AppCompatActivity {

    private final DataController mDataController = new DataController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDataController.addDataListener((DataController.DataListener) findViewById(R.id.graph));

        ArrayAdapter<TimeSpan> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getTimeSpans());
        Spinner spinner = (Spinner) findViewById(R.id.spinner_nav);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mDataController.updateData((TimeSpan) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mDataController.updateData(null);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
       mDataController.onDestroy();
    }
}