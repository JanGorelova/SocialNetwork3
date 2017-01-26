package com.exam.logic.services;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.imageio.ImageReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Validator {

    public static ValidCode validateImage(ImageReader reader) throws IOException {
        if ((reader.getWidth(0) < 200) || (reader.getHeight(0) < 200)) return ValidCode.INVALID_PIXEL_SIZE;
        return ValidCode.SUCCESS;
    }

    @RequiredArgsConstructor
    public enum ValidCode {
        INVALID_EMAIL("error.invalid.email"),
        INVALID_PASSWORD("error.invalid.password"),
        INVALID_FIRST_NAME("error.invalid.firstName"),
        INVALID_LAST_NAME("error.invalid.lastName"),
        INVALID_PASSWORD_CONFIRM("error.invalid.password.confirm"),
        INVALID_BIRTHDAY("error.invalid.birthday"),
        INVALID_UNIVERSITY("error.invalid.university"),
        INVALID_CITY("error.invalid.city"),
        INVALID_COUNTRY("error.invalid.country"),
        INVALID_TELEPHONE("error.invalid.telephone"),
        INVALID_ABOUT("error.invalid.about"),
        INVALID_PIXEL_SIZE("error.invalid.pixelSize"),
        SUCCESS("success");
        @Getter
        private final String propertyName;
    }

    public static ValidCode validateLoginForm(String email, String password) {
        if (!isValidEmail(email)) return ValidCode.INVALID_EMAIL;
        if (!isValidPassword(password)) return ValidCode.INVALID_PASSWORD;
        return ValidCode.SUCCESS;
    }

    private static boolean isValidEmail(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    private static boolean isValidPassword(String password) {
//        ^                 # start-of-string
//        (?=.*[0-9])       # a digit must occur at least once
//        (?=.*[a-z])       # a lower case letter must occur at least once
//        (?=.*[A-Z])       # an upper case letter must occur at least once
//        (?=\S+$)          # no whitespace allowed in the entire string
//        .{8,16}             # anything, at least eight places though
//        $                 # end-of-string
        String pPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,16}$";
        Pattern p = Pattern.compile(pPattern);
        Matcher m = p.matcher(password);
        return m.matches();
    }

    public static ValidCode validateRegistration(String firstName, String lastName, String email, String password, String passwordConfirm) {
        if (!isValidName(firstName)) return ValidCode.INVALID_FIRST_NAME;
        if (!isValidName(lastName)) return ValidCode.INVALID_LAST_NAME;
        if (!isValidEmail(email)) return ValidCode.INVALID_EMAIL;
        if (!isValidPassword(password)) return ValidCode.INVALID_PASSWORD;
        if (!password.equals(passwordConfirm)) return ValidCode.INVALID_PASSWORD_CONFIRM;
        return ValidCode.SUCCESS;
    }

    private static boolean isValidName(String name) {
        int MAX_CHARS_IN_NAME = 17;
        String nPattern = "^([^\\s]*[a-zA-Z\\u0430-\\u044f\\u0451\\u0410-\\u042f\\u0401\\-]){2,}$";
        Pattern p = Pattern.compile(nPattern);
        Matcher m = p.matcher(name);
        return m.matches() && name.length() < MAX_CHARS_IN_NAME;
    }

    public static ValidCode validateProfile(String telephone, String birthday, String country, String city, String university, String about) {
        if (!isValidTelephone(telephone)) return ValidCode.INVALID_TELEPHONE;
        if (!isValidBirthday(birthday)) return ValidCode.INVALID_BIRTHDAY;
        if (!isValidText(country, 25)) return ValidCode.INVALID_COUNTRY;
        if (!isValidText(city, 25)) return ValidCode.INVALID_CITY;
        if (!isValidText(university, 50)) return ValidCode.INVALID_UNIVERSITY;
        if (!isValidText(about, 50)) return ValidCode.INVALID_ABOUT;
        return ValidCode.SUCCESS;
    }

    private static boolean isValidTelephone(String telephone) {
        int maxChars = 20;
        if (telephone.length() > maxChars) return false;
        if (telephone.length() == 0) return true;
        String nPattern = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$";
        Pattern p = Pattern.compile(nPattern);
        Matcher m = p.matcher(telephone);
        return m.matches();
    }

    private static boolean isValidText(String text, int maxChars) {
        if (text.length() == 0) return true;
        if (text.length() > maxChars) return false;
        String nPattern = "^([\\s]*[a-zA-Z\\u0430-\\u044f\\u0451\\u0410-\\u042f\\u0401\\-]){2,}$";
        Pattern p = Pattern.compile(nPattern);
        Matcher m = p.matcher(text);
        return m.matches();
    }

    private static boolean isValidBirthday(String birthday) {
        int maxChars = 17;
        int minOldForUsers = 14;
        if (birthday.length() > maxChars) return false;
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(birthday);
        } catch (DateTimeParseException e) {
            return false;
        }
        return !localDate.isAfter(LocalDate.now().minusYears(minOldForUsers));
    }
}