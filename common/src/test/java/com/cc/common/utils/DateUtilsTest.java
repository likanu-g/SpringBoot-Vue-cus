package com.cc.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class DateUtilsTest {

    private String format;

    @BeforeTestClass
    public void init(){
        format = "yyyy-MM-dd";
    }

    @Test
    void testDateTime_yyyyMMdd(){
        String dataTime = DateFormatUtils.format(new Date(), "yyyyMMdd");
        assertEquals(dataTime,DateUtils.dateToDateTimeString());
    }

    @Test
    void testDateTime_YYYY_MM_DD(){
        String  dateTime = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
        assertEquals(dateTime,DateUtils.dateToDateTimeString(new Date()));
    }

    @Test
    void testStringToDate() throws ParseException {
        String  dateTime = "2023-1-5 16:30:30";
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mmm:ss").parse(dateTime);
        assertEquals(date, DateUtils.dateTimeStringToDate("yyyy-MM-dd HH:mmm:ss",dateTime));
    }


}
