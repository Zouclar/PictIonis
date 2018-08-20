package com.etna.pictionis.pictionis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Chat extends AppCompatActivity implements View.OnClickListener {
//TODO: Faire le chat
    private Button SendButton;
    private Button Rollback;
    private TextView UserName;
    private EditText ToSubmit;
    DatabaseReference DBref = FirebaseDatabase.getInstance().getReference();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        SendButton = findViewById(R.id.buttonSend);
        UserName = findViewById(R.id.UserName);
        ToSubmit = findViewById(R.id.editText);
        Rollback = findViewById(R.id.RollbackButton);

        Rollback.setText("<");

        SendButton.setOnClickListener(this);
        Rollback.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == SendButton){
            String UIDuser = currentUser.getUid();
            System.out.print(ToSubmit.getText().toString());
        }

        if (view == Rollback){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
