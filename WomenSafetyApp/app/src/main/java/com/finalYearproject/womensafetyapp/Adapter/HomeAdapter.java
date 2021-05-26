package com.finalYearproject.womensafetyapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.finalYearproject.womensafetyapp.R;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    String TAG = HomeAdapter.class.getSimpleName();
    private Context context;
    private int[] logos;

    private List<String> list;
    int[] bLogos = {R.drawable.add, R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add};
    String[] colorArray = {"#952B60","#F9521E", "#2FF3E0","#8A6FDF","#CBAE11","#A8E10C","#008D7C","#2FF3E0"};



    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView helper_name;
        private ImageView helper_image_view;
        private LinearLayout background_linear;

        public MyViewHolder(View view) {
            super(view);
            helper_name = (TextView) itemView.findViewById(R.id.watchman_menu_category);
            helper_image_view = (ImageView) itemView.findViewById(R.id.watchman_menu_image);
            background_linear = (LinearLayout) itemView.findViewById(R.id.background_linear);
        }
    }

    public HomeAdapter(Context context, List<String> list){//,DoctorHomeActvity.DoctorHomeAdapterCallback callback) {
        this.context = context;
        this.list = list;
    }

    @Override
    public HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_home, parent, false);
        return new HomeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final HomeAdapter.MyViewHolder holder, int position) {

        try {

            holder.helper_name.setText(list.get(position));
            int cImage = bLogos[position];
            holder.helper_image_view.setImageResource(cImage);
            holder.background_linear.setBackgroundColor(Color.parseColor(colorArray[position]));

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}




