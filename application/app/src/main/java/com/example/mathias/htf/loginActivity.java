package com.example.mathias.htf;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class loginActivity extends AppCompatActivity {
    int statuscode = 503;
    doRegister register;
    doLogin login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btnLogin = (Button)findViewById(R.id.btnLogin);
        Button btnRegister = (Button)findViewById(R.id.btnRegister);
        Button btnSuccess = (Button)findViewById(R.id.btnSuccess);
        Button btnFail = (Button)findViewById(R.id.btnFail);
        btnSuccess.setOnClickListener(clickListenerLogin);
        btnFail.setOnClickListener(clickListenerLogin);
        btnLogin.setOnClickListener(clickListenerLogin);
        btnRegister.setOnClickListener(clickListenerLogin);
        register  = new doRegister(getApplicationContext(),this);
        login = new doLogin(getApplicationContext(),this);
    }

    View.OnClickListener clickListenerLogin = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnLogin:
                    //run login code
                    login();
                    break;
                case R.id.btnRegister:
                    //run register code
                    register();
                    break;
                case R.id.btnFail:
                    statuscode = 503;
                    register.statuscode =503;
                    Log.e("statuscode",String.valueOf(statuscode));
                    break;
                case R.id.btnSuccess:
                    statuscode = 201;
                    register.statuscode = 201;
                    Log.e("statuscode",String.valueOf(statuscode));
                    break;
                default:
                    Log.e("Error","e do not this buttom space marine");
            }
        }
    };

    public void login(){
        Log.e("loggin","inside login function");
        EditText txtResearcher = (EditText)findViewById(R.id.txtUsername);
        EditText txtPassword = (EditText)findViewById(R.id.txtPassword);
        String Username = txtResearcher.getText().toString();
        String Password = txtPassword.getText().toString();
        ArrayList<String> params = new ArrayList<String>();
        params.add(Username);
        params.add(Password);
        login.execute(params);

    }


    public void register(){
        Log.e("loggin","inside register function");
        EditText txtResearcher = (EditText)findViewById(R.id.txtUsername);
        EditText txtPassword = (EditText)findViewById(R.id.txtPassword);

        String Username = txtResearcher.getText().toString();
        String Password = txtPassword.getText().toString();
        ArrayList<String> params = new ArrayList<String>();
        params.add(Username);
        params.add(Password);
        params.add(String.valueOf(statuscode));
        register.execute(params);


    }
}
