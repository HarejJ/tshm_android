package com.example.nejc.tshm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LogInActivity extends AppCompatActivity {
    LogInActivity logInActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logInActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        Button logIn = (Button) findViewById(R.id.LogInButton);
        TextView registration = (TextView) findViewById(R.id.RegistrationText);
        TextView forgotPasword = (TextView) findViewById(R.id.ForgotPassword);

        logIn.setOnClickListener(onClickListener);
        registration.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d("view: ", String.valueOf(view.getId()));
            Log.d("gumb: ", String.valueOf(R.id.LogInButton));
            switch (view.getId()){
                case R.id.LogInButton:
                    sendData(view);
                    break;
                case  R.id.RegistrationText:
                    Intent intent = new Intent(logInActivity, RegistrationActivity.class);
                    startActivity(intent);
            }
        }
    };
    private void sendData(View view){
        String username = ((EditText)findViewById(R.id.UserName)).getText().toString();
        String password = (((EditText)findViewById(R.id.Password)).getText().toString());
        TextView error = (TextView) findViewById(R.id.Error);
        error.setTextColor(getResources().getColor(R.color.error));
        if(username.length()==0 || password.length() == 0){
            error.setText("Vnesite uporabniško ime in geslo");
            return;
        }
        error.setText("");
        //TODO: šifrirati geslo
        RESTCallTask restTask = new RESTCallTask(logInActivity,view, "login", username, password);
        restTask.execute("POST", String.format("login", username, password));
    }

}
