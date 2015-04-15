package com.example.securities.ssltool;

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
        t = new TextView(this);
        t = (TextView) findViewById(R.id.textView2);
        t.setText("Checking Hostname...");

        try {
            URL url = new URL("https://www.google.com");
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            //readStream(con.getInputStream());
            } catch (Exception e) {
             e.printStackTrace();
        }
    }


  /*  private void readStream(InputStream in) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }*/


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
