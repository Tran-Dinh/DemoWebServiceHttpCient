package com.dinh.savvycom.demowevservice;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView txtIsConnect;
    EditText edtRespond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        if (isConnection()) {
            txtIsConnect.setText("You are Connected");
            txtIsConnect.setBackgroundColor(0xFF00CC00);
        } else {
            txtIsConnect.setText("Not Connect");
        }


        new HttpAsyncTask().execute("http://hmkcode.com/android-internet-connection-using-http-get-httpclient/");
    }

    /**
     * declare textview, editText
     */

    public void init() {
        txtIsConnect = (TextView) findViewById(R.id.tvIsConnected);
        edtRespond = (EditText) findViewById(R.id.etResponse);
    }

    /**
     * Use HttpURLConnection to get url
     * @param url
     * @return
     */

    public String getRespond(String url) {
        InputStream is = null;

        String result = "";
        HttpURLConnection con = null;

        try {

            // creat object httpUrlConnection
            con = (HttpURLConnection) new URL(url).openConnection();

            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            is = con.getInputStream();
            result = convertInputStreamToString(is);


        } catch (IOException e) {
            e.printStackTrace();
            result = "Did not work";
        }finally {
            con.disconnect();
        }
        
        return result;
    }

    /**
     * convert data web to String
     * @param inputStream
     * @return
     * @throws IOException
     */

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        Reader reader = null;
        reader = new InputStreamReader(inputStream, "UTF-8");
        char[] buffer = new char[4028];
        reader.read(buffer);
        return new String(buffer);


    }


    /**
     * check connect internet
     * @return
     */
    public boolean isConnection() {

        ConnectivityManager connect = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connect.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else
            return false;
    }

    /**
     * asynctast show data of web into UI
     */

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return getRespond(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            edtRespond.setText(result);
        }
    }


}
