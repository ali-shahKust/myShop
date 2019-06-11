package com.tool.kustiit.myshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tool.kustiit.myshop.Model.Users;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    public static EditText number , password;
    private Button loginBtn;
    private ProgressDialog loadingbar;
    private FirebaseUser mUser;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        number = (EditText)findViewById(R.id.login_phone);
        loginBtn = (Button) findViewById(R.id.loginactivitybtn);

        loadingbar = new ProgressDialog(this);



        //Login on click

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                loginWithPhone();



            }
        });


    }



    private void loginWithPhone() {
       // Toast.makeText(this, "method Called", Toast.LENGTH_SHORT).show();
        loadingbar.setTitle("Logging In");
        loadingbar.setMessage("Please Wait a While");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();
        PhoneAuthProvider auth = PhoneAuthProvider.getInstance();

        auth.verifyPhoneNumber(number.getText().toString(), 60, TimeUnit.SECONDS, LoginActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {



            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {


                Users.user_number = number.getText().toString();
                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        mUser = FirebaseAuth.getInstance().getCurrentUser();
                        final String uID = mUser.getUid();
                        System.err.println("User id is " + uID);

                        if(task.isSuccessful()){
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uID);
                            HashMap<String, Object> user_map = new HashMap<>();

                            user_map.put("user_number", number.getText().toString());
                            userRef.setValue(user_map);

                            Toast.makeText(LoginActivity.this, "logged in", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                            loadingbar.dismiss();

                        }
                        else {
                            task.getException();
                        }
//
//                        HashMap<String, Object> userMap = new HashMap<>();
//                        userMap.put("user_number" , number.getText().toString());

//                        Toast.makeText(LoginActivity.this, "logged in", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                        startActivity(intent);
//                        finish();
//                        loadingbar.dismiss();


                    }
                });
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("dxdiag", e.getMessage());
            }
        });
    }

    }

