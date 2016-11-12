package gunnhacks.scanandshop;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class Checkout extends AppCompatActivity implements OnClickListener{


    private LinearLayout linlayout;
    private TextView textView;
    private TextView totalView;
    private LinearLayout horizlayout;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private Context asdf;
    private ImageButton scanBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);
        scanBtn = (ImageButton) findViewById(R.id.scan);
        scanBtn.setOnClickListener(this);
        linlayout = (LinearLayout) findViewById(R.id.linearlayout);
        asdf = this;
        totalView = (TextView) findViewById(R.id.total);
        DatabaseReference fatso = database.getReference();
        fatso.addListenerForSingleValueEvent(new ValueEventListener() {





            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int total = 0;
                for (DataSnapshot childsnap : dataSnapshot.getChildren()) {
                    String prodname = childsnap.getKey().toString();
                    String price = childsnap.getValue().toString();
                    horizlayout = new LinearLayout(asdf);
                    horizlayout.setOrientation(LinearLayout.HORIZONTAL);

                    textView = new TextView(asdf);
                    textView.setText(prodname);
                    textView.setWidth(500);
                    textView.setTextSize(15);
                    horizlayout.addView(textView);
                    textView = new TextView(asdf);
                    textView.setText(price);
                    textView.setTextSize(15);
                    horizlayout.setPadding(0, 5, 0, 5);
                    horizlayout.addView(textView);
                    linlayout.addView(horizlayout);
                    total += Double.parseDouble(price);
                }
                totalView.setText(Double.toString(total));
                //String val = dataSnapshot.getValue().toString();
                //display.setText(val);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onClick(View view){
        if(view.getId()==R.id.scan){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        if(view.getId()==R.id.pay)
        {
            Intent intent = new Intent(this, Pay.class);
            startActivity(intent);
        }
    }


}
