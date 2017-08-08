package com.sachinchandil.securedpreference.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.sachinchandil.securedpreference.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generatePinAndSave();
        checkLogin();
    }

    private SecuredPreference securedPreference;
    public void generatePinAndSave() {
        securedPreference = new SecuredPreference(this);
        if(!securedPreference.isLoginPinSet()){
            securedPreference.setLoginPin("12345678@asdf");
        }
    }

    public void checkLogin(){
        if(securedPreference.loginPinMatches("12345678@asdf")){
            Log.w(MainActivity.class.getSimpleName(), "Login successful");
        }else{
            Log.w(MainActivity.class.getSimpleName(), "Login failed");
        }
    }
}
