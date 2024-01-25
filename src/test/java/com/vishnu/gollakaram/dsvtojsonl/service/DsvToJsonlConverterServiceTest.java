package com.vishnu.gollakaram.dsvtojsonl.service;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DsvToJsonlConverterServiceTest {

    @Test
    void convertDsvToJsonl_shouldConvertDsvToJsonObject() throws IOException, ParseException {
        String dsvInput = "Name,Age,Date\nJohn Doe,25,2022-01-25\nJane Doe,30,2022-01-26";
        List<String> lines = Arrays.asList(dsvInput.split("\n"));

        StringWriter writer = new StringWriter();
        DsvToJsonlConverterService.convertDsvToJsonl(lines, writer, ',');

        String expectedJsonL = "{\"Name\": \"John Doe\",\"Age\": 25,\"Date\": \"2022-01-25\"}\n" +
                "{\"Name\": \"Jane Doe\",\"Age\": 30,\"Date\": \"2022-01-26\"}\n";

        assertEquals(expectedJsonL, writer.toString());
    }

    @Test
    void parseDsvLine_shouldParseDsvLine() {
        String dsvLine = "John Doe,25,2022-01-25";
        String[] result = DsvToJsonlConverterService.parseDsvLine(dsvLine, ',');

        assertEquals("John Doe", result[0]);
        assertEquals("25", result[1]);
        assertEquals("2022-01-25", result[2]);
    }
}
