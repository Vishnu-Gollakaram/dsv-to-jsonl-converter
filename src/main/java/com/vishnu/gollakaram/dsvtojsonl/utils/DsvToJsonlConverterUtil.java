package com.vishnu.gollakaram.dsvtojsonl.utils;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * Utility class for DSV to JSONL conversion.
 */
public class DsvToJsonlConverterUtil {

    /**
     * Converts DSV values to a JSON object.
     *
     * @param header Array of header names.
     * @param values Array of values.
     * @return Map representing the JSON object.
     * @throws ParseException If an error occurs during parsing.
     */
    public Map<String, Object> convertToJsonObject(String[] header, String[] values) throws ParseException {
        int numElements = Math.min(header.length, values.length);

        return IntStream.range(0, numElements)
                .boxed()
                .filter(i -> !values[i].trim().isEmpty())
                .collect(LinkedHashMap::new, (map, i) -> {
                    String key = header[i].trim();
                    String value = values[i].trim();
                    Object parsedValue = parseValue(value);
                    map.put(key, parsedValue);
                }, LinkedHashMap::putAll);
    }

    /**
     * Checks if the given date is valid.
     *
     * @param year  Year.
     * @param month Month.
     * @param day   Day.
     * @return True if the date is valid, false otherwise.
     */
    public static boolean isValidDate(int year, int month, int day) {
        return year > 0 && month >= 1 && month <= 12 && day >= 1 && day <= 31;
    }

    /**
     * Tries to parse a date from the given value.
     *
     * @param value Date value.
     * @return Optional containing the formatted date if successful, empty otherwise.
     */
    public static Optional<Object> tryParseDate(String value) {
        Optional<Object> formattedDate = Optional.empty();
        try {
            String[] dateParts = value.split("[/-]");
            boolean[] dayParsed = {false};
            int[] yearMonthDay = new int[3];

            IntStream.range(0, dateParts.length).forEach(i -> {
                String datePart = dateParts[i];
                if (datePart.length() == 4) {
                    yearMonthDay[0] = Integer.parseInt(datePart);
                } else if (!dayParsed[0]) {
                    yearMonthDay[2] = Integer.parseInt(datePart);
                    dayParsed[0] = true;
                } else {
                    yearMonthDay[1] = Integer.parseInt(datePart);
                }
            });

            formattedDate = Optional.of(yearMonthDay)
                    .map(array -> {
                        int year = array[0];
                        int month = array[1];
                        int day = array[2];

                        if (month > 12) {
                            day = day + month;
                            month = day - month;
                            day = day - month;
                        }

                        return isValidDate(year, month, day) ?
                                LocalDate.of(year, month, day).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) :
                                null;
                    });
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignored) {
        }
        return formattedDate;
    }

    /**
     * Checks if the given value can be parsed as a long.
     *
     * @param value Value to check.
     * @return True if the value can be parsed as a long, false otherwise.
     */
    public Boolean isLong(String value) {
        try {
            Long.parseLong(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the given value can be parsed as a double.
     *
     * @param value Value to check.
     * @return True if the value can be parsed as a double, false otherwise.
     */
    public Boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Parses the value using different converters (long, double, date).
     *
     * @param value Value to parse.
     * @return Parsed value or original value if parsing is not successful.
     */
    public Object parseValue(String value) {
        List<Function<String, Optional<Object>>> converters = Arrays.asList(
                DsvToJsonlConverterUtil::tryParseLong,
                DsvToJsonlConverterUtil::tryParseDouble,
                DsvToJsonlConverterUtil::tryParseDate
        );

        return converters.stream()
                .map(converter -> converter.apply(value))
                .filter(Optional::isPresent)
                .findFirst()
                .orElse(Optional.of(value))
                .orElse(null);
    }

    /**
     * Tries to parse the given value as a long.
     *
     * @param value Value to parse.
     * @return Optional containing the parsed long if successful, empty otherwise.
     */
    private static Optional<Object> tryParseLong(String value) {
        try {
            return Optional.of(Long.parseLong(value));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Tries to parse the given value as a double.
     *
     * @param value Value to parse.
     * @return Optional containing the parsed double if successful, empty otherwise.
     */
    private static Optional<Object> tryParseDouble(String value) {
        try {
            return Optional.of(Double.parseDouble(value));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
