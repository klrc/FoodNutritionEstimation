package com.example.camera;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class UserAdapter extends ArrayAdapter<UserInformation> {
    private List<UserInformation> mUserInformationList;
    private int resourceId;
    private LayoutInflater inlfater = null;

    public UserAdapter(Context context, int textViewResourceId, List<UserInformation> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        inlfater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserInformation user = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        ImageView userImage = (ImageView) view.findViewById(R.id.user_image);
        TextView userName = (TextView) view.findViewById(R.id.user_name);
        userImage.setImageResource(user.getImageId());

        userName.setText(user.getName());
        return view;
    }
//    public void updataView(int posi, ListView listView) {
//        int visibleFirstPosi = listView.getFirstVisiblePosition();
//        int visibleLastPosi = listView.getLastVisiblePosition();
//        if (posi >= visibleFirstPosi && posi <= visibleLastPosi) {
//            View view = listView.getChildAt(posi - visibleFirstPosi);
//            RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) view.getTag();
//
//            String txt = holder.strText.getText().toString();
//            txt = txt + "++;";
//            holder.strText.setText(txt);
//            strList.set(posi, txt);
//        } else {
//            String txt = strList.get(posi);
//            txt = txt + "++;";
//            strList.set(posi, txt);
//        }
//    }
}
//    static class ViewHolder extends RecyclerView.ViewHolder{
//            ImageView userImage;
//            TextView userName;
//            public ViewHolder(View view){
//                super(view);
//                userImage = (ImageView)view.findViewById(R.id.user_image);
//                userName = (TextView)view.findViewById(R.id.user_name);
//
//            }
//    }
//    public UserAdapter(Login login, int user_item, List<UserInformation> userInformationList){
//        mUserInformationList = userInformationList;
//    }
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);
//        ViewHolder holder = new ViewHolder(view);
//        return holder;
//    }
//    @Override
//    public void onBindViewHolder(ViewHolder holder,int position){
//        UserInformation uinfo = mUserInformationList.get(position);
//        holder.userImage.setImageResource(uinfo.getImageId());
//        holder.userName.setText(uinfo.getName());
//    }
//    @Override
//    public int getItemCount(){
//        return mUserInformationList.size();
//    }



