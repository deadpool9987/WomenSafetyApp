package com.finalYearproject.womensafetyapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.finalYearproject.womensafetyapp.Model.FirstAidModel;
import com.finalYearproject.womensafetyapp.Model.SelfDefenceModel;
import com.finalYearproject.womensafetyapp.R;

import java.util.List;

public class SelfDefenceAdapter extends RecyclerView.Adapter<SelfDefenceAdapter.MyViewHolder> {

    String TAG = SelfDefenceAdapter.class.getSimpleName();
    private Context context;
    private List<SelfDefenceModel> list;
    //private VisitorsList.VisitorsListCallBackForcall callBack;



    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView self_defence_category;
        public MyViewHolder(View view) {
            super(view);

            self_defence_category = (TextView) itemView.findViewById(R.id.self_defence_category);
        }
    }

    public SelfDefenceAdapter(Context context, List<SelfDefenceModel> list){//,DoctorHomeActvity.DoctorHomeAdapterCallback callback) {
        this.context = context;
        this.list = list;
        //this.callBack = callBack;
        //this.btn_puch_outCallBack = btn_puch_outCallBack;

    }

    @Override
    public SelfDefenceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_self_defence, parent, false);
        return new SelfDefenceAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SelfDefenceAdapter.MyViewHolder holder, int position) {

        try {

            final SelfDefenceModel obj = list.get(position);

            holder.self_defence_category.setText(obj.getSelf_defence_name());


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}



