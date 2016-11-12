package gunnhacks.scanandshop;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by ashwinnathan on 11/11/16.
 */

public class Checkout extends AppCompatActivity{


    private LinearLayout linlayout;
    private TextView textView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private Context asdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);
            linlayout = (LinearLayout) findViewById(R.id.linearlayout);
            asdf = this;
        DatabaseReference fatso = database.getReference();
        fatso.addListenerForSingleValueEvent(new ValueEventListener() {
            String val = "";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childsnap : dataSnapshot.getChildren()) {
                    String temp = childsnap.getKey().toString() + " " + childsnap.getValue().toString();
                    textView = new TextView(asdf);
                    textView.setText(temp);
                    linlayout.addView(textView);
                    val = val + childsnap.getKey().toString() + "=" + childsnap.getValue().toString() + ", ";
                }
                //String val = dataSnapshot.getValue().toString();
                //display.setText(val);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
}


}
