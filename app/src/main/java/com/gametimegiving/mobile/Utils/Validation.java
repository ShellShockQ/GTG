package com.gametimegiving.mobile.Utils;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    static final String TAG = "Validation";
    static final String PASSWORD_PATTERN = "^.{8,12}$";
    static String regexMMDDYYYY = "^([1-9]|1[012])[/](0[1-9]|[12][0-9]|3[01])[/](19|20)\\d\\d$";
    static String regexDDMMYYYY = "^(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d$";
    static String usrNamePtrn = "^[a-z0-9._-]{2,25}$";
    static String FirstNamePtrn = "[a-zA-z]+([ '-][a-zA-Z]+)*";
    static String LastNamePtrn = "[a-zA-z]+([ '-][a-zA-Z]+)*";
    static String mobilePtrn = "[7-9][0-9]{9}$";

    // return true if the input field is valid, based on the parameter passed
    public static boolean isFieldEmpty(EditText ed) {
        if (ed != null) {
            String uname = ed.getText().toString().trim();
            if (uname.equals("") || uname.length() <= 0)
                return false;
        }
        return true;
    }

    // Begins with 0, +91 or 0091

    public static boolean isMobileValid(String mobile) {
        if (Pattern.matches(mobilePtrn, mobile)) {
            if (mobile.length() < 11 && mobile.length() > 9) {
                return false;
            }
            return false;
        }

        return true;

    }

    // Regular Expression
    // you can change the expression based on your need
    public static boolean isEmailValid(String mail) {

        String expression = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern p = Pattern.compile(expression, Pattern.CASE_INSENSITIVE); // pattern=/^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\.([a-zA-Z])+([a-zA-Z])+/;

        Matcher m = p.matcher(mail);
        return !(m.matches() && mail.trim().length() > 0);

    }

    public static boolean isPasswordMatch(EditText password, EditText c_password) {

        String pass = password.getText().toString().trim();
        String c_pass = c_password.getText().toString().trim();
        return !pass.equals(c_pass);
    }

    public static boolean isAValidDDMMYYYYDate(EditText editTextDOB) {
        String dob = editTextDOB.getText().toString().trim();
        return !Pattern.matches(regexMMDDYYYY, dob);
    }

    /*	Be between 8 and 40 characters
        Contain at least one digit.
        Contain at least one lower case character.
    */
    public static boolean isPasswordValid(EditText password) {
        String pass = password.getText().toString().trim();
        return !Pattern.matches(PASSWORD_PATTERN, pass);

    }

    /*	Between 2 and 25 characters long.
        We want to contain characters, numbers and the ., -, _ symbols.*/
    public static boolean validateUserName(EditText userName) {
        String Uname = userName.getText().toString().trim();
        return !Pattern.matches(usrNamePtrn, Uname);
    }

    public static boolean validateFirstName(EditText firstName) {
        String Fname = firstName.getText().toString().trim();
        return !Pattern.matches(FirstNamePtrn, Fname);
    }

    public static boolean validateLastName(EditText lastName) {
        String Lname = lastName.getText().toString().trim();
        return !Pattern.matches(LastNamePtrn, Lname);
    }


}