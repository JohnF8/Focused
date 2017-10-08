package com.example.john.focused;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    TextView focus;
    TextView overview;
    TextView why;
    TextView how;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Elements init
        focus = (TextView) findViewById(R.id.mainFocus);
        overview = (TextView) findViewById(R.id.mainOverview);
        why = (TextView) findViewById(R.id.mainWhy);
        how = (TextView) findViewById(R.id.mainHow);

        new JSONTask().execute("http://54.70.51.182:3000/api/goals/");

        if(focus.getText().equals(null) && overview.getText().equals(null)){
            focus.setVisibility(TextView.INVISIBLE);
            overview.setVisibility(TextView.INVISIBLE);
            why.setVisibility(TextView.INVISIBLE);
            how.setVisibility(TextView.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemClicked = item.getItemId();
        if (itemClicked == R.id.add_focus) {
            Intent intent = new Intent(MainActivity.this, AddGoal.class);
            startActivity(intent);
        }
        if(itemClicked == R.id.history){
            Intent intent = new Intent(MainActivity.this, settings.class);
            startActivity(intent);
        }
        return true;
    }

    public class JSONTask extends AsyncTask<String, String, ArrayList<String> > {

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String receivedJSON = buffer.toString();
                JSONObject parentObj = new JSONObject(receivedJSON);
                JSONArray parentArray = parentObj.getJSONArray("goals");
                ArrayList<String> finalData = new ArrayList<String>();

                JSONObject finalObject = parentArray.getJSONObject(0);

                finalData.add(finalObject.getString("focus"));
                finalData.add(finalObject.getString("why"));
                finalData.add(finalObject.getString("how"));
                finalData.add(finalObject.getString("overview"));

                return finalData;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> finalData) {
           focus.setText(finalData.get(0));
            overview.setText(finalData.get(1));
            how.setText(finalData.get(2));
            why.setText(finalData.get(3));

        }
    }
}

