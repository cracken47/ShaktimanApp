package com.alabhya.Shaktiman.utils;

import android.text.TextUtils;

import java.util.regex.Pattern;

public class Validator {
    public boolean isvalidAadhar(String aadharNumber){
        Pattern aadharPattern = Pattern.compile("\\d{12}");
        boolean isValidAadhar = aadharPattern.matcher(aadharNumber).matches();
        if(isValidAadhar){
            isValidAadhar = AdharValidator.validateVerhoeff(aadharNumber);
        }
        return isValidAadhar;
    }

    public boolean isValidPhone(String phoneNumber){
        Pattern phonePattern = Pattern.compile("\\d{10}");
        boolean isValidPhone = phonePattern.matcher(phoneNumber).matches();
        return isValidPhone;
    }

    public boolean validInput(String input){
        if(input=="" || input==null || TextUtils.isEmpty(input)){
            return false;
        }else return true;
    }

    public boolean isValidPassword(String password){
        Pattern phonePattern = Pattern.compile("^(?=.*\\d)(?=.*[a-zA-Z])(?!.*[\\W_\\x7B-\\xFF]).{6,15}$");
        return phonePattern.matcher(password).matches();
    }
}
