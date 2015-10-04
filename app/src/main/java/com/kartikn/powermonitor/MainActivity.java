package com.kartikn.powermonitor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class MainActivity extends Activity {



    private GraphView graph2;
    private LineGraphSeries<DataPoint> series_level_reading;
    private boolean runOnce = false;
    private int scale, temperature, voltage, avg_current,inst_current, current_level, starting_level, plugged, status;
    private long starting_time, elapsed_time=0,total_elapsed_time=0;
    private TextView text1, text2, text3, text4, text5, text6, text7, text8, text9;
    private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (runOnce == false) {
                starting_level =  intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                starting_time = System.currentTimeMillis();
                scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
                series_level_reading.appendData(new DataPoint(starting_level,0),false,100);
                runOnce = true;
            }
            temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
            voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
            plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
            status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
            BatteryManager bm = (BatteryManager) getSystemService(Context.BATTERY_SERVICE);
            inst_current = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW);
            avg_current = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE);
            current_level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            text1.setText(""+scale+"%");
            text2.setText("" + current_level + "%");
            text4.setText(""+avg_current/1000+"mA");
            text5.setText("" + inst_current / 1000 + "mA");
            text6.setText("" + (float) voltage / 1000 + "mV");
            text8.setText("" + (float) temperature / 10 + "C");
            String plugged_text="";
            switch(plugged) {
                case 0:             plugged_text="Battery";
                    break;
                case 1:             plugged_text="on AC";
                    break;
                case 2:             plugged_text="on USB";
                    break;
                case 4:             plugged_text="Wireless";
                    break;
            }
            text9.setText(plugged_text);
            switch(status) {
                case 1:             text7.setText("Unknown");
                    break;
                case 2:             text7.setText("Charging");
                    break;
                case 3:            text7.setText("Discharging");
                    break;
                case 4:             text7.setText("Not Charging");
                    break;
                case 5:             text7.setText("Fully Charged");
                    break;
            }

            if (current_level <scale) {
                text3.setText(""+(System.currentTimeMillis()-starting_time)/1000+"secs");}
            if ((current_level-starting_level)>=1 && current_level < scale) {
                series_level_reading.appendData(new DataPoint(current_level, (System.currentTimeMillis()- starting_time)/1000),false,100);
                starting_level = current_level;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new SimpleEula(this).show();
        text1 = (TextView)findViewById(R.id.text_1);
        text2 = (TextView)findViewById(R.id.text_2);
        text3 = (TextView)findViewById(R.id.text_3);
        text4 = (TextView)findViewById(R.id.text_4);
        text5 = (TextView)findViewById(R.id.text_5);
        text6 = (TextView)findViewById(R.id.text_6);
        text8 = (TextView)findViewById(R.id.text_8);
        text7 = (TextView)findViewById(R.id.text_7);
        text9 = (TextView)findViewById(R.id.text_9);
        this.registerReceiver(this.batteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        graph2 = (GraphView) findViewById(R.id.graph2);
        graph2.setTitle("Charging Curve");
        graph2.setTitleColor(Color.RED);
        GridLabelRenderer gridLabelRenderer = new GridLabelRenderer(graph2);
        gridLabelRenderer.setHorizontalAxisTitle("Charge");
        gridLabelRenderer.setHorizontalAxisTitleColor(Color.RED);
        gridLabelRenderer.setVerticalAxisTitle("Time(secs)");
        gridLabelRenderer.setVerticalAxisTitleColor(Color.RED);
        series_level_reading = new LineGraphSeries<>();
        series_level_reading.setColor(Color.RED);
        graph2.addSeries(series_level_reading);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("About Power Monitor")
                    .setMessage("This app gives a view into the power usage of your android device. One unique feature is the graph of charging done versus time taken \n \n " +
                            "You can find more apps from me at https://play.google.com/store/apps/developer?id=Kartik+Narayanan \n \n" +
                        "Please mail me your feedback at kartik.narayanan@gmail.com");
            builder.create().show();

        }

        return super.onOptionsItemSelected(item);
    }
}

