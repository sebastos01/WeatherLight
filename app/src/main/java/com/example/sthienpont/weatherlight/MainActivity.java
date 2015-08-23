
package com.example.sthienpont.weatherlight;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sthienpont.weatherlight.dataobjects.Preferences;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    /**
     * Keyword
     */
    private static final String USER_DATA = "userData";

    private TextView tvTemperature;
    private Button bWeather;
    private EditText etCity;

    private Button bOne;
    private Button bTwo;
    private Button bThree;
    private Button bFour;

    private Preferences preferences;

    private View.OnClickListener onAddListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            String city = etCity.getText().toString().replace(" ", "");
            city = city.isEmpty() ? "" : Character.toUpperCase(city.charAt(0)) + city.substring(1);
            if (evaluateCity(city)) {
                getWeather(city);
                saveCityToPreferencesAndReloadScreen(city);
            }
        }
    };

    private boolean evaluateCity(String city) {
        if (city.isEmpty()) {
            Toast.makeText(this, getString(R.string.add_error_empty), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (preferences.cities.contains(city)) {
            Toast.makeText(this, getString(R.string.add_error_duplicate, city), Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        if (preferences.cities.size() >= 4) {
            Toast.makeText(this, getString(R.string.add_error_full), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private View.OnClickListener bOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            switchButtonColor(button);
            String city = button.getText().toString();
            getWeather(city);
            preferences.currentCity = city;
            savePreferences();
        }
    };

    private View.OnLongClickListener bOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            final Button button = (Button) v;
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(getString(R.string.remove_city, button.getText().toString()))
                    .setPositiveButton(R.string.remove, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    preferences.cities.remove(button.getText().toString());
                    if (!preferences.cities.contains(preferences.currentCity)) {
                        if (preferences.cities.isEmpty()) {
                            preferences.currentCity = "";
                        } else {
                            preferences.currentCity = preferences.cities.get(0);
                        }
                    }
                    savePreferences();
                    updateButtons();
                }
            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // close dialog
                }
            });
            builder.create().show();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTemperature = (TextView) findViewById(R.id.tvTemperature);
        bWeather = (Button) findViewById(R.id.bWeather);
        etCity = (EditText) findViewById(R.id.etCity);
        bOne = (Button) findViewById(R.id.bOne);
        bTwo = (Button) findViewById(R.id.bTwo);
        bThree = (Button) findViewById(R.id.bThree);
        bFour = (Button) findViewById(R.id.bFour);

        bWeather.setOnClickListener(onAddListener);

        bOne.setOnClickListener(bOnClickListener);
        bTwo.setOnClickListener(bOnClickListener);
        bThree.setOnClickListener(bOnClickListener);
        bFour.setOnClickListener(bOnClickListener);

        bOne.setOnLongClickListener(bOnLongClickListener);
        bTwo.setOnLongClickListener(bOnLongClickListener);
        bThree.setOnLongClickListener(bOnLongClickListener);
        bFour.setOnLongClickListener(bOnLongClickListener);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

    private void saveCityToPreferencesAndReloadScreen(String city) {
        preferences.cities.add(city);
        preferences.currentCity = city;
        savePreferences();
        updateButtons();
    }

    private void updateButtons() {
        loadPreferences();
        hideAllButtons();
        setCityButtonsFromPreferences();
        switchCurrentCityButtonColor();
        if (!preferences.currentCity.isEmpty()) {
            getWeather(preferences.currentCity);
        } else {
            tvTemperature.setText(getString(R.string.temperature));
        }
    }

    private void switchCurrentCityButtonColor() {
        String city = preferences.currentCity;
        if (city.equalsIgnoreCase(bOne.getText().toString())) {
            switchButtonColor(bOne);
        }
        if (city.equalsIgnoreCase(bTwo.getText().toString())) {
            switchButtonColor(bTwo);
        }
        if (city.equalsIgnoreCase(bThree.getText().toString())) {
            switchButtonColor(bThree);
        }
        if (city.equalsIgnoreCase(bFour.getText().toString())) {
            switchButtonColor(bFour);
        }
    }

    private void setCityButtonsFromPreferences() {
        for (String s : preferences.cities) {
            if (bOne.getVisibility() != View.VISIBLE) {
                bOne.setText(s);
                bOne.setVisibility(View.VISIBLE);
            } else if (bTwo.getVisibility() != View.VISIBLE) {
                bTwo.setText(s);
                bTwo.setVisibility(View.VISIBLE);
            } else if (bThree.getVisibility() != View.VISIBLE) {
                bThree.setText(s);
                bThree.setVisibility(View.VISIBLE);
            } else if (bFour.getVisibility() != View.VISIBLE) {
                bFour.setText(s);
                bFour.setVisibility(View.VISIBLE);
            }
        }
    }

    private void hideAllButtons() {
        bOne.setVisibility(View.INVISIBLE);
        bOne.setText("");
        bTwo.setVisibility(View.INVISIBLE);
        bTwo.setText("");
        bThree.setVisibility(View.INVISIBLE);
        bThree.setText("");
        bFour.setVisibility(View.INVISIBLE);
        bFour.setText("");
    }

    private void switchButtonColor(Button button) {

        if (button.getText() == bOne.getText()) {
            bOne.setBackground(ContextCompat.getDrawable(this, R.drawable.roundedbutton_clicked));
        } else {
            bOne.setBackground(ContextCompat.getDrawable(this, R.drawable.roundedbutton));
        }
        if (button.getText() == bTwo.getText()) {
            bTwo.setBackground(ContextCompat.getDrawable(this, R.drawable.roundedbutton_clicked));
        } else {
            bTwo.setBackground(ContextCompat.getDrawable(this, R.drawable.roundedbutton));
        }
        if (button.getText() == bThree.getText()) {
            bThree.setBackground(ContextCompat.getDrawable(this, R.drawable.roundedbutton_clicked));
        } else {
            bThree.setBackground(ContextCompat.getDrawable(this, R.drawable.roundedbutton));
        }
        if (button.getText() == bFour.getText()) {
            bFour.setBackground(ContextCompat.getDrawable(this, R.drawable.roundedbutton_clicked));
        } else {
            bFour.setBackground(ContextCompat.getDrawable(this, R.drawable.roundedbutton));
        }
    }

    private void getWeather(String city) {
        new CallWeatherAPI(tvTemperature, this).execute(getString(R.string.api_url), city);
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String userData = gson.toJson(preferences);
        Log.d(TAG, "saving preferences: " + userData);
        editor.putString(USER_DATA, userData);
        editor.apply();
    }

    private void loadPreferences() {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        String userData = prefs.getString(USER_DATA, null);
        Log.d(TAG, "Found preferences = " + userData);
        preferences = (new Gson()).fromJson(userData, Preferences.class);
        if (preferences == null) {
            preferences = new Preferences();
        }
    }
}
