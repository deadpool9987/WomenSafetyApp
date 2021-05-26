package com.finalYearproject.womensafetyapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.finalYearproject.womensafetyapp.Model.FirstAidModel;
import com.finalYearproject.womensafetyapp.R;

import java.util.List;

public class FirstAidAdapter extends RecyclerView.Adapter<FirstAidAdapter.MyViewHolder> {

    String TAG = FirstAidAdapter.class.getSimpleName();
    private Context context;
    private List<FirstAidModel> list;
    //private VisitorsList.VisitorsListCallBackForcall callBack;



    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView first_aid_category;
        public MyViewHolder(View view) {
            super(view);

            first_aid_category = (TextView) itemView.findViewById(R.id.first_aid_category);
        }
    }

    public FirstAidAdapter(Context context, List<FirstAidModel> list){//,DoctorHomeActvity.DoctorHomeAdapterCallback callback) {
        this.context = context;
        this.list = list;
        //this.callBack = callBack;
        //this.btn_puch_outCallBack = btn_puch_outCallBack;

    }

    @Override
    public FirstAidAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_first_aid, parent, false);
        return new FirstAidAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FirstAidAdapter.MyViewHolder holder, int position) {

        try {

            final FirstAidModel obj = list.get(position);

            holder.first_aid_category.setText(obj.getSafety_measure_name());


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}



