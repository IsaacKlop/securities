package itopia.ssltestingtool;

import android.os.AsyncTask;


public class AsyncTaskRunner extends  AsyncTask<String,String,String>{

    private String response;
    @Override
    protected String doInBackground(String... params) {
        try {
            response = SimpleHttpClient.executeHttpPost("https://192.168.227.1:8443/SSLTestServer/AppResponse", params);
        } catch (Exception e) {
            e.printStackTrace();
            this.response = e.getMessage();
        }
        return response;
    }
}