package com.vishnu.gollakaram.dsvtojsonl;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DsvToJsonlConverterTest {

    @Test
    void testConversionCommaWithoutDelimiter() throws IOException {
        testConversion("src/test/resources/test_input_comma.csv", "src/test/resources/expected_output.jsonl", null, false);
    }

    @Test
    void testConversionPipeWithoutDelimiter() throws IOException {
        testConversion("src/test/resources/test_input_pipe.csv", "src/test/resources/expected_output.jsonl", null, false);
    }

    @Test
    void testConversionWithCommaDelimiter() throws IOException {
        testConversion("src/test/resources/test_input_comma.csv", "src/test/resources/expected_output.jsonl", ",", true);
    }

    @Test
    void testConversionWithPipeDelimiter() throws IOException {
        testConversion("src/test/resources/test_input_pipe.csv", "src/test/resources/expected_output.jsonl", "|", true);
    }

    @Test
    void testConversionWithUnderscoreDelimiter() throws IOException {
        testConversion("src/test/resources/test_input_underscore.csv", "src/test/resources/expected_output.jsonl", "_", true);
    }

    private void testConversion(String inputFilePath, String expectedOutputFilePath, String delimiter, boolean testWithDelimiter) throws IOException {
        String actualOutputFilePath = "target/test_output.jsonl";

        if (testWithDelimiter) {
            DsvToJsonlConverter.main(new String[]{inputFilePath, actualOutputFilePath, delimiter});
        } else {
            DsvToJsonlConverter.main(new String[]{inputFilePath, actualOutputFilePath});
        }

        List<String> expectedLines = Files.readAllLines(Paths.get(expectedOutputFilePath));
        List<String> actualLines = Files.readAllLines(Paths.get(actualOutputFilePath));

        assertEquals(expectedLines, actualLines);
    }
}
