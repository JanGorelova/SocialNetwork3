package com.exam.util;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class NameNormalizerTest {
    @Test
    public void normalize() throws Exception {
        String kate = NameNormalizer.normalize("КаТеРина");
        assertThat(kate, is("Катерина"));
        String piter = NameNormalizer.multiNormalize("Санкт-петербург");
        assertThat(piter, is("Санкт-Петербург"));
    }

    @Test
    public void multiNormalize() throws Exception {
        String fio = NameNormalizer.multiNormalize("пЕтя пЕтРОв");
        assertThat(fio, is("Петя Петров"));
        String city = NameNormalizer.multiNormalize("Санкт  Петербург");
    }
}