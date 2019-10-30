package com.example.camera;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ResultOthers extends AppCompatActivity {
    private List<ResultShowInfo> resultList = new ArrayList<>();
    final private String TAG = "ResultOthers";
    private List<String> detected_result=new ArrayList<>();
    private String result1_vol="100";
    private String result1_weight="200";
    private String result2_vol="100";
    private String result2_weight="200";
    private String result3_vol="100";
    private String result3_weight="200";
    private Button all_wrong;
    private String loginuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_others);
        Intent intent =getIntent();
        loginuser=intent.getStringExtra("user");
        Log.i(TAG,"loginuser:"+loginuser);
        detected_result.add("牛肉");
        detected_result.add("鱼肉");
        detected_result.add("羊肉");
        final ListView listView = (ListView)findViewById(R.id.result_others_list);
        all_wrong=findViewById(R.id.all_wrong);
        showResult(detected_result);
        final ResultOthersAdapter adapter = new ResultOthersAdapter(ResultOthers.this,R.layout.result_item,resultList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ResultShowInfo result_item = resultList.get(position);
                Intent i = new Intent(ResultOthers.this,ResultShow.class);
                i.putExtra("user",loginuser);
                i.putExtra("result_food_name",result_item.getName());
                startActivity(i);
                finish();
            }
        });
        all_wrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultOthers.this,AllWrong.class);
                intent.putExtra("user",loginuser);
                startActivity(intent);
            }
        });



    }

    private void showResult(List<String> resultlist){
        String id="null";
        ResultShowInfo reusltinfo = new ResultShowInfo(resultlist.get(0),R.raw.beef2,result1_vol,result1_weight);
        resultList.add(reusltinfo);
        ResultShowInfo reusltinfo2 = new ResultShowInfo(resultlist.get(1),R.raw.cng,result2_vol,result2_weight);
        resultList.add(reusltinfo2);
        ResultShowInfo reusltinfo3 = new ResultShowInfo(resultlist.get(2),R.raw.currybeef,result3_vol,result3_weight);
        resultList.add(reusltinfo3);

//        for(int i=0;i<resultlist.size();i++){
//            Log.i(TAG,"用户都有："+resultlist.get(i));
//            String fn = result_food_list.get(i);
//            ResultShowInfo u = new  ResultShowInfo(resultlist.get(i),R.drawable.login_1_temp_icon,id);
//            resultList.add(u);
//        }
    }
}