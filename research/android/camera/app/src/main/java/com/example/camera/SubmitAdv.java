package com.example.camera;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SubmitAdv extends AppCompatActivity {
    private Button submit;
    private Button back;
    private EditText adv;
    private String TAG = "submitAdv";
    private String loginuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_adv);
        submit = (Button)findViewById(R.id.submit);
        back = (Button)findViewById(R.id.adv_back);
        adv = (EditText) findViewById(R.id.adv);
        Intent i = getIntent();
       loginuser= i.getStringExtra("user");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String adv_content = adv.getText().toString();

                if(adv_content.trim().equals("")){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(SubmitAdv.this);
                    dialog.setTitle("提交失败！");
                    dialog.setMessage("请输入您的建议");
                    dialog.setCancelable(true);
                    dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Intent intent = new Intent(SubmitAdv.this,SubmitAdv.class);
                            //startActivity(intent);
                            //finish();
                        }
                    });
                    dialog.show();
                }
                else{
                AlertDialog.Builder dialog = new AlertDialog.Builder(SubmitAdv.this);
                dialog.setTitle("提交成功！");
                dialog.setMessage("我们已将您的意见提交上去，感谢您的宝贵意见");
                dialog.setCancelable(true);
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(SubmitAdv.this,Setting.class);
                        intent.putExtra("user",loginuser);
                        startActivity(intent);
                        finish();
                    }
                });
                dialog.show();}
                ///文字发送给某邮箱的逻辑
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_submit_setting = new Intent(SubmitAdv.this,Setting.class);
                intent_submit_setting.putExtra("user",loginuser);
                startActivity(intent_submit_setting);
                finish();
            }
        });
    }
}
