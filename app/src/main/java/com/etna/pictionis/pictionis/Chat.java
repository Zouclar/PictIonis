package com.etna.pictionis.pictionis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Chat extends AppCompatActivity implements View.OnClickListener {

    private Button SendButton;
    private Button Rollback;
    private TextView UserName;
    private EditText ToSubmit;

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
            System.out.print(ToSubmit.getText().toString());
        }

        if (view == Rollback){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
