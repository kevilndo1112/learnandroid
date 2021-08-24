package kevilnkaito.Weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText txtCityName;
    TextView lblResult;
    ImageView imageWeatherIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtCityName = findViewById(R.id.txtCityName);
        lblResult = findViewById(R.id.lblResult);
        imageWeatherIcon = findViewById(R.id.imageWeatherIcon);
    }

    public void onBtnGetWeatherClick(View view) {

        hideKeyboard();

        lblResult.setVisibility(View.VISIBLE);
        lblResult.setText("Loading...");

        if (txtCityName.getText().toString().isEmpty()) {
            notificationPleaseEnterCityName();
            return;
        }

        try {

            String encodedCityName = URLEncoder.encode(txtCityName.getText().toString(), "UTF-8");
            String urlRequest = "https://api.openweathermap.org/data/2.5/weather?q=" + encodedCityName + "&appid=3790f712337cbfece38c0ae41594ad7c";

            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(urlRequest);

            //String jsonResult = downloadTask.get();
            //jsonProcessingAndSetResult(jsonResult);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {

                String result = "";

                for (String string : strings) {

                    URL url = new URL(string);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                    int data = inputStreamReader.read();

                    while (data != -1) {
                        char current = (char) data;
                        result += current;
                        data = inputStreamReader.read();
                    }

                }

                return result;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            jsonProcessingAndSetResult(s);
        }
    }

    private void jsonProcessingAndSetResult(String jsonString) {

        if (jsonString == null) {
            notificationCouldNotFindWeather();
            return;
        }

        try {

            JSONObject jsonObject = new JSONObject(jsonString);

            String weatherString = jsonObject.getString("weather");
            JSONArray weatherArray = new JSONArray(weatherString);

            String message = "";
            String icon = "";

            for (int i = 0; i < weatherArray.length(); i++) {
                JSONObject weatherItem = weatherArray.getJSONObject(i);

                String main = weatherItem.getString("main");
                String description = weatherItem.getString("description");
                if (i == 0) {
                    icon = weatherItem.getString("icon");
                }

                message += main + ": " + description + "\r\n";

            }

            JSONObject mainObject = jsonObject.getJSONObject("main");
            double temp = mainObject.getDouble("temp");
            temp = temp - 273.15; //Kelvin to Celsius conversion
            temp = Common.round(temp, 2);

            message += "Temp : " + temp + " °C" + "\r\n";

            if (!message.isEmpty()) {

                lblResult.setText(message);

                imageWeatherIcon.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load("http://openweathermap.org/img/wn/" + icon + "@2x.png")
                        .into(imageWeatherIcon);

            } else {
                notificationCouldNotFindWeather();
            }

        } catch (JSONException e) {
            notificationCouldNotFindWeather();
            e.printStackTrace();
        }
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(txtCityName.getWindowToken(), 0);
    }

    private void notificationCouldNotFindWeather() {
        lblResult.setText("");
        Toast.makeText(getApplicationContext(), "Could not find weather. 😯", Toast.LENGTH_SHORT).show();

        imageWeatherIcon.setVisibility(View.INVISIBLE);
    }

    private void notificationPleaseEnterCityName() {
        lblResult.setText("");
        Toast.makeText(getApplicationContext(), "Please enter the city name. 🤔", Toast.LENGTH_SHORT).show();

        imageWeatherIcon.setVisibility(View.INVISIBLE);
    }
}