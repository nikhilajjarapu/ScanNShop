package gunnhacks.scanandshop;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import org.json.*;
import java.net.*;
import java.io.*;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    private GestureDetectorCompat gestureObject;

    private ImageButton scanBtn;
    private ImageButton cartBtn;
    private ImageButton dealsBtn;
    public String UPC = "673419189682";
    FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scanBtn = (ImageButton)findViewById(R.id.scan_button);
        cartBtn = (ImageButton)findViewById(R.id.cart);
        scanBtn.setOnClickListener(this);
        cartBtn.setOnClickListener(this);


        //gestureObject = new GestureDetectorCompat(this, new LearnGesture());

    }
    public void onClick(View v){
        if(v.getId()==R.id.scan_button)
        {
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
        if(v.getId()==R.id.cart)
        {
            Intent intent = new Intent(this, Checkout.class);
            startActivity(intent);
        }

    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            UPC = scanningResult.getContents().toString();
            String scanFormat = scanningResult.getFormatName();
            //formatTxt.setText("FORMAT: " + scanFormat);
            //contentTxt.setText("CONTENT: " + UPC);
            DataParser dp = new DataParser();
            dp.execute();
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }

    }
    public class DataParser extends AsyncTask<String, Void, String> {

        private String stream;

        private String url = "http://api.walmartlabs.com/v1/items?apiKey=s76z46gcjz56dmjpm3ca7qz8&upc=" + UPC + "&format=json";


        @Override
        protected String doInBackground(String[] params) {
            try {
                URLConnection walmart = new URL(url).openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(walmart.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                stream = sb.toString();
                return stream;

            }
            catch (IOException e){
                return "Error";
                //throw new RuntimeException(e);

            }


        }

        @Override
        protected void onPostExecute(String message) {
            try{

                JSONObject reader = new JSONObject(message);
                //mResult.setText(reader.getJSONArray("items").getJSONObject(0).getString("salePrice"));
                String saleprice = reader.getJSONArray("items").getJSONObject(0).getString("salePrice");
                String itemname = reader.getJSONArray("items").getJSONObject(0).getString("name");

                String entry = itemname;
                DatabaseReference myRef = database.getReference(entry);
                myRef.setValue(saleprice);
            }
            catch(JSONException f){
                throw new RuntimeException(f);
            }
        }

    }

    /*class LearnGesture extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY){
            if(event2.getX()>event1.getX())
            { //left to right swipe

            } else if(event2.getX()<event1.getX())
            {
                Intent intent = new Intent(MainActivity.this, Checkout.class);
                startActivity(intent);
            }
            return true;
        }
    }*/

}

