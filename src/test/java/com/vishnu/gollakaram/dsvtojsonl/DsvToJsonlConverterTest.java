package com.vishnu.gollakaram.dsvtojsonl;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DsvToJsonlConverterTest {

    @Test
    void testConversionWithCommaDelimiter() throws IOException {
        testConversion("src/test/resources/test_input_comma.csv", "src/test/resources/expected_output.jsonl");
    }

    @Test
    void testConversionWithPipeDelimiter() throws IOException {
        testConversion("src/test/resources/test_input_pipe.csv", "src/test/resources/expected_output.jsonl");
    }

    private void testConversion(String inputFilePath, String expectedOutputFilePath) throws IOException {
        String actualOutputFilePath = "target/test_output.jsonl";

        DsvToJsonlConverter.main(new String[]{inputFilePath, actualOutputFilePath});

        List<String> expectedLines = Files.readAllLines(Paths.get(expectedOutputFilePath));
        List<String> actualLines = Files.readAllLines(Paths.get(actualOutputFilePath));

        assertEquals(expectedLines, actualLines);
    }
}
