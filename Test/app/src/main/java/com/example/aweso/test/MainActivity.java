package com.example.aweso.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Button mSend;
    private Button mRead;
    private EditText mEdit;
    private EditText mEditRead;
    private TextView display;
    private String test = "Hello";
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSend = (Button) findViewById(R.id.addData);
        mRead = (Button) findViewById(R.id.readData);
        mEdit = (EditText) findViewById(R.id.editText);
        mEditRead = (EditText) findViewById(R.id.editTextRead);
        display = (TextView) findViewById(R.id.vinay);


        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String entry = mEdit.getText().toString();
                DatabaseReference myRef = database.getReference(entry);
                myRef.setValue(test);
            }
        });



        mRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String reference = mEditRead.getText().toString();
                DatabaseReference fatso = database.getReference(reference);
                fatso.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String val = dataSnapshot.getValue().toString();
                        display.setText(val);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });


    }
}
