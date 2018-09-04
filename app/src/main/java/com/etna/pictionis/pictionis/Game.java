package com.etna.pictionis.pictionis;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Game extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private TextView phraseWelcome;
    private TextView points;
    private Button disconnect;
    //private Button buttonChat;
    private Button buttonClear;
    private CanvasView customCanvas;
    private String partyname;
    private Boolean isHost;
    private EditText textInputLayout;
    private Button buttonSend;
    private ListView textResponse;

    private DatabaseReference DBref = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference TblxPath;

    ArrayList<String> listText=new ArrayList<String>();
    private String Partyname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        partyname = getIntent().getStringExtra("partyname");
        isHost = getIntent().getBooleanExtra("isHost", false);
        firebaseAuth = FirebaseAuth.getInstance();
        phraseWelcome = findViewById(R.id.phraseWelcome);
        disconnect = findViewById(R.id.disconnect);

        customCanvas = findViewById(R.id.canvas);

        //buttonChat = findViewById(R.id.buttonChat);

        buttonClear = findViewById(R.id.button1);

        points = findViewById(R.id.Points);

        textInputLayout = findViewById(R.id.textInputLayout);

        buttonSend = findViewById(R.id.button_send);

        textResponse = findViewById(R.id.textResponse);

        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(getApplicationContext(), Login.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();


        customCanvas.setPartyName(partyname, isHost);
        phraseWelcome.setText("Bienvenue "+user.getEmail());

        disconnect.setOnClickListener(this);
        //buttonChat.setOnClickListener(this);
        buttonClear.setOnClickListener(this);
        buttonSend.setOnClickListener(this);
    }

    public void clearCanvas(View v) {
        customCanvas.clearRemoteCanvas();
        customCanvas.clearLocalCanvas();
    }

    @Override
    public void onClick(View view) {
        if (view == disconnect){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(getApplicationContext(), Login.class));
        }

        if (view == buttonSend){
            String text = textInputLayout.getText().toString();
            System.out.println("Text : "+ text);
            System.out.println("Refer : "+ this.partyname);

            // EN TRAVAUX !!!!
//            DBref = DBref.child(this.partyname);
//            TblxPath = DBref.child("chat");
//            listText.add(text);
//            TblxPath.setValue(listText);


        }
        if (view == buttonClear){
            customCanvas.clearRemoteCanvas();
            customCanvas.clearLocalCanvas();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
