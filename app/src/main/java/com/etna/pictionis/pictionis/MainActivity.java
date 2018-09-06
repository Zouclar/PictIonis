package com.etna.pictionis.pictionis;

import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth firebaseAuth;
    private DatabaseReference DBref = FirebaseDatabase.getInstance().getReference();
    private ListView parties;
    ArrayList<String> partiesArray = new ArrayList<String>();
    private Button launchPartyButton;
    private EditText partyNameText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        launchPartyButton = findViewById(R.id.launch);
        partyNameText   = findViewById(R.id.partyname);
        firebaseAuth =  FirebaseAuth.getInstance();

        launchPartyButton.setOnClickListener(this);

        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(getApplicationContext(), Login.class));
        }

        DBref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    partiesArray.add(snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Cancelled");
            }
        });

        DatabaseReference DBref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference DBSTblx = DBref.child("Path");
        parties = findViewById(R.id.parties);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, partiesArray);
        parties.setAdapter(adapter);
        parties.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String partyName = (String) (parties.getItemAtPosition(i));
                Intent intent = new Intent(getApplicationContext(), Game.class);
                intent.putExtra("partyname", partyName);
                intent.putExtra("isHost", false);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == launchPartyButton){
            String partyName = partyNameText.getText().toString();
            Intent intent = new Intent(getApplicationContext(), Game.class);
            intent.putExtra("nameparty", partyName);
            intent.putExtra("isHost", true);
            startActivity(intent);
        }
    }
}
