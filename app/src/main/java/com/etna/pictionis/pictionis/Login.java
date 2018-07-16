package com.etna.pictionis.pictionis;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private Button LoginButton;
    private EditText editEmail;
    private EditText editPassword;
    private TextView textViewNotRegistered;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        LoginButton = findViewById(R.id.bouttonConnexion);

        editEmail = findViewById(R.id.editEmailSignIn);
        editPassword = findViewById(R.id.editPasswordSignIn);

        textViewNotRegistered = findViewById(R.id.pasEncoreInscrit);

        LoginButton.setOnClickListener(this);
        textViewNotRegistered.setOnClickListener(this);

        if (firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }



    private void loginUser(){
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
            progressDialog.setMessage("Connexion en cours...");
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(Login.this, "Vous êtes Connectés !", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(Login.this, "Problème de connexion, merci de re-essayer !", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else{
            editPassword.setError(getString(R.string.error_invalid_password));
        }


    }


    public boolean isPasswordValid(String password) {
        if (password.length() > 5){
            return true;
        } else {
            editPassword.setError(getString(R.string.error_invalid_password));
            editPassword.requestFocus();
            return false;
        }
    }

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
        if (view == LoginButton){
            loginUser();
        }

        if (view == textViewNotRegistered){
            startActivity(new Intent(this, Register.class));
        }
    }
}
