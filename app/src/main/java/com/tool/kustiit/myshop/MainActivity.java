package com.tool.kustiit.myshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button joinbtn , loginbtn ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Main view Buttons

        //joinbtn =(Button)findViewById(R.id.signWithGoogle);
        loginbtn = (Button)findViewById(R.id.loginbtn);


        //Login intent

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);

                startActivity(intent);
            }
        });


    }
}
