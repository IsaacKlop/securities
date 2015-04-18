package com.example.securities.ssltool;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.*;
import java.net.*;
import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends ActionBarActivity {

    TextView t;
    URL url;
    String HTTPS_URL = ("https://www.google.com");
    String HTTP_URL  = ("google.com");
    String outputCipher;
    String outputHostname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToHostname (View view) {
        new backOperation().execute();
        t = new TextView(this);
        t = (TextView) findViewById(R.id.textView2);
        t.setText("Cipher Suite : " + outputCipher + "\n" +
                  "Hostname : " + outputHostname);
    }

    private class backOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL(HTTPS_URL);
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

                //Grabs Cipher Suite from host
                InputStream in = con.getInputStream();
                outputCipher = con.getCipherSuite();

                //Grabs hostname of HTTP host
                InetAddress Address = InetAddress.getByName(HTTP_URL);
                outputHostname = Address.getHostName();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return("");
        }
    }

    public void goToDate (View view){
        t = new TextView(this);
        t = (TextView)findViewById(R.id.textView2);
        t.setText("Checking Date...");
    }

    public void goToCA (View view){
        t = new TextView(this);
        t = (TextView)findViewById(R.id.textView2);
        t.setText("Checking CA...");
    }

}