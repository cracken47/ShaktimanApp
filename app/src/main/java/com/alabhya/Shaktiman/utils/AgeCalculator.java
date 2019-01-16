package com.alabhya.Shaktiman.utils;

import java.util.Calendar;

public class AgeCalculator {

    public AgeCalculator() {
    }

    public String getAge(String DateOfBirth){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        int year = Integer.parseInt(DateOfBirth.substring(0,4));
        int month = Integer.parseInt(DateOfBirth.substring(5,7));
        int day = Integer.parseInt(DateOfBirth.substring(8));
        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }
}
