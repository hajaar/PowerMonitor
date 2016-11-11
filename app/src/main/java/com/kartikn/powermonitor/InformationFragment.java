package com.kartikn.powermonitor;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


/**
 * A simple {@link Fragment} subclass.
 */
public class InformationFragment extends Fragment {

    private GraphView graph2;
    private LineGraphSeries<DataPoint> series_level_reading;
    private boolean runOnce = false;
    private int scale, temperature, voltage, avg_current, inst_current, current_level, starting_level, plugged, status;
    private long starting_time, elapsed_time = 0, total_elapsed_time = 0;
    private TextView text1, text2, text3, text4, text5, text6, text7, text8, text9;
    private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (runOnce == false) {
                starting_level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                starting_time = System.currentTimeMillis();
                scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
                series_level_reading.appendData(new DataPoint(starting_level, 0), false, 100);
                runOnce = true;
            }
            temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
            voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
            plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
            status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
            BatteryManager bm = (BatteryManager) getActivity().getSystemService(Context.BATTERY_SERVICE);
            inst_current = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW);
            avg_current = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE);
            current_level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            text1.setText("" + scale + "%");
            text2.setText("" + current_level + "%");
            text4.setText("" + avg_current / 1000 + "mA");
            text5.setText("" + inst_current / 1000 + "mA");
            text6.setText("" + (float) voltage / 1000 + "mV");
            text8.setText("" + (float) temperature / 10 + "C");
            String plugged_text = "";
            switch (plugged) {
                case 0:
                    text9.setText(R.string.battery);
                    break;
                case 1:
                    text9.setText(R.string.AC);
                    break;
                case 2:
                    text9.setText(R.string.USB);
                    break;
                case 4:
                    text9.setText(R.string.wireless);
                    break;
            }
            switch (status) {
                case 1:
                    text7.setText(R.string.unknown);
                    break;
                case 2:
                    text7.setText(R.string.charging);
                    break;
                case 3:
                    text7.setText(R.string.discharging);
                    break;
                case 4:
                    text7.setText(R.string.notcharging);
                    break;
                case 5:
                    text7.setText(R.string.fullycharged);
                    break;
            }
            Log.d("Current starting ", current_level + " : " + starting_level);
            if (current_level < scale) {
                text3.setText("" + (System.currentTimeMillis() - starting_time) / 1000);
            }
            if ((current_level - starting_level) >= 1 && current_level < scale) {
                series_level_reading.appendData(new DataPoint(current_level, (System.currentTimeMillis() - starting_time) / 1000), false, 100);
                Log.d("Current starting time", current_level + " : " + starting_level + " : " + (System.currentTimeMillis() - starting_time) / 1000);
                starting_level = current_level;
            }
        }
    };

    public InformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_information, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        text1 = (TextView) getActivity().findViewById(R.id.text_1);
        text2 = (TextView) getActivity().findViewById(R.id.text_2);
        text3 = (TextView) getActivity().findViewById(R.id.text_3);
        text4 = (TextView) getActivity().findViewById(R.id.text_4);
        text5 = (TextView) getActivity().findViewById(R.id.text_5);
        text6 = (TextView) getActivity().findViewById(R.id.text_6);
        text8 = (TextView) getActivity().findViewById(R.id.text_8);
        text7 = (TextView) getActivity().findViewById(R.id.text_7);
        text9 = (TextView) getActivity().findViewById(R.id.text_9);
        getActivity().registerReceiver(this.batteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        graph2 = (GraphView) getActivity().findViewById(R.id.graph2);
        graph2.setTitle(getString(R.string.graphtitle));
        graph2.setTitleColor(Color.RED);
        GridLabelRenderer gridLabelRenderer = new GridLabelRenderer(graph2);
        gridLabelRenderer.setHorizontalAxisTitle(getString(R.string.xaxis));
        gridLabelRenderer.setHorizontalAxisTitleColor(Color.RED);
        gridLabelRenderer.setVerticalAxisTitle(getString(R.string.yaxis));
        gridLabelRenderer.setVerticalAxisTitleColor(Color.RED);
        series_level_reading = new LineGraphSeries<>();
        series_level_reading.setColor(Color.RED);
        graph2.addSeries(series_level_reading);
        AdView mAdView;
        super.onActivityCreated(savedInstanceState);
        mAdView = (AdView) getActivity().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
    }


}
