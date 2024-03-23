package com.webiotid.happybirthdays;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText txtUser, txtPass;
    private Button btnLogin;
    /*String username = "admin";
    String password = "admin";*/

    AlertDialog.Builder dialog;
    LayoutInflater inflaters;
    View dialogView;
    private TextView tvRegister;
    private EditText etUsername, etPassword, etRepeatPassword;
    private DatabaseHelper db;
    public static final String SHARED_PREF_NAME = "myPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUser = findViewById(R.id.txtUsername);
        txtPass = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.login);
        tvRegister = findViewById(R.id.tvRegister);

        db = new DatabaseHelper(this);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new AlertDialog.Builder(LoginActivity.this);
                inflaters = getLayoutInflater();
                dialogView = inflaters.inflate(R.layout.form_register, null);
                dialog.setView(dialogView);
                dialog.setCancelable(true);
                dialog.setIcon(R.drawable.webiotid);
                dialog.setTitle("Register");

                etUsername = (EditText) dialogView.findViewById(R.id.etUsername);
                etPassword = (EditText) dialogView.findViewById(R.id.etPassword);
                etRepeatPassword = (EditText) dialogView.findViewById(R.id.etRepeatPassword);

                dialog.setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                dialog.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = dialog.create();
                alert.setCanceledOnTouchOutside(false);
                alert.show();
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String inUsername = etUsername.getText().toString();
                        String inPassword = etPassword.getText().toString();
                        String inrePassword = etRepeatPassword.getText().toString();

                        Boolean cekUser = db.checkUser(inUsername);
                        if (inUsername.isEmpty() || inPassword.isEmpty() || inrePassword.isEmpty()) {
                            Toast.makeText(LoginActivity.this, "Form belum diisi semua", Toast.LENGTH_LONG).show();
                        }else if (cekUser){
                            etUsername.setError("Username Sudah Ada");
                        }else if (!(inrePassword.equals(inPassword))){
                            etRepeatPassword.setError("Password Tidak Sama");
                        }else {
                            Boolean daftar = db.simpanUser(inUsername, inPassword);
                            if (daftar == true){
                                Toast.makeText(LoginActivity.this, "Daftar Berhasil", Toast.LENGTH_LONG).show();
                                alert.dismiss();
                            }else {
                                Toast.makeText(LoginActivity.this, "Daftar Gagal", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getUsername = txtUser.getText().toString();
                String getPassword = txtPass.getText().toString();

                if (getPassword.isEmpty() && getPassword.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Username atau password salah!", Toast.LENGTH_LONG).show();
                }else{
                    Boolean masuk = db.checkLogin(getUsername, getPassword);
                    if (masuk == true){
                        Boolean updateSession = db.upgradeSession("ada", 1);
                        if (updateSession == true){
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putBoolean("masuk", true);
                            editor.apply();
                            Toast.makeText(getApplicationContext(), "Berhasil Masuk", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Gagal Masuk", Toast.LENGTH_LONG).show();
                    }
                }

                /*if (txtUser.getText().toString().equalsIgnoreCase(username) && txtPass.getText().toString().equalsIgnoreCase(password)) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "Username dan Password Salah", Toast.LENGTH_SHORT);
                }*/
            }
        });

    }
}