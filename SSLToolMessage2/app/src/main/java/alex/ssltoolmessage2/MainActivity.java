package alex.ssltoolmessage2;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import alex.ssltoolmessage2.R;


public class MainActivity extends ActionBarActivity {

    EditText etUsername, etPassword;
    TextView finalResult;
    Button login;
    private AsyncTask<String, String, String> asyncTask;
    private String response;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();
        setContentView(R.layout.activity_main);
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        login = (Button) findViewById(R.id.btn_login);
        finalResult = (TextView) findViewById(R.id.tv_error);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskRunner runner=new AsyncTaskRunner();
                String username=etUsername.getText().toString();
                String password=etPassword.getText().toString();
                asyncTask=runner.execute(username,password);

                try {
                    String asyncResultText=asyncTask.get();
                    response = asyncResultText.trim();
                } catch (InterruptedException e1) {
                    response = e1.getMessage();
                } catch (ExecutionException e1) {
                    response = e1.getMessage();
                } catch (Exception e1) {
                    response = e1.getMessage();
                }
                finalResult.setText(response);
            }
        });
    }

    public static Context getAppContext() {
        return MainActivity.context;
    }
}
