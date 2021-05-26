package com.finalYearproject.womensafetyapp.ApiConstants;

public class ApiUrls {

    //static public String ServerURL = "http://chatvis.com/API/";

    static private String ServerURL = "https://womensafetyapp001.000webhostapp.com/ApiCalls/";

    static public String login = ServerURL + "login.php";
    static public String signUp = ServerURL + "registerUser.php";
    static public String checkIfEmailExist = ServerURL + "checkIfMailExist.php";
    static public String forgotPassword = ServerURL + "forgotPassword.php";
    static public String updateUser = ServerURL + "updateUserWithProfile.php";
    static public String getFirstAidList = ServerURL + "getFIrstAidList.php";
    static public String getSelfDefenceList = ServerURL + "getSelfDefenceList.php";

}
