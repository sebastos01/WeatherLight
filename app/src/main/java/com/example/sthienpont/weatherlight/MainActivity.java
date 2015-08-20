
package com.example.sthienpont.weatherlight;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView tvTemperature;
    private Button bWeather;
    private EditText etCity;
    private Button bMadrid;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getWeather(etCity.getText().toString());
            InputMethodManager inputManager = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };

    private View.OnClickListener madridClicklistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getWeather(bMadrid.getText().toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTemperature = (TextView) findViewById(R.id.tvTemperature);
        bWeather = (Button) findViewById(R.id.bWeather);
        etCity = (EditText) findViewById(R.id.etCity);
        bMadrid = (Button) findViewById(R.id.bMadrid);

        bWeather.setOnClickListener(onClickListener);
        bMadrid.setOnClickListener(madridClicklistener);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bMadrid.callOnClick();
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

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, getResources().getString(R.string.creator), Toast.LENGTH_SHORT)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getWeather(String city) {
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city;
        new CallWeatherAPI(tvTemperature, this).execute(url);

    }
}
