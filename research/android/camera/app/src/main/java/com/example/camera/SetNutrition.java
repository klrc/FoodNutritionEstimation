package com.example.camera;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetNutrition extends AppCompatActivity {
    private String TAG="SetNutrition";
    private EditText m;
    private Button mSureButton;
    private Button mCancelButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_nutrition);
        mSureButton = findViewById(R.id.setnutr_sure);
        mCancelButton = findViewById(R.id.setnutr_cancel);
        mSureButton.setOnClickListener(m_setnutrition_listener);
        mCancelButton.setOnClickListener(m_setnutrition_listener);

}
    View.OnClickListener m_setnutrition_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.setnutr_sure:
                    Log.i(TAG,"sure 1");
                    Intent intent = new Intent(SetNutrition.this,Login.class);
                    Log.i(TAG,"sure 2");
                    startActivity(intent);
                    Log.i(TAG,"sure 3");
                    finish();
                    Log.i(TAG,"sure 4");
                    //save data
                    break;
                case  R.id.setnutr_cancel:
                    Intent intent2 = new Intent(SetNutrition.this,Login.class);
                    //save data
                    break;
            }
        }

    };
}
