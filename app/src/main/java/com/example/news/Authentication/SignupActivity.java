package com.example.news.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.news.MainActivity;
import com.example.news.ModelClasses.User;
import com.example.news.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private TextView logbtn;
    private EditText editTextname , editTextemail , editTextpassword;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignupActivity.this, SignupActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);



        mAuth=FirebaseAuth.getInstance();

        logbtn=(TextView)findViewById(R.id.gotologin);
        Button gettingst= findViewById(R.id.register);

        editTextname =(EditText) findViewById(R.id.login_email);
        editTextemail=(EditText) findViewById(R.id.login_pass);
        editTextpassword=(EditText) findViewById(R.id.signuppassword);


        logbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });

        gettingst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
                startActivity(new Intent(SignupActivity.this, MainActivity.class));
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.gotologin:
                startActivity(new Intent(this , LoginActivity.class));
                break;

            case R.id.register:
                Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String email = editTextemail.getText().toString().trim();
        String password = editTextpassword.getText().toString().trim();
        String name = editTextname.getText().toString().trim();

        if(name.isEmpty()){
            editTextname.setError(" User name is required");
            editTextname.requestFocus();
            return;
        }

        if(email.isEmpty()){
            editTextemail.setError(" email is required");
            editTextemail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextemail.setError("Please provide valid email");
            editTextemail.requestFocus();
            return;
        }

        if(password.isEmpty() || password.length() <6){
            editTextpassword.setError(" password should have atleast 6 letters");
            editTextpassword.requestFocus();
            return;
        }


        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User( name ,email , password);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(SignupActivity.this, "registration done", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                            }else{
                                                Toast.makeText(SignupActivity.this, "Failed to register", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }else{
                            Toast.makeText(SignupActivity.this, "Failed to go further", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}