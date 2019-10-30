package com.example.camera;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static org.litepal.LitePalApplication.getContext;

public class ResultOthersAdapter extends ArrayAdapter<ResultShowInfo> {
    private Context context;
    private List<ResultShowInfo> mResultInfolist;
    private LayoutInflater inflater = null;
    private int resourceId;
    private float foods_vol;
    private float foods_weight;

    public ResultOthersAdapter(Context context, int textViewResourceId, List<ResultShowInfo> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ResultShowInfo resultinfo  = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        ImageView resultImage = (ImageView) view.findViewById(R.id.result_food_image);
        TextView resultName = (TextView) view.findViewById(R.id.result_food_name);
        resultImage.setImageResource(resultinfo.getImageId());

        resultName.setText(resultinfo.getName());
        return view;
    }

}
