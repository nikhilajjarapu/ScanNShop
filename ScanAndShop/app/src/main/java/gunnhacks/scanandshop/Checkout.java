package gunnhacks.scanandshop;

import android.app.Activity;
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

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

/**
 * Created by ashwinnathan on 11/11/16.
 */

public class Checkout extends AppCompatActivity implements OnClickListener{

    final String TAG = getClass().getName();
    private LinearLayout linlayout;
    private TextView textView;
    private TextView totalView;
    private LinearLayout horizlayout;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private Context asdf;
    private ImageButton scanBtn;
    private ImageButton payBtn;
    private int MY_SCAN_REQUEST_CODE = 100;
    private String cardinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);
        scanBtn = (ImageButton) findViewById(R.id.scan);
        scanBtn.setOnClickListener(this);
        payBtn = (ImageButton) findViewById(R.id.pay);
        payBtn.setOnClickListener(this);
        linlayout = (LinearLayout) findViewById(R.id.linearlayout);
        asdf = this;
        totalView = (TextView) findViewById(R.id.total);
        cardinfo = "";
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
        public void onScanPress(View v) {
            Intent scanIntent = new Intent(this, CardIOActivity.class);

            // customize these values to suit your needs.
            scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
            scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
            scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false

            // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
            startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);

        }
    protected void onResume() {
        super.onResume();

        if (!CardIOActivity.canReadCardWithCamera()) {
            Intent intent = new Intent(this, Pay.class);
            startActivity(intent);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == MY_SCAN_REQUEST_CODE) {
            String resultDisplayStr;
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                resultDisplayStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";

                // Do something with the raw number, e.g.:
                // myService.setCardNumber( scanResult.cardNumber );

                if (scanResult.isExpiryValid()) {
                    resultDisplayStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
                }

                if (scanResult.cvv != null) {
                    // Never log or display a CVV
                    resultDisplayStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
                }

                if (scanResult.postalCode != null) {
                    resultDisplayStr += "Postal Code: " + scanResult.postalCode + "\n";
                }
            }
            else {
                resultDisplayStr = "";
            }
            cardinfo = resultDisplayStr;
            // do something with resultDisplayStr, maybe display it in a textView
            // resultTextView.setText(resultDisplayStr);
        }
        Intent intent = new Intent(this, Final.class);
        startActivity(intent);
        // else handle other activity results
    }



    public void onClick(View view){
        if(view.getId()==R.id.scan){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        if(view.getId()==R.id.pay && !cardinfo.isEmpty())
        {
            Intent intent = new Intent(this, Pay.class);
            startActivity(intent);
        } else if(view.getId()==R.id.pay)
        {
            onScanPress(view);
        }
    }


}
