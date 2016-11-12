package gunnhacks.scanandshop;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.*;
import java.net.*;
import java.io.*;

public class MainActivity extends AppCompatActivity {

    private Button mQuery;
    private EditText barcode;
    ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQuery = (Button) findViewById(R.id.query);
        barcode = (EditText) findViewById(R.id.barcodeText);

        mQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String bar = barcode.getText().toString();
                DataParser chapathi = new DataParser();
                chapathi.execute();

            }


        });

    }

    public class DataParser extends AsyncTask<String, Void, String> {

        private String stream;
        public TextView mResult = (TextView) findViewById(R.id.result);
        private String fatso;
        private String url = "http://api.walmartlabs.com/v1/items?apiKey=s76z46gcjz56dmjpm3ca7qz8&upc=" + fatso + "&format=json";


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
                throw new RuntimeException(e);
            }

        }

        @Override
        protected void onPostExecute(String message) {
            try{
                JSONObject reader = new JSONObject(message);
                mResult.setText(reader.getString("salePrice"));
            }
            catch(JSONException f){
                throw new RuntimeException(f);
            }
        }

    }
}

