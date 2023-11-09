package com.example.moodmetrics;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.moodmetrics.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    TextView errorMsg;
    EditText inputID, inputPass;
    Button signInBTN, registerBTN, forgotPasswordBTN;
    DBHelper DB;
    static String tmpName;

    SharedPreferences pref;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        errorMsg = findViewById(R.id.infoError);
        inputID = findViewById(R.id.userID);
        inputPass = findViewById(R.id.userPassword);
        signInBTN = findViewById(R.id.signIn);
        registerBTN = findViewById(R.id.register);
        forgotPasswordBTN = findViewById(R.id.forgotPassword);
        pref = getSharedPreferences("user_details",MODE_PRIVATE);

        errorMsg.setVisibility(View.INVISIBLE);

        DB = DBHelper.getInstance(this);

        signInBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = inputID.getText().toString();
                String password = inputPass.getText().toString();
                tmpName=username;

                if (DB.checkusernamePassword(username, password)) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("username",username);
                    editor.putString("password",password);
                    editor.commit();
                    startActivity(intent);
                    errorMsg.setVisibility(View.INVISIBLE);
                } else {
                    errorMsg.setVisibility(View.VISIBLE);
                }
            }
        });

        registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = inputID.getText().toString();

                if (DB.checkusername(username)) {
                    Toast.makeText(LoginActivity.this, "Same ID already exists\nPlease change ID to register", Toast.LENGTH_SHORT).show();
                } else if (username.length() < 6) {
                    Toast.makeText(LoginActivity.this, "Your ID is too short", Toast.LENGTH_SHORT).show();
                } else {
                    showDialog(username);
                }
            }
        });

        forgotPasswordBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = inputID.getText().toString();
                if(username.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter your ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(DB.checkusername(username)) {
                    String password = DB.getPassword(username); // You need to create this method in DBHelper class
                    AlertDialog.Builder passwordAlert = new AlertDialog.Builder(LoginActivity.this);
                    passwordAlert.setTitle("Forgot Password");
                    passwordAlert.setMessage("Your password is: " + password);
                    passwordAlert.setPositiveButton("OK", null);
                    passwordAlert.show();
                } else {
                    Toast.makeText(LoginActivity.this, "User ID does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void showDialog(final String username) {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(LoginActivity.this)
                .setTitle("Register")
                .setMessage("Are you sure you want to register with this ID and Password?")
                .setPositiveButton("Register", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String password = inputPass.getText().toString();
                        DB.insertData(username, password);
                        Toast.makeText(LoginActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(LoginActivity.this, "Cancel Register", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }
}