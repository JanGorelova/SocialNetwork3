package com.exam.util;

/**
 * Created by Vasiliy Bobkov on 21.11.2016.
 */
public abstract class NameNormalizer {
    public static String normalize(String name) {
        name = name.toLowerCase();
        String[] subNames = name.split("-");
        name = Character.toString(subNames[0].charAt(0)).toUpperCase() + subNames[0].substring(1);
        for (int i = 1; i < subNames.length; i++) {
            name += "-" + Character.toString(subNames[i].charAt(0)).toUpperCase() + subNames[i].substring(1);
        }
        return name;
    }
    public static String multiNormalize(String name){
        String out="";
        for (String subName:name.split(" ")){
            out+=normalize(subName)+" ";
        }
        return out.trim();
    }
}