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

/**
 * Created by jzou on 11/12/16.
 */

public class Final extends AppCompatActivity implements OnClickListener{
    private Button payBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finalize);
        payBtn = (Button) findViewById(R.id.finalize);
        payBtn.setOnClickListener(this);

    }

    public void onClick(View view){
//        if(view.getId()==R.id.finalize){
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//        }

    }
}
