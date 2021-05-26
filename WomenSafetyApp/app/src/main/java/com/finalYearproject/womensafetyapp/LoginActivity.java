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
import android.widget.TextView;

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

public class LoginActivity extends AppCompatActivity {

    private EditText user_email;
    private EditText user_password;
    private Context mContext;
    private Button btnLogin;
    SweetAlertDialog pDialog;
    private TextView btn_sign_up;
    private Button btn_forgot_password;
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        user_email = (EditText) findViewById(R.id.user_email_id);
        user_password = (EditText) findViewById(R.id.user_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
        btn_sign_up = (TextView) findViewById(R.id.btn_sign_up);
        btn_forgot_password = (Button) findViewById(R.id.btn_forgot_password);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateSignUp();
            }
        });
        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext.getApplicationContext(), SignUpActivity.class);
                mContext.startActivity(i);
            }
        });

        btn_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext.getApplicationContext(), ForgotPasswordActivity.class);
                mContext.startActivity(i);
            }
        });

    }

    private void validateSignUp(){

        if (user_email.getText().toString().equals("")){
            AlertDialogClassCommon.showDialogError(mContext,"Enter Username");
        }
        else if(user_password.getText().toString().trim().equals("")){
            AlertDialogClassCommon.showDialogError(mContext,"Enter Password");
        }
        else {
            loginUser();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    private void loginUser(){
        pDialog.getProgressHelper();
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                ApiUrls.login, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    pDialog.dismissWithAnimation();
                    pDialog.hide();
                    JSONObject jsonobj = new JSONObject(response.toString());
                    Boolean error = jsonobj.getBoolean("error");
                    String message = jsonobj.getString("message");
                    if (error == false){

                        JSONObject dataObject = jsonobj.getJSONObject("data");
                        SharedPreferences shared = mContext.getSharedPreferences("MyPreferences", MODE_PRIVATE);
                        SharedPreferences.Editor editor = shared.edit();
                        editor.putString("user_name",dataObject.getString("user_name"));
                        editor.putString("user_mobile_number",dataObject.getString("user_mobile_number"));
                        editor.putString("user_aadhar_number",dataObject.getString("user_aadhar_number"));
                        editor.putString("user_profile_pic",dataObject.getString("user_profile_pic"));
                        editor.putString("user_email",dataObject.getString("user_email"));
                        editor.putString("user_auth_token",dataObject.getString("user_auth_token"));
                        editor.putString("user_emergency_contact",dataObject.getString("user_emergency_contact"));
                        editor.putString("user_id",dataObject.getString("user_id"));
                        editor.putBoolean("isLoggedIn",true);
                        editor.commit();
                        new SweetAlertDialog(mContext).setTitleText("LoggedIn Successfully").show();
                        Intent i = new Intent(mContext.getApplicationContext(), HomeActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        mContext.startActivity(i);

                    }
                    else{
                        AlertDialogClassCommon.showDialogError(mContext,message);
                    }

                } catch (Exception ex) {
                    pDialog.dismissWithAnimation();
                    pDialog.hide();
                    AlertDialogClassCommon.showDialogError(mContext,ex.toString());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismissWithAnimation();
                pDialog.hide();
            }

        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_email",user_email.getText().toString().trim());
                params.put("user_password",user_password.getText().toString().trim());
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