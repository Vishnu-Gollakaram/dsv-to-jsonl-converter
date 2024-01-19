package com.vishnu.gollakaram.dsvtojsonl;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DsvToJsonlConverter {

    private static final String DATE_FORMAT = "YYYY-MM-dd";
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
    private static Character parentSplitBy = null;

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java -jar DsvToJsonlConverter.jar <input_file> <output_file>");
            System.exit(1);
        }

        String inputFile = args[0];
        String outputFile = args[1];

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            convertDsvToJsonl(lines, writer);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static void convertDsvToJsonl(List<String> lines, Writer output) throws IOException, ParseException {
        String[] header = lines.get(0).split("[,|]");
        List<Map<String, Object>> convertedEntries = new ArrayList<>();

        if (lines.get(0).contains(",")) {
            parentSplitBy = ',';
        } else {
            parentSplitBy = '|';
        }

        for (int i = 1; i < lines.size(); i++) {
            String[] values = parseDsvLine(lines.get(i));
            Map<String, Object> jsonObject = convertToJsonObject(header, values);
            convertedEntries.add(jsonObject);
        }

        ObjectMapper objectMapper = new ObjectMapper();

        for (Map<String, Object> jsonObject : convertedEntries) {
            String jsonLine = objectMapper.writeValueAsString(jsonObject)
                    .replaceAll(":([^\\s\\{\\[])", ": $1");
            output.write(jsonLine + "\n");
        }
    }

    private static String[] parseDsvLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean insideQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == parentSplitBy) {
                if (!insideQuotes) {
                    values.add(current.toString().trim());
                    current.setLength(0);
                    continue;
                }
            } else if (c == '\"') {
                insideQuotes = !insideQuotes;
                continue;
            }

            current.append(c);
        }

        values.add(current.toString().trim());
        return values.toArray(new String[0]);
    }


    private static Map<String, Object> convertToJsonObject(String[] header, String[] values) throws ParseException {
        Map<String, Object> jsonObject = new LinkedHashMap<>();
        int numElements = Math.min(header.length, values.length);

        for (int i = 0; i < numElements; i++) {
            String key = header[i].trim();
            String value = values[i].trim();
            if (!value.isEmpty()) {
                Object parsedValue = parseValue(value);
                jsonObject.put(key, parsedValue);
            }
        }
        return jsonObject;
    }

    private static Object parseValue(String value) throws ParseException {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e1) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e2) {
                try {
                    String[] dateParts = value.split("[/-]");
                    int year = -1;
                    int month = -1;
                    int day = -1;
                    boolean dayParsed = false;

                    for (String datePart : dateParts) {
                        if (datePart.length() == 4) {
                            year = Integer.parseInt(datePart);
                        } else if (!dayParsed) {
                            day = Integer.parseInt(datePart);
                            dayParsed = true;
                        } else {
                            month = Integer.parseInt(datePart);
                        }
                    }

                    if (month > 12) {
                        day = day + month;
                        month = day - month;
                        day = day - month;
                    }

                    if (isValidDate(year, month, day)) {
                        LocalDate date = LocalDate.of(year, month, day);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        return date.format(formatter);
                    }
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignored) {
                }

                return value;
            }
        }
    }

    private static boolean isValidDate(int year, int month, int day) {
        return year > 0 && month >= 1 && month <= 12 && day >= 1 && day <= 31;
    }
}
