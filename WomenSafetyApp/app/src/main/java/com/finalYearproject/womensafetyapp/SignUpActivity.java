package com.finalYearproject.womensafetyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.finalYearproject.womensafetyapp.ApiConstants.ApiUrls;
import com.finalYearproject.womensafetyapp.util.AlertDialogClassCommon;
import com.finalYearproject.womensafetyapp.util.AppController;
import com.finalYearproject.womensafetyapp.util.Validator;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SignUpActivity extends AppCompatActivity {

    private Context mContext;
    private EditText user_name,user_mobile_number,user_aadhar_number,user_email,user_password,user_emergency_contact;
    private Button btn_submit;
    private ImageView profile_pic;
    private ImageView outside_image_view;
    SweetAlertDialog pDialog;
    private static final String TAG = SignUpActivity.class.getSimpleName();
    private static final int CAMERA_REQUEST = 1888;
    Bitmap bitmap;
    boolean check = true;
    String userID;
    String authToken;
    private TextView title;
    String userName,userMobileNumber,userAadharNumber,userEmail,userPassword,userEmergencyContact = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mContext = this;
        user_name = (EditText) findViewById(R.id.user_name);
        user_mobile_number = (EditText) findViewById(R.id.user_mobile_number);
        user_aadhar_number = (EditText) findViewById(R.id.user_aadhar_number);
        user_email = (EditText) findViewById(R.id.user_email);
        user_password = (EditText) findViewById(R.id.user_password);
        user_emergency_contact = (EditText) findViewById(R.id.user_emergency_contact);
        profile_pic = (ImageView) findViewById(R.id.profile_pic);
        outside_image_view = (ImageView) findViewById(R.id.outside_imageview);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        title = findViewById(R.id.title_text);
        title.setText("Register User");
        pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
        SharedPreferences shared = mContext.getSharedPreferences("MyPreferences", MODE_PRIVATE);
        userID = ((shared.getString("user_id","")));
        authToken = ((shared.getString("auth_token","")));
        outside_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateSubmit();
            }
        });
    }

    private void openCamera(){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    //to close keyboard
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            outside_image_view.setVisibility(View.GONE);
            bitmap = (Bitmap) data.getExtras().get("data");
            profile_pic.setImageBitmap(bitmap);

        }
    }


    private void validateSubmit(){
        if(profile_pic.getDrawable() == null){
            AlertDialogClassCommon.showDialogError(mContext,"Please add profile pic");
        }
        else if(user_name.getText().toString().trim().equals("")){
            AlertDialogClassCommon.showDialogError(mContext,"Enter Vendor Name");
        }
        else if(user_mobile_number.getText().toString().trim().equals("")){
            AlertDialogClassCommon.showDialogError(mContext,"Enter Mobile No");
        }
        else if(user_aadhar_number.getText().toString().trim().equals("")){
            AlertDialogClassCommon.showDialogError(mContext,"Enter Aadhar Number");
        }
        else if(user_email.getText().toString().trim().equals("")){
            AlertDialogClassCommon.showDialogError(mContext,"Enter Email Address");
        }
        else if(!Validator.IsEmail(user_email.getText().toString().trim())){
            AlertDialogClassCommon.showDialogError(mContext,"Enter Valid Email");
        }
        else if (user_password.getText().toString().trim().equals("")){
            AlertDialogClassCommon.showDialogError(mContext,"Enter Password");
        }
        else if ((user_mobile_number.getText().toString().trim().length() > 10) || (user_mobile_number.getText().toString().trim().length() < 10)){
            AlertDialogClassCommon.showDialogError(mContext,"Enter Valid Mobile Number");
        }
        else if ((user_emergency_contact.getText().toString().trim().length() > 10) || (user_emergency_contact.getText().toString().trim().length() < 10)){
            AlertDialogClassCommon.showDialogError(mContext,"Enter Valid Mobile Number");
        }
        else if(!validateAadharNumber(user_aadhar_number.getText().toString().trim())){
            AlertDialogClassCommon.showDialogError(mContext,"Enter Valid Aadhar Number");
        }
        else{
            userName = user_name.getText().toString().trim();
            userMobileNumber = user_mobile_number.getText().toString().trim();
            userAadharNumber = user_aadhar_number.getText().toString().trim();
            userEmail = user_email.getText().toString().trim();
            userPassword = user_password.getText().toString().trim();
            userEmergencyContact = user_emergency_contact.getText().toString().trim();
            checkUserEmail();

        }
    }

    private void checkUserEmail(){
        pDialog.getProgressHelper();
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                ApiUrls.checkIfEmailExist, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    pDialog.dismissWithAnimation();
                    JSONObject jsonobj = new JSONObject(response.toString());
                    Boolean error = jsonobj.getBoolean("error");
                    String message = jsonobj.getString("message");
                    if (error == true){
                        createVendor();
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
                params.put("user_email",user_email.getText().toString().trim());
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

    private void createVendor(){

        ByteArrayOutputStream byteArrayOutputStreamObject ;

        byteArrayOutputStreamObject = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                pDialog.getProgressHelper();
                pDialog.setTitleText("Loading");
                pDialog.setCancelable(false);
                pDialog.show();
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                // Dismiss the progress dialog after done uploading.
                pDialog.hide();

                try {
                    JSONObject rootjsonobj = new JSONObject(string1.toString());
                    if (rootjsonobj.has("error")) {

                        boolean error = rootjsonobj.getBoolean("error");

                        if (error == false) {
                            String errorMsg = rootjsonobj.getString("message");
                            AlertDialogClassCommon.showSuccesMessage(mContext,errorMsg);
                            finish();
                        }
                        else{
                            String errorMsg = rootjsonobj.getString("message");
                            AlertDialogClassCommon.showDialogError(mContext,errorMsg);
                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    AlertDialogClassCommon.showDialogError(mContext,"Exception Occured.");

                }
                Log.e(TAG, string1);
                //profile_pic.setImageResource(android.R.color.transparent);


            }

            @Override
            protected String doInBackground(Void... params) {

                SignUpActivity.ImageProcessClass imageProcessClass = new SignUpActivity.ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                HashMapParams.put("user_name", userName);
                HashMapParams.put("user_mobile_number", userMobileNumber);
                HashMapParams.put("user_aadhar_number", userAadharNumber);
                HashMapParams.put("user_email", userEmail);
                HashMapParams.put("user_password", userPassword);
                HashMapParams.put("user_emergency_contact", userEmergencyContact);
                HashMapParams.put("image_path", ConvertImage);

                String FinalData = imageProcessClass.ImageHttpRequest(ApiUrls.signUp, HashMapParams);

                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

        AsyncTaskUploadClassOBJ.execute();
    }

    public class ImageProcessClass{

        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {

                URL url;
                HttpURLConnection httpURLConnectionObject ;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject ;
                BufferedReader bufferedReaderObject ;
                int RC ;

                url = new URL(requestURL);

                httpURLConnectionObject = (HttpURLConnection) url.openConnection();

                httpURLConnectionObject.setReadTimeout(19000);

                httpURLConnectionObject.setConnectTimeout(19000);

                httpURLConnectionObject.setRequestMethod("POST");

                httpURLConnectionObject.setDoInput(true);

                httpURLConnectionObject.setDoOutput(true);

//                httpURLConnectionObject.setRequestProperty("userid",userID);
//
//                httpURLConnectionObject.setRequestProperty("authtoken",authToken);
                OutPutStream = httpURLConnectionObject.getOutputStream();

                bufferedWriterObject = new BufferedWriter(

                        new OutputStreamWriter(OutPutStream, "UTF-8"));

                bufferedWriterObject.write(bufferedWriterDataFN(PData));

                bufferedWriterObject.flush();

                bufferedWriterObject.close();

                OutPutStream.close();

                RC = httpURLConnectionObject.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReaderObject.readLine()) != null){

                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        AlertDialogClassCommon.showDialogError(mContext,"Please add profile pic");
                    }
                });
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            StringBuilder stringBuilderObject;

            stringBuilderObject = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {

                if (check)

                    check = false;
                else
                    stringBuilderObject.append("&");

                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilderObject.append("=");

                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilderObject.toString();
        }

    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    public static boolean validateAadharNumber(String aadharNumber){
        Pattern aadharPattern = Pattern.compile("\\d{12}");
        boolean isValidAadhar = aadharPattern.matcher(aadharNumber).matches();
        if(isValidAadhar){
            isValidAadhar = VerhoeffAlgorithm.validateVerhoeff(aadharNumber);
        }
        return isValidAadhar;
    }
}

class VerhoeffAlgorithm{
    static int[][] d  = new int[][]
            {
                    {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
                    {1, 2, 3, 4, 0, 6, 7, 8, 9, 5},
                    {2, 3, 4, 0, 1, 7, 8, 9, 5, 6},
                    {3, 4, 0, 1, 2, 8, 9, 5, 6, 7},
                    {4, 0, 1, 2, 3, 9, 5, 6, 7, 8},
                    {5, 9, 8, 7, 6, 0, 4, 3, 2, 1},
                    {6, 5, 9, 8, 7, 1, 0, 4, 3, 2},
                    {7, 6, 5, 9, 8, 2, 1, 0, 4, 3},
                    {8, 7, 6, 5, 9, 3, 2, 1, 0, 4},
                    {9, 8, 7, 6, 5, 4, 3, 2, 1, 0}
            };
    static int[][] p = new int[][]
            {
                    {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
                    {1, 5, 7, 6, 2, 8, 3, 0, 9, 4},
                    {5, 8, 0, 3, 7, 9, 6, 1, 4, 2},
                    {8, 9, 1, 6, 0, 4, 3, 5, 2, 7},
                    {9, 4, 5, 3, 1, 2, 6, 8, 7, 0},
                    {4, 2, 8, 6, 5, 7, 3, 9, 0, 1},
                    {2, 7, 9, 3, 8, 0, 6, 4, 1, 5},
                    {7, 0, 4, 6, 9, 1, 3, 2, 5, 8}
            };
    static int[] inv = {0, 4, 3, 2, 1, 5, 6, 7, 8, 9};

    public static boolean validateVerhoeff(String num){
        int c = 0;
        int[] myArray = StringToReversedIntArray(num);
        for (int i = 0; i < myArray.length; i++){
            c = d[c][p[(i % 8)][myArray[i]]];
        }

        return (c == 0);
    }
    private static int[] StringToReversedIntArray(String num){
        int[] myArray = new int[num.length()];
        for(int i = 0; i < num.length(); i++){
            myArray[i] = Integer.parseInt(num.substring(i, i + 1));
        }
        myArray = Reverse(myArray);
        return myArray;
    }
    private static int[] Reverse(int[] myArray){
        int[] reversed = new int[myArray.length];
        for(int i = 0; i < myArray.length ; i++){
            reversed[i] = myArray[myArray.length - (i + 1)];
        }
        return reversed;
    }
}
