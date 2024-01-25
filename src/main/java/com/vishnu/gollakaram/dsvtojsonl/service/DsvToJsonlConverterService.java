package com.vishnu.gollakaram.dsvtojsonl.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vishnu.gollakaram.dsvtojsonl.utils.DsvToJsonlConverterUtil;

import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Service class for converting DSV (Delimiter Separated Values) to JSONL (JSON Lines) format.
 */
public class DsvToJsonlConverterService {

    /**
     * Converts DSV lines to JSONL format and writes the result to the specified output.
     *
     * @param lines     List of DSV lines to convert.
     * @param output    Writer to which the JSONL output will be written.
     * @param delimiter Delimiter character used in DSV.
     * @throws IOException    If an I/O error occurs.
     * @throws ParseException If an error occurs during parsing.
     */
    public static void convertDsvToJsonl(List<String> lines, Writer output, Character delimiter) throws IOException, ParseException {
        DsvToJsonlConverterUtil util = new DsvToJsonlConverterUtil();
        String[] header;

        if (Objects.isNull(delimiter)) {
            delimiter = lines.get(0).contains("|") ? '|' : ',';
            header = lines.get(0).split("[,|]");
        }

        else {
            header = lines.get(0).split("[" + delimiter + "]");
        }

        Character finalDelimiter = delimiter;
        List<Map<String, Object>> convertedEntries = lines.stream()
                .skip(1)
                .map(line -> parseDsvLine(line, finalDelimiter))
                .map(values -> {
                    try {
                        return util.convertToJsonObject(header, values);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        ObjectMapper objectMapper = new ObjectMapper();

        convertedEntries.forEach(jsonObject -> {
            try {
                String jsonLine = objectMapper.writeValueAsString(jsonObject)
                        .replaceAll(":([^\\s\\{\\[])", ": $1");
                output.write(jsonLine + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Parses a DSV line into an array of values using the specified delimiter.
     *
     * @param line      DSV line to parse.
     * @param delimiter Delimiter character used in DSV.
     * @return Array of values parsed from the DSV line.
     */
    public static String[] parseDsvLine(String line, Character delimiter) {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        AtomicInteger insideQuotes = new AtomicInteger(0);

        line.chars().forEach(c -> {
            char currentChar = (char) c;

            if (currentChar == delimiter) {
                if (insideQuotes.get() % 2 == 0) {
                    values.add(current.toString().trim());
                    current.setLength(0);
                    return;
                }
            } else if (currentChar == '\"') {
                insideQuotes.getAndIncrement();
                return;
            }

            current.append(currentChar);
        });

        values.add(current.toString().trim());
        return values.toArray(new String[0]);
    }
}
