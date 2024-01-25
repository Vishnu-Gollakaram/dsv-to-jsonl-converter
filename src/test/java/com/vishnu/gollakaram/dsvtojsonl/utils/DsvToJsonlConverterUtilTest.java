package com.vishnu.gollakaram.dsvtojsonl.utils;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DsvToJsonlConverterUtilTest {

    @Test
    void convertToJsonObject_shouldConvertValues() throws ParseException {
        DsvToJsonlConverterUtil util = new DsvToJsonlConverterUtil();
        String[] header = {"Name", "Age", "Date"};
        String[] values = {"John Doe", "25", "2022-01-25"};

        Map<String, Object> jsonObject = util.convertToJsonObject(header, values);

        assertEquals("John Doe", jsonObject.get("Name"));
        assertEquals(25L, jsonObject.get("Age"));
        assertEquals("2022-01-25", jsonObject.get("Date"));
    }

    @Test
    void isValidDate_shouldReturnTrueForValidDate() {
        assertTrue(DsvToJsonlConverterUtil.isValidDate(2022, 1, 25));
    }

    @Test
    void tryParseDate_shouldReturnFormattedDateForValidInput() {
        Optional<Object> result = DsvToJsonlConverterUtil.tryParseDate("2022-01-25");
        assertTrue(result.isPresent());
        assertEquals("2022-01-25", result.get());
    }

    @Test
    void isLong_shouldReturnTrueForValidLong() {
        assertTrue(new DsvToJsonlConverterUtil().isLong("123"));
    }

    @Test
    void isDouble_shouldReturnTrueForValidDouble() {
        assertTrue(new DsvToJsonlConverterUtil().isDouble("123.45"));
    }

    @Test
    void parseValue_shouldReturnParsedValue() {
        DsvToJsonlConverterUtil util = new DsvToJsonlConverterUtil();
        assertEquals(123L, util.parseValue("123"));
        assertEquals(123.45, util.parseValue("123.45"));
        assertEquals("2022-01-25", util.parseValue("2022-01-25"));
        assertEquals("SomeString", util.parseValue("SomeString"));
    }
}