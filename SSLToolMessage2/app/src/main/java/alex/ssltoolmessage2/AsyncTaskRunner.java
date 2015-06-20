package alex.ssltoolmessage2;

import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.os.AsyncTask;
import android.util.Log;


public class AsyncTaskRunner extends  AsyncTask<String,String,String>{

    private String response;
    @Override
    protected String doInBackground(String... params) {
        int count = params.length;
        if(count==2){
            ArrayList<NameValuePair> postParameters = new ArrayList<>();
            postParameters.add(new BasicNameValuePair("username",params[0]));
            postParameters.add(new BasicNameValuePair("password",params[1]));
            try {
                response = SimpleHttpClient.executeHttpPost("https://192.168.227.1:8443/SSLTestServer/AppResponse", postParameters);
                String res = response.toString();
                Log.d("Response", res);
                this.response = res.replaceAll("\\s+", "");
            } catch (Exception e) {
                e.printStackTrace();
                this.response = e.getMessage();
            }
        }else{
            response ="Invalid number of arguments-"+count;
        }
        return response;
    }
}