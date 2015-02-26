package com.austin.cs476;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Spinner;


public class MainActivity extends Activity implements OnClickListener{

    private Boolean boolStart = false;
    private Chronometer chronometer;
    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    private int startingBatteryVal;
    private int endingBatteryVal;
    private int inputVal;

/*
This function is run when the application has been started, it initializes all values
 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        Spinner custom = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> customAdapter = ArrayAdapter.createFromResource(
        this, R.array.battery_options, R.layout.spinner_layout);
        customAdapter.setDropDownViewResource(R.layout.spinner_layout);
        custom.setAdapter(customAdapter);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        findViewById(R.id.button).setOnClickListener(this);
        startingBatteryVal = getBatteryLevel();
    }
/*
This function is called when the 'Start' button has been clicked
 */
    @Override
    public void onClick(View v) {
        chronometer.setBase(SystemClock.elapsedRealtime());
        if(!boolStart) {
            chronometer.start();
            boolStart = true;
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            winParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_OFF;
            win.setAttributes(winParams);
            ((Button)findViewById(R.id.button)).setText("STOP");
            Spinner spinner = (Spinner) findViewById(R.id.spinner);
            String text = spinner.getSelectedItem().toString();
            inputVal = Integer.parseInt(text);
        }
        else {
            chronometer.stop();
            boolStart = false;
            ((Button)findViewById(R.id.button)).setText("START");
        }
    }
/*
Reinitializes the app without a need to close and re-open it
 */
    public void refresh(View v) {
        Intent newIntent = getIntent();
        finish();
        startActivity(newIntent);
    }
/*
The function used to get the battery level when the function is called (For current and initial battery)
 */
    public int getBatteryLevel() {
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        return (int)(((float)level / (float)scale) * 100.0f);
    }
/*
This function is used to access the Android Sensors to tell if the device has been shaken.
 */
    private final SensorEventListener mSensorListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if ((mAccel > 10)&&boolStart) {
                endingBatteryVal = getBatteryLevel();
                if(endingBatteryVal > (startingBatteryVal - inputVal)) {
                    Window win = getWindow();
                    WindowManager.LayoutParams winParams = win.getAttributes();
                    winParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL;
                    win.setAttributes(winParams);
                }
            }
        }
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
}
