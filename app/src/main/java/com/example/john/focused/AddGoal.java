package com.example.john.focused;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.HttpResponseException;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

import static com.example.john.focused.R.id.focusEdit;

public class AddGoal extends AppCompatActivity implements View.OnClickListener {

    EditText focusText, overviewText, whyText, howText;
    Button postBtn;
    goal goal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);

        focusText = (EditText) findViewById(focusEdit);
        overviewText = (EditText) findViewById(R.id.overviewEdit);
        whyText=(EditText) findViewById(R.id.whyEdit);
        howText =(EditText) findViewById(R.id.howEdit);
        postBtn = (Button) findViewById(R.id.postBtn);

        postBtn.setOnClickListener(this);
    }

    public static String POST(String url, goal goals){
        InputStream inputStream = null;
        String result = "";
        try{
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json="";

            JSONObject jsonObject = new JSONObject();

            jsonObject.accumulate("focus", goals.getFocus());
            jsonObject.accumulate("overview", goals.getOverview());
            jsonObject.accumulate("how", goals.getHow());
            jsonObject.accumulate("why", goals.getWhy());

            json = jsonObject.toString();

            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpClient.execute(httpPost);
            inputStream= httpResponse.getEntity().getContent();
            if(inputStream != null)
                result=convertInputStreamToString(inputStream);
            else
                result="Did not work";
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return result;
    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.postBtn:
                if(!validate())
                    Toast.makeText(getBaseContext(), "Enter some data", Toast.LENGTH_LONG).show();
                new HttpAsyncTask().execute("http://54.70.51.182:3000/api/goals/");
            break;
        }
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls){
            goal = new goal();
            goal.setFocus(focusText.getText().toString());
            goal.setOverview(overviewText.getText().toString());
            goal.setWhy(howText.getText().toString());
            goal.setHow(whyText.getText().toString());

            return POST(urls[0],goal);
        }

        @Override
        protected void onPostExecute(String result){
            Toast.makeText(getBaseContext(), "Data sent!", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validate(){
        if(focusText.getText().toString().trim().equals(""))
            return false;
        else if(overviewText.getText().toString().trim().equals(""))
            return false;
        else if(howText.getText().toString().trim().equals(""))
            return false;
        else
            return true;
    }
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        if(id==R.id.add_focus){
        }
        if(id==R.id.history){
            Intent intent = new Intent(AddGoal.this, settings.class);
            startActivity(intent);
        }
        return true;
    }
}
