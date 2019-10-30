package com.example.camera;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import android.text.TextUtils;
import android.support.v7.widget.SearchView;
import java.util.List;

public class FoodMenu extends AppCompatActivity {
    private String TAG = "FoodMenu";
    private List<Food> foodList;
    private List<Food> findList;
    private List<Food> searchList = new ArrayList<>();
    private List<String> nameList;
    private FoodAdapter findAdapter;
    private FoodAdapter adapter;
    private FoodDataManager mFoodDataManager;
    private ListView listView;
    private SearchView searchView;
    //搜索框

//    Object[] names;
   // ArrayAdapter<String> adapter;
   // ArrayList<String> mAllList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_food_menu);
        listView = (ListView) findViewById(R.id.food_menu);
        //搜索框逻辑
        searchView = (SearchView) findViewById(R.id.menu_search);
        foodList = new ArrayList<Food>();
        findList = new ArrayList<Food>();

        listView.setAdapter(new ArrayAdapter<Object>(getApplicationContext(),
                android.R.layout.simple_expandable_list_item_1));
        listView.setTextFilterEnabled(true);
        //
        if (mFoodDataManager == null) {
            mFoodDataManager = new FoodDataManager(this);
            mFoodDataManager.openDataBase();
        }
        Log.i(TAG,"START");
        //初始化 读取菜单。
        initFoods("Food_Name");
        final FoodAdapter foodadapter = new FoodAdapter(FoodMenu.this,R.layout.food_item,foodList);
        listView.setAdapter(foodadapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Food food = foodList.get(position);
                String foodname = food.getFood_name();
                Intent intent_FoodName_FoodInfo = new Intent(FoodMenu.this,FoodInfo.class);
                intent_FoodName_FoodInfo.putExtra("foodname",foodname);
                startActivity(intent_FoodName_FoodInfo);
            }
        });

        /**
         * 默认情况下是没提交搜索的按钮，所以用户必须在键盘上按下"enter"键来提交搜索.你可以同过setSubmitButtonEnabled(
         * true)来添加一个提交按钮（"submit" button)
         * 设置true后，右边会出现一个箭头按钮。如果用户没有输入，就不会触发提交（submit）事件
         */
        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {

            //输入完成后，提交时触发的方法，一般情况是点击输入法中的搜索按钮才会触发，表示现在正式提交了
            public boolean onQueryTextSubmit(String query)
            {
                List<String> fl = mFoodDataManager.showFoodName("Food_Name");
                String[] foodlist = new String[fl.size()];
                fl.toArray(foodlist);

                if(TextUtils.isEmpty(query))
                {
                    Log.i(TAG,"null");
                    Toast.makeText(FoodMenu.this, "请输入查找内容！", Toast.LENGTH_SHORT).show();
                   listView.setAdapter(adapter);

                }
                else
                {
                    findList.clear();
                    for(int i = 0; i < foodlist.length; i++)
                    {
                        //iconInformation information = list.get(i);
                        Log.i(TAG,"it isnt");
                        Log.i(TAG,foodlist[i]);
                        if(foodlist[i].toString()==query)
                        {
                            Log.i(TAG,foodlist[i].toString());
                            Food food = new Food(foodlist[i],R.drawable.food);
                            findList.add(food);
                            break;
                        }
                    }
                    if(findList.size() == 0)
                    {
                        Log.i(TAG,"CANT FIND");
                        Toast.makeText(FoodMenu.this, "查找的商品不在列表中a ", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(FoodMenu.this, "查找成功", Toast.LENGTH_SHORT).show();
                        findAdapter = new FoodAdapter(FoodMenu.this,R.layout.food_item, findList);
                        listView.setAdapter(findAdapter);
                    }
                }
                return true;
            }

            //在输入时触发的方法，当字符真正显示到searchView中才触发，像是拼音，在输入法组词的时候不会触发
            public boolean onQueryTextChange(String newText)
            {
                List<String> fl = mFoodDataManager.showFoodName("Food_Name");
                String[] foodlist = new String[fl.size()];
                fl.toArray(foodlist);
                if(TextUtils.isEmpty(newText))
                {
                    Toast.makeText(FoodMenu.this, "请输入查找内容！", Toast.LENGTH_SHORT).show();
                    listView.setAdapter(adapter);
                }
                else
                {
                    findList.clear();
                    for(int i = 0; i < searchList.size(); i++)
                    {
                        Food food = new Food(foodlist[i],R.drawable.food);
                        if(food.getFood_name().contains(newText))
                        {
                            findList.add(food);
                        }
                    }
                    findAdapter = new FoodAdapter(FoodMenu.this, foodadapter.resourceId, findList);
                    findAdapter.notifyDataSetChanged();
                    listView.setAdapter(findAdapter);
                }
                return true;
            }
        });
        Log.i(TAG,"NO BACK LOGIN OK?");
        onBackPressed();
        Log.i(TAG,"NO BACK LOGIN OK?2");
    }
    ////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onBackPressed() {
        super.onBackPressed();//这句话一定要注销，不然又去调用默认的back处理方式了
        Log.i(TAG,"NO BACK LOGIN OK?");
//        Intent intent=new Intent(FoodMenu.this,MainInterface.class);
//        startActivity(intent);
//        finish();

    }
    public void initFoods(String colunmname){
        List<String> fl = mFoodDataManager.showFoodName(colunmname);
        String[] foodlist = new String[fl.size()];
        fl.toArray(foodlist);
        for (int i = 0;i<foodlist.length;i++){
            Log.i(TAG,foodlist[i]);
            Food food = new Food(foodlist[i],R.drawable.food);
            foodList.add(food);
        }
        //return  foodlist;
       // List<String> f1 =mFoodDataManager.showFoodName("Food_Name");
        //数组转list

    }
    public class Food{
        private String food_name;
        private int food_id;
        public Food(String food_name, int food_id){
            this.food_name = food_name;
            this.food_id = food_id;
        }
        public String getFood_name(){
            return food_name;
        }
        public int getFood_id(){
            return food_id;
        }
    }
    public class FoodAdapter extends ArrayAdapter<Food>{
        private int resourceId;
        public FoodAdapter(Context context, int textViewResourceId, List<Food> objects){
            super(context,textViewResourceId,objects);
            resourceId = textViewResourceId;
        }
        @Override
        public View getView(int position, View converView, ViewGroup parent){
            Food food = getItem(position);
            View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            ImageView foodImage = (ImageView) view.findViewById(R.id.food_image);
            TextView foodName = (TextView)view.findViewById(R.id.food_name);
            foodImage.setImageResource(food.getFood_id());
            foodName.setText(food.getFood_name());
            return  view;
        }

    }

//    public void init_menu(){
//        Log.i(TAG,"create");
//        String foodname;
//        double kcal = 1;
//        double CHO = 1;
//        double fats = 1;
//        double protein = 1;
//        for(int i = 0;i<foodlist.length;i++){
//            foodname = foodlist[i];
//            FoodData mFoodData = new FoodData(foodname,kcal,protein,fats,CHO);
//            mFoodDataManager.openDataBase();
//            long flag = mFoodDataManager.insertFoodData(mFoodData);
//            if (flag == -1) {
//                Toast.makeText(this, "初始化失败，请重试", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "初始化成功", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//}
}
