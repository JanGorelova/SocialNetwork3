package com.exam.logic.services;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by Vasiliy Bobkov on 15.03.2017.
 */
public class ValidatorTest {
    @Test
    public void validateImage() throws Exception {

    }

    @Test
    public void validateLoginForm() throws Exception {

    }

    @Test
    public void validateRegistration() throws Exception {

    }

    @Test
    public void validateProfile() throws Exception {
        Validator.ValidCode code = Validator.validateProfile("+7931", "2001-12-01", "Россия", "Санкт-Петербург", "Спбпу", "Жизнерадостный");
        assertTrue(code == Validator.ValidCode.INVALID_TELEPHONE);
        code = Validator.validateProfile("+79313743405", "2001- 12-01", "Россия", "Санкт-Петербург", "Спбпу", "Жизнерадостный");
        assertTrue(code == Validator.ValidCode.INVALID_BIRTHDAY);
        code = Validator.validateProfile("+79313743405", "2001-12-01", "d", "Санкт-Петербург", "Спбпу", "Жизнерадостный");
        assertTrue(code == Validator.ValidCode.INVALID_COUNTRY);
        code = Validator.validateProfile("+79313743405", "2001-12-01", "Россия", "d*", "Спбпу", "Жизнерадостный");
        assertTrue(code == Validator.ValidCode.INVALID_CITY);
        code = Validator.validateProfile("+79313743405", "2001-12-01", "Россия", "Санкт-Петербург", "*/asd", "Жизнерадостный");
        assertTrue(code == Validator.ValidCode.INVALID_UNIVERSITY);
        code = Validator.validateProfile("+79313743405", "2001-12-01", "Россия", "Санкт-Петербург", "Спбпу", "/*/sad");
        assertTrue(code == Validator.ValidCode.INVALID_ABOUT);
        code = Validator.validateProfile("+79313743405", "2001-12-01", "Россия", "Санкт-Петербург", "Спбпу", "Жизнерадостный");
        assertTrue(code == Validator.ValidCode.SUCCESS);
    }
}