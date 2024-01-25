package com.vishnu.gollakaram.dsvtojsonl;

import java.io.*;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

import com.vishnu.gollakaram.dsvtojsonl.service.DsvToJsonlConverterService;

/**
 * Main class for converting DSV (Delimiter Separated Values) to JSONL (JSON Lines) format.
 */
public class DsvToJsonlConverter {

    private static final String DATE_FORMAT = "YYYY-MM-dd";
    private static Character parentSplitBy = null;

    /**
     * Main method to execute the DSV to JSONL conversion.
     *
     * @param args Command-line arguments. Requires at least 2 arguments: input_file and output_file.
     *             Optionally, a third argument can be provided as the delimiter character.
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("\"Usage: java -jar DsvToJsonlConverter-1-jar-with-dependencies.jar <input_file> <output_file> [delimiter]\"");
            System.exit(1);
        }

        String inputFile = args[0];
        String outputFile = args[1];
        Character delimiter = args.length >= 3 ? args[2].charAt(0) : null;
        DsvToJsonlConverterService service = new DsvToJsonlConverterService();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            List<String> lines = reader.lines().collect(Collectors.toList());

            service.convertDsvToJsonl(lines, writer, delimiter);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}