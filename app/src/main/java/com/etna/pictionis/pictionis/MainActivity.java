package com.etna.pictionis.pictionis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth firebaseAuth;
    private ListView parties;
    private String test[] = new String[] {
            "plop", "coco", "nool", "clickDatAss"
    };
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


        DatabaseReference DBref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference DBSTblx = DBref.child("Path");
        parties = findViewById(R.id.parties);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, test);
        parties.setAdapter(adapter);
        parties.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println(test[i]);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == launchPartyButton){
            String partyName = partyNameText.getText().toString();
            Intent intent = new Intent(getApplicationContext(), Game.class);
            intent.putExtra("partyname", partyName);
            startActivity(intent);
        }
    }
}
