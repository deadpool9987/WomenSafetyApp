package com.finalYearproject.womensafetyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.finalYearproject.womensafetyapp.Adapter.FirstAidAdapter;
import com.finalYearproject.womensafetyapp.Adapter.SelfDefenceAdapter;
import com.finalYearproject.womensafetyapp.ApiConstants.ApiUrls;
import com.finalYearproject.womensafetyapp.Model.FirstAidModel;
import com.finalYearproject.womensafetyapp.Model.SelfDefenceModel;
import com.finalYearproject.womensafetyapp.util.AlertDialogClassCommon;
import com.finalYearproject.womensafetyapp.util.AppController;
import com.finalYearproject.womensafetyapp.util.ItemClickSupport;
import com.finalYearproject.womensafetyapp.util.JSONParser;
import com.finalYearproject.womensafetyapp.util.ParseVolleyError;
import com.mlsdev.animatedrv.AnimatedRecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SelfDefenceActivity extends AppCompatActivity {

    private TextView title;
    private Context mContext;
    private float cornerRadius = (float) 15.0;
    private RecyclerView admin_home_list;
    private String fCMTokenSTring;
    private String android_id;
    SweetAlertDialog pDialog;
    ArrayList<SelfDefenceModel> selfDefenceDataArray = new ArrayList();
    public static final String TAG = FirstAidActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_defence);
        mContext = this;
        title = findViewById(R.id.title_text);
        title.setText("Self Defence");
        pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
        //registerDeviceToken();
        admin_home_list = (AnimatedRecyclerView) findViewById(R.id.watchman_menu_list);
        getSelfDefenceList();

        ItemClickSupport.addTo(admin_home_list).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                try
                {
                    SelfDefenceModel selfDefenceData = selfDefenceDataArray.get(position);

                    if(selfDefenceData != null)
                    {
                        Intent i = new Intent(mContext.getApplicationContext(), WebViewActivity.class);
                        i.putExtra("Video", selfDefenceData.getSelf_defence_video());
                        i.putExtra("Instruction", selfDefenceData.getSelf_defence_instruction());
                        mContext.startActivity(i);
                    }

                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        });
    }

    private void getSelfDefenceList(){
        pDialog.getProgressHelper();
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.GET,
                ApiUrls.getSelfDefenceList, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonobj = new JSONObject(response.toString());
                    //JSONObject jsonobj = new JSONObject(response);
                    pDialog.hide();
                    if (jsonobj.has("error")) {

                        boolean error = jsonobj.getBoolean("error");

                        if (error == false) {
                            selfDefenceDataArray = JSONParser.parseSelfDefenceModel(response.toString());
                            if (selfDefenceDataArray != null){
                                //VisitorListAdapter adapterCourier = new VisitorListAdapter(mContext,userDataInfonew BtnClickListener());
                                SelfDefenceAdapter apterVisitor = new SelfDefenceAdapter(getApplicationContext(), selfDefenceDataArray);
                                admin_home_list.setAdapter(apterVisitor);
                                apterVisitor.notifyDataSetChanged();
                                admin_home_list.scheduleLayoutAnimation();
                            }
                            else{
                                String errorMsg = jsonobj.getString("message");
                                AlertDialogClassCommon.showDialogWarning(mContext,errorMsg);
                            }

                            //recyclerView.setAdapter(adapterCourier);
                        } else {
                            String errorMsg = jsonobj.getString("error_msg");
                            AlertDialogClassCommon.showDialogError(mContext,errorMsg);
                        }
                    }

                } catch (Exception ex) {
                    pDialog.hide();
                    ex.printStackTrace();
                    AlertDialogClassCommon.showDialogError(mContext,"Error at parsing data");
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
                String message = ParseVolleyError.ParseVolleyError(error);
                AlertDialogClassCommon.showDialogError(mContext,message);
            }

        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                SharedPreferences shared = mContext.getSharedPreferences("MyPreferences", MODE_PRIVATE);
                String userID = ((shared.getString("user_id","")));
                String authToken = ((shared.getString("auth_token","")));
                map.put("userid", userID);
                map.put("authtoken",authToken);
                return map;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, TAG);

    }
}