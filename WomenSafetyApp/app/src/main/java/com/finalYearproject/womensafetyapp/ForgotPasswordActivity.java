package com.finalYearproject.womensafetyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.finalYearproject.womensafetyapp.ApiConstants.ApiUrls;
import com.finalYearproject.womensafetyapp.util.AlertDialogClassCommon;
import com.finalYearproject.womensafetyapp.util.AppController;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText user_email_id;
    private Button btn_submit;
    private Context mContext;
    SweetAlertDialog pDialog;
    private static final String TAG = ForgotPasswordActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mContext = this;
        user_email_id = (EditText) findViewById(R.id.user_email_id);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_email_id.getText().toString().trim().equals("")){
                    AlertDialogClassCommon.showDialogError(mContext,"Please Enter registered Email ID");
                }
                else{
                    createNewPassword();
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    private void createNewPassword(){
        pDialog.getProgressHelper();
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                ApiUrls.forgotPassword, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    pDialog.dismissWithAnimation();
                    JSONObject jsonobj = new JSONObject(response.toString());
                    Boolean error = jsonobj.getBoolean("error");
                    String message = jsonobj.getString("message");
                    if (error == false){
                        new SweetAlertDialog(mContext).setTitleText(message).show();
                        Intent i = new Intent(mContext.getApplicationContext(), LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        mContext.startActivity(i);
                    }
                    else{
                        AlertDialogClassCommon.showDialogError(mContext,message);
                    }

                } catch (Exception ex) {
                    pDialog.dismissWithAnimation();
                    AlertDialogClassCommon.showDialogError(mContext,ex.toString());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismissWithAnimation();
            }

        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_email",user_email_id.getText().toString().trim());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                return map;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, TAG);

    }

}