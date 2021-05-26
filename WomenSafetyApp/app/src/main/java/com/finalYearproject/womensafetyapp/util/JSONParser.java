package com.finalYearproject.womensafetyapp.util;

import com.finalYearproject.womensafetyapp.Model.FirstAidModel;
import com.finalYearproject.womensafetyapp.Model.SelfDefenceModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONParser {

    static String TAG = com.finalYearproject.womensafetyapp.util.JSONParser.class.getSimpleName();

    public static ArrayList<FirstAidModel> parseFirstAidSafety(String jsonString) {

        ArrayList<FirstAidModel> userListData = new ArrayList<>();


        if (jsonString != null) {

            try {

                JSONObject rootjsonobj = new JSONObject(jsonString);
                JSONArray jsonarr = rootjsonobj.getJSONArray("data");
                //JSONArray jsonarr = new JSONArray(jsonString);

                if (jsonarr != null) {

                    //JSONArray jsonarr = jso.getJSONArray("Couriers");

                    if (jsonarr != null) {
                        if (jsonarr.length() > 0) {

                            for (int i = 0; i < jsonarr.length(); i++) {

                                JSONObject jsonobj = jsonarr.getJSONObject(i);

                                FirstAidModel userInfo = new FirstAidModel();


                                if (jsonobj.has("safety_measure_id")) {
                                    if (!jsonobj.isNull("safety_measure_id")) {
                                        userInfo.setSafety_measure_id(jsonobj.getString("safety_measure_id"));
                                    }
                                }

                                if (jsonobj.has("safety_measure_name")) {
                                    if (!jsonobj.isNull("safety_measure_name")) {
                                        userInfo.setSafety_measure_name(jsonobj.getString("safety_measure_name"));
                                    }
                                }

                                if (jsonobj.has("safety_measure_instruction")) {
                                    if (!jsonobj.isNull("safety_measure_instruction")) {
                                        userInfo.setSafety_measure_instruction(jsonobj.getString("safety_measure_instruction"));
                                    }
                                }

                                if (jsonobj.has("safety_measure_youtube_id")) {
                                    if (!jsonobj.isNull("safety_measure_youtube_id")) {
                                        userInfo.setSafety_measure_youtube_id(jsonobj.getString("safety_measure_youtube_id"));
                                    }
                                }

                                userListData.add(userInfo);

                            }

                        }
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return userListData;
    }

    public static ArrayList<SelfDefenceModel> parseSelfDefenceModel(String jsonString) {

        ArrayList<SelfDefenceModel> userListData = new ArrayList<>();


        if (jsonString != null) {

            try {

                JSONObject rootjsonobj = new JSONObject(jsonString);
                JSONArray jsonarr = rootjsonobj.getJSONArray("data");
                //JSONArray jsonarr = new JSONArray(jsonString);

                if (jsonarr != null) {

                    //JSONArray jsonarr = jso.getJSONArray("Couriers");

                    if (jsonarr != null) {
                        if (jsonarr.length() > 0) {

                            for (int i = 0; i < jsonarr.length(); i++) {

                                JSONObject jsonobj = jsonarr.getJSONObject(i);

                                SelfDefenceModel userInfo = new SelfDefenceModel();


                                if (jsonobj.has("self_defence_id")) {
                                    if (!jsonobj.isNull("self_defence_id")) {
                                        userInfo.setSelf_defence_id(jsonobj.getString("self_defence_id"));
                                    }
                                }

                                if (jsonobj.has("self_defence_instruction")) {
                                    if (!jsonobj.isNull("self_defence_instruction")) {
                                        userInfo.setSelf_defence_instruction(jsonobj.getString("self_defence_instruction"));
                                    }
                                }

                                if (jsonobj.has("self_defence_name")) {
                                    if (!jsonobj.isNull("self_defence_name")) {
                                        userInfo.setSelf_defence_name(jsonobj.getString("self_defence_name"));
                                    }
                                }

                                if (jsonobj.has("self_defence_video")) {
                                    if (!jsonobj.isNull("self_defence_video")) {
                                        userInfo.setSelf_defence_video(jsonobj.getString("self_defence_video"));
                                    }
                                }

                                userListData.add(userInfo);

                            }

                        }
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return userListData;
    }
}