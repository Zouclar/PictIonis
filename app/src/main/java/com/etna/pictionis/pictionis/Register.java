package com.etna.pictionis.pictionis;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private Button RegisterButton;
    private EditText editEmail;
    private EditText editPassword;
    private TextView textViewAlreadySign;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        RegisterButton = findViewById(R.id.bouttonEnregistrer);

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);

        textViewAlreadySign = findViewById(R.id.dejaInscrit);

        RegisterButton.setOnClickListener(this);
        textViewAlreadySign.setOnClickListener(this);

        if (firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    //Check email and password and register it
    private void registerUser(){
        final String email = editEmail.getText().toString().trim();
        final String password = editPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Merci d'entrer un Email non nulle", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Merci d'entrer un Mot de passe non nulle", Toast.LENGTH_SHORT).show();
            return;
        }


        if (isPasswordValid(password) && isEmailValid(email)){
            progressDialog.setMessage("Enregistrement du Compte...");
            progressDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Register.this, "Compte Bien enregistré !", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(Register.this, "Problème dans l'enregistrement de votre compte, merci de re-essayer !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }else{
            editPassword.setError(getString(R.string.error_invalid_password));
        }
    }

    //Check if password is valid
    public boolean isPasswordValid(String password) {
        if (password.length() > 5){
            return true;
        } else {
            editPassword.setError(getString(R.string.error_invalid_password));
            editPassword.requestFocus();
            return false;
        }
    }


    //Check if email is valid
    public boolean isEmailValid(String email) {
        if (email.contains("@") && email.contains(".")){
            return true;
        } else {
            editEmail.setError(getString(R.string.error_invalid_email));
            editEmail.requestFocus();
            return false;
        }
    }

    @Override
    public void onClick(View view) {
        if (view == RegisterButton){
            registerUser();
        }

        if (view == textViewAlreadySign){
            startActivity(new Intent(getApplicationContext(), Login.class));
        }
    }
}
