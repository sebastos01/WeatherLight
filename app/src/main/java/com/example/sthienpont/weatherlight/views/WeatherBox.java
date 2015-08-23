
package com.example.sthienpont.weatherlight.views;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sthienpont.weatherlight.R;

/**
 * Created by Sébastien on 23/8/15.
 */
public class WeatherBox extends LinearLayout {

    private TextView tvCity;
    private TextView tvTemperature;
    private TextView tvClouds;
    private TextView tvForecast;

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (tvCity.getText().length() <= 0) {
                tvForecast.setVisibility(INVISIBLE);
            } else {
                tvForecast.setVisibility(VISIBLE);
            }
        }
    };

    public WeatherBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.weather_box, this);

        tvCity = (TextView) findViewById(R.id.tvCity);
        tvTemperature = (TextView) findViewById(R.id.tvTemperature);
        tvClouds = (TextView) findViewById(R.id.tvClouds);
        tvForecast = (TextView) findViewById(R.id.tvForecast);

        tvCity.addTextChangedListener(textWatcher);
    }

    public void setCity(String city) {
        tvCity.setText(city);
    }

    public void setTemperature(String temperature) {
        tvTemperature.setText(temperature);
    }

    public void setClouds(String clouds) {
        tvClouds.setText(clouds);
    }

    public void setNoWeatherInfo() {
        tvCity.setText("");
        tvTemperature.setText("N.A.");
        tvClouds.setText("");
    }
}
