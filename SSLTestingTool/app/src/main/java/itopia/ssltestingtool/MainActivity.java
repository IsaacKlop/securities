package itopia.ssltestingtool;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;


public class MainActivity extends ActionBarActivity {

    EditText etUsername, etPassword;
    TextView finalResult;
    Button login;
    ToggleButton tbHostname, tbDate, tbCa;
    private AsyncTask<String, String, String> asyncTask;
    private String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        tbHostname = (ToggleButton) findViewById(R.id.toggleHostname);
        tbDate = (ToggleButton) findViewById(R.id.toggleDate);
        tbCa = (ToggleButton) findViewById(R.id.toggleCA);
        login = (Button) findViewById(R.id.btn_login);
        finalResult = (TextView) findViewById(R.id.tv_error);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskRunner runner = new AsyncTaskRunner();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                //asyncTaskRunner only accepts Strings as input, booleans converted to strings and converted back in SimpleHttpClient
                String hostname = String.valueOf(tbHostname.isChecked());
                String date = String.valueOf(tbDate.isChecked());
                String ca = String.valueOf(tbCa.isChecked());

                asyncTask = runner.execute(username, password, hostname, date, ca);

                try {
                    String asyncResultText = asyncTask.get();
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
}
