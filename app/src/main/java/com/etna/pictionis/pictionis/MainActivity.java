package com.etna.pictionis.pictionis;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import static android.R.attr.value;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private TextView phraseWelcome;
    private TextView points;
    private Button disconnect;
    private Button buttonChat;
    private CanvasView customCanvas;
    DatabaseReference DBref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference DBSTblx = DBref.child("Points");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        phraseWelcome = findViewById(R.id.phraseWelcome);
        disconnect = findViewById(R.id.disconnect);

        customCanvas = findViewById(R.id.canvas);

        buttonChat = findViewById(R.id.buttonChat);

        points = findViewById(R.id.Points);


        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(getApplicationContext(), Login.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        phraseWelcome.setText("Bienvenue "+user.getEmail());

        disconnect.setOnClickListener(this);
        buttonChat.setOnClickListener(this);
    }

    public void clearCanvas(View v) {
        customCanvas.clearCanvas();
    }

    @Override
    public void onClick(View view) {
        if (view == disconnect){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(getApplicationContext(), Login.class));
        }

        if (view == buttonChat){
            finish();
            startActivity(new Intent(getApplicationContext(), Chat.class));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        DBSTblx.addValueEventListener(new ValueEventListener() {
            public static final String TAG = "";

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //String point = dataSnapshot.getValue(String.class);
                //Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                //Double point = dataSnapshot.getValue(Double.class);
                //System.out.println(map.toString());
                //Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                //for (Map.Entry<String, String> entry : map.entrySet()) {
                //   System.out.println("Key : " + entry.getKey().toString() + " Value : " + entry.getValue().toString());
                //}

                //points.setText(map.toString());
                //Log.d(TAG, "Value is: " + value);
                //double[] tableau = customCanvas.getPositions();
                //System.out.print(point.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.print(databaseError.toString());
            }
        });
    }
}
