package com.exam.util;

import org.apache.commons.codec.digest.DigestUtils;

public abstract class Security {
    public static String md5Hex(String pass) {
        return DigestUtils.md5Hex(pass);
    }
}
